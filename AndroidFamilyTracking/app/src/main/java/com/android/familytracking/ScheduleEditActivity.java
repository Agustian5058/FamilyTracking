package com.android.familytracking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ScheduleEditActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener, OnMapReadyCallback, DatePickerDialog.OnDateSetListener  {

    private DrawerLayout mLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolbar;
    private NavigationView mView;
    private static final String TAG = "SCHEDULE_EDIT_ACTIVITY";
    private String MemberId, schedule_hour_begin, schedule_minutes_begin, schedule_hour_finish, schedule_minutes_finish, scheduleId;
    String[] jam = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};
    String[] menit = {"00", "15", "30", "45"};
    private EditText kegiatan;
    private TextView longitude, latitude, hari, tanggal;
    private Spinner jam_mulai,menit_mulai,jam_selesai,menit_selesai;
    private CheckBox rutin, notifikasi;
    private Button edit, getDate;
    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);
        DBHandler SQLite = new DBHandler(this);
        MemberId = SQLite.getMember();

        Intent intent = getIntent();
        scheduleId = intent.getStringExtra("scheduleId");

        longitude = (TextView) findViewById(R.id.longitude);
        latitude = (TextView) findViewById(R.id.latitude);
        kegiatan = (EditText) findViewById(R.id.kegiatan);
        hari = (TextView) findViewById(R.id.hari);
        tanggal = (TextView) findViewById(R.id.tanggal);
        jam_mulai = (Spinner) findViewById(R.id.jam_mulai);
        menit_mulai = (Spinner) findViewById(R.id.menit_mulai);
        jam_selesai = (Spinner) findViewById(R.id.jam_selesai);
        menit_selesai = (Spinner) findViewById(R.id.menit_selesai);
        rutin = (CheckBox) findViewById(R.id.rutin);
        notifikasi = (CheckBox) findViewById(R.id.notifikasi);
        edit = (Button) findViewById(R.id.add);
        getDate = (Button) findViewById(R.id.getDate);

        edit.setText("Edit Schedule");

        jam_mulai.setOnItemSelectedListener(this);
        menit_mulai.setOnItemSelectedListener(this);
        jam_selesai.setOnItemSelectedListener(this);
        menit_selesai.setOnItemSelectedListener(this);

        ArrayAdapter jamadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, jam);
        jamadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jam_mulai.setAdapter(jamadapter);
        jam_selesai.setAdapter(jamadapter);

        ArrayAdapter menitadapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, menit);
        menitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menit_mulai.setAdapter(menitadapter);
        menit_selesai.setAdapter(menitadapter);

        showRecord();

        showLocationDialog();

        getDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(kegiatan.getText())){
                    kegiatan.setError("Masukkan Kegiatan");
                    return;
                }
                else if(longitude.getText().equals("") || longitude.getText().equals("Tentukan Lokasi")) {
                    longitude.setText("Tentukan Lokasi");
                    latitude.setText("Tentukan Lokasi");
                    return;
                }
                else if(!rutin.isChecked() && hari.getText().equals("") || hari.getText().equals("Tentukan Tanggal")) {
                    hari.setText("Tentukan Tanggal");
                    return;
                }
                else {
                    DatabaseReference Reference = FirebaseDatabase.getInstance()
                            .getReference("Schedule").child(MemberId);
                    Schedule schedule = new Schedule();
                    schedule.setSchedule(kegiatan.getText().toString());
                    schedule.setScheduleBegin(schedule_hour_begin +":"+ schedule_minutes_begin);
                    schedule.setScheduleDay(hari.getText().toString());
                    schedule.setScheduleFinish(schedule_hour_finish +":"+ schedule_minutes_finish);
                    schedule.setScheduleId(scheduleId);
                    schedule.setScheduleLongitude(longitude.getText().toString());
                    schedule.setScheduleLatitude(latitude.getText().toString());
                    if (rutin.isChecked()){
                        schedule.setScheduleDate("Routine");
                    }else{
                        schedule.setScheduleDate(tanggal.getText().toString());
                    }
                    if (notifikasi.isChecked()){
                        schedule.setScheduleNotification("On");
                    }else{
                        schedule.setScheduleNotification("Off");
                    }
                    Reference.child(scheduleId).setValue(schedule);
                    startActivity(new Intent(ScheduleEditActivity.this, ScheduleActivity.class));
                }
            }
        });

        mLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToolbar = (Toolbar)findViewById(R.id.navigation_actionbar);

        setSupportActionBar(mToolbar);
        mToogle = new ActionBarDrawerToggle(this, mLayout, R.string.open, R.string.close);

        mLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mView = (NavigationView) findViewById(R.id.navigation_view);
        mView.setNavigationItemSelectedListener(ScheduleEditActivity.this);
    }

    public void showRecord(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Schedule").child(MemberId).child(scheduleId);
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Schedule schedule = dataSnapshot.getValue(Schedule.class);
                kegiatan.setText(schedule.getSchedule());
                latitude.setText(schedule.getScheduleLatitude());
                longitude.setText(schedule.getScheduleLongitude());
                hari.setText(schedule.getScheduleDay());
                if(schedule.getScheduleDate().equals("Routine")){
                    rutin.setChecked(true);
                }else{
                    tanggal.setText(schedule.getScheduleDate());
                }
                if(schedule.getScheduleNotification().equals("On")){
                    notifikasi.setChecked(true);
                }else{
                    notifikasi.setChecked(false);
                }
                jam_mulai.setSelection(getIndex(jam_mulai, schedule.getScheduleBegin().substring(0, 2)));
                Log.e(TAG, schedule.getScheduleBegin().substring(0, 2));
                menit_mulai.setSelection(getIndex(menit_mulai, schedule.getScheduleBegin().substring(3)));
                Log.e(TAG, schedule.getScheduleBegin().substring(3));
                jam_selesai.setSelection(getIndex(jam_selesai, schedule.getScheduleFinish().substring(0, 2)));
                menit_selesai.setSelection(getIndex(menit_selesai, schedule.getScheduleFinish().substring(3)));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private int getIndex(Spinner spinner, String myString){
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    public void showDatePicker(View view){
        ScheduleEditActivity.DatePickerFragment fragment = new ScheduleEditActivity.DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        tanggal.setText(day+"-"+(month+1)+"-"+year);
        int result = cal.get(Calendar.DAY_OF_WEEK);
        switch (result) {
            case Calendar.MONDAY:
                hari.setText("Senin");
                break;
            case Calendar.TUESDAY:
                hari.setText("Selasa");
                break;
            case Calendar.WEDNESDAY:
                hari.setText("Rabu");
                break;
            case Calendar.THURSDAY:
                hari.setText("Kamis");
                break;
            case Calendar.FRIDAY:
                hari.setText("Jumat");
                break;
            case Calendar.SATURDAY:
                hari.setText("Sabtu");
                break;
            case Calendar.SUNDAY:
                hari.setText("Minggu");
                break;
        }
    }

    public void showLocationDialog(){
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Schedule").child(MemberId).child(scheduleId);
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Schedule schedule = dataSnapshot.getValue(Schedule.class);
                LatLng location = new LatLng(Double.parseDouble(schedule.getScheduleLatitude()), Double.parseDouble(schedule.getScheduleLongitude()));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                mMap.addMarker(new MarkerOptions().position(location).title("Current Schedule"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(300));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                String[] hasil = point.toString().split(",");
                latitude.setText(hasil[0].substring(10));
                longitude.setText(hasil[1].substring(0, hasil[1].length()-1));
            }
        });
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.Group_Menu){
            Toast.makeText(this, "Grup Menu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ScheduleEditActivity.this, GroupActivity.class));
            finish();
        }
        else if(id == R.id.Schedule_Menu){
            Toast.makeText(this, "Jadwal", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ScheduleEditActivity.this, ScheduleActivity.class));
        }
        else if(id == R.id.Location_Menu){
            Toast.makeText(this, "Lokasi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ScheduleEditActivity.this, LocationActivity.class));
        }
        else if(id == R.id.Track_Menu){
            Toast.makeText(this, "Track", Toast.LENGTH_SHORT).show();
        }
        mLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (mToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.jam_mulai){
            schedule_hour_begin = jam[i];
            Log.e(TAG, "schedule_hour_begin = " + jam[i]);
        }
        if(adapterView.getId() == R.id. menit_mulai){
            schedule_minutes_begin = menit[i];
            Log.e(TAG, "schedule_minutes_begin = " + menit[i]);
        }
        if(adapterView.getId() == R.id.jam_selesai){
            schedule_hour_finish = jam[i];
            Log.e(TAG, "schedule_hour_finish = " + jam[i]);
        }
        if(adapterView.getId() == R.id.menit_selesai){
            schedule_minutes_finish = menit[i];
            Log.e(TAG, "schedule_minutes_finish = " + menit[i]);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(ScheduleEditActivity.this, ScheduleActivity.class);
        startActivity(i);
        finish();
    }
}
