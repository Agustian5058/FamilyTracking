package com.android.familytracking;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
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

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolbar;
    private NavigationView mView;
    private static final String TAG = "GRP_DETAIL_ACTIVITY";
    private String MemberName, GroupId, MemberId;
    private String[] GroupMember;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GroupId");
        MemberName = intent.getStringExtra("MemberName");

        DBHandler SQLite = new DBHandler(this);
        MemberId = SQLite.getMember();

        getMember();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToolbar = (Toolbar)findViewById(R.id.navigation_actionbar);

        setSupportActionBar(mToolbar);
        mToogle = new ActionBarDrawerToggle(this, mLayout, R.string.open, R.string.close);

        mLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mView = (NavigationView) findViewById(R.id.navigation_view);
        mView.setNavigationItemSelectedListener(TrackActivity.this);
    }

    public void getMember(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Group").child(GroupId).child("MemberGroup");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot groupSnapshot:dataSnapshot.getChildren()){
                    MemberGroup member = groupSnapshot.getValue(MemberGroup.class);
                    final String memberId = member.getMemberId();
                    Log.e(TAG, "Member Id = " + memberId);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Location").child(MemberId);
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                Lokasi lokasi = dataSnapshot.getValue(Lokasi.class);
                LatLng location = new LatLng(lokasi.getLatitude(), lokasi.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location);
                mMap.addMarker(new MarkerOptions().position(location).title(MemberName));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(300));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Group").child(GroupId).child("MemberGroup");
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot groupSnapshot:dataSnapshot.getChildren()){
                    MemberGroup member = groupSnapshot.getValue(MemberGroup.class);
                    final String memberId = member.getMemberId();
                    Log.e(TAG, "Member Id = " + memberId);

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Location").child(memberId);
                    rootRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            Lokasi lokasi = dataSnapshot1.getValue(Lokasi.class);
                            Log.e(TAG, "Member Id = " + memberId);
                            double Longitude = lokasi.getLongitude();
                            Log.e(TAG, "Longitude = " + Longitude);
                            double Latitude = lokasi.getLatitude();
                            Log.e(TAG, "Latitude = " + Latitude);
                            LatLng location = new LatLng(Latitude, Longitude);
                            mMap.addMarker(new MarkerOptions().position(location).title(Latitude +", " + Longitude));
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.Group_Menu){
            Toast.makeText(this, "Grup Menu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TrackActivity.this, GroupActivity.class));
            finish();
        }
        else if(id == R.id.Schedule_Menu){
            Toast.makeText(this, "Jadwal", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TrackActivity.this, ScheduleActivity.class));
        }
        else if(id == R.id.Location_Menu){
            Toast.makeText(this, "Lokasi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TrackActivity.this, LocationActivity.class));
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

    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(TrackActivity.this, GroupDetailActivity.class);
        i.putExtra("GroupId", GroupId);
        i.putExtra("MemberName", MemberName);
        startActivity(i);
        finish();
    }
}
