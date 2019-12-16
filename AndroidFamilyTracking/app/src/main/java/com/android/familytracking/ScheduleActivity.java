package com.android.familytracking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolbar;
    private NavigationView mView;
    private static final String TAG = "SCHEDULE_ACTIVITY";
    private ImageButton add_schedule;
    private ListView scheduleList;
    private List<Schedule> mScheduleList;
    private String MemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        add_schedule = (ImageButton)findViewById(R.id.add_schedule);
        scheduleList = (ListView)findViewById(R.id.schedule_list);
        registerForContextMenu(scheduleList);

        DBHandler SQLite = new DBHandler(this);
        MemberId = SQLite.getMember();

        mScheduleList = new ArrayList<Schedule>();

        displaySchedule();

        add_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ScheduleActivity.this, ScheduleAddActivity.class));
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
        mView.setNavigationItemSelectedListener(ScheduleActivity.this);
    }

    public void displaySchedule(){
        DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Schedule").child(MemberId);
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mScheduleList.clear();
                for(DataSnapshot groupSnapshot:dataSnapshot.getChildren()){
                    Schedule schedule = groupSnapshot.getValue(Schedule.class);
                    mScheduleList.add(schedule);
                }
                ScheduleList adapter = new ScheduleList(ScheduleActivity.this, mScheduleList);
                scheduleList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Edit");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int IndexSelected = info.position;
        if(item.getTitle()=="Edit") {
            Intent intent=new Intent(ScheduleActivity.this, ScheduleEditActivity.class);
            intent.putExtra("scheduleId", mScheduleList.get(IndexSelected).getScheduleId());
            startActivity(intent);
            finish();
        } else if(item.getTitle()=="Delete") {
            DatabaseReference Reference = FirebaseDatabase.getInstance()
                    .getReference("Schedule").child(MemberId);
            Reference.child(mScheduleList.get(IndexSelected).getScheduleId()).removeValue();
            displaySchedule();
        } else {
            return false;
        }
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.Group_Menu){
            Toast.makeText(this, "Grup Menu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ScheduleActivity.this, GroupActivity.class));
            finish();
        }
        else if(id == R.id.Schedule_Menu){
            Toast.makeText(this, "Jadwal", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ScheduleActivity.this, ScheduleActivity.class));
        }
        else if(id == R.id.Location_Menu){
            Toast.makeText(this, "Lokasi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ScheduleActivity.this, LocationActivity.class));
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
}
