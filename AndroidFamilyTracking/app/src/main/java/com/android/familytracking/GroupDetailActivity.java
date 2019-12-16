package com.android.familytracking;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolbar;
    private NavigationView mView;
    private static final String TAG = "GRP_DETAIL_ACTIVITY";
    private Button anggota_grup, track_anggota, jadwal_anggota, chat_anggota;
    private TextView CodeGroup;
    private String MemberName, GroupId, MemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        DBHandler SQLite = new DBHandler(this);
        MemberId = SQLite.getMember();

        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GroupId");
        MemberName = intent.getStringExtra("MemberName");

        anggota_grup = (Button) findViewById(R.id.anggota_grup);
        track_anggota = (Button) findViewById(R.id.track_anggota);
        jadwal_anggota = (Button) findViewById(R.id.jadwal_anggota);
        chat_anggota = (Button) findViewById(R.id.chat_anggota);
        CodeGroup = (TextView) findViewById(R.id.CodeGroup);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Group").child(GroupId);
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group grup = dataSnapshot.getValue(Group.class);
                if(MemberId.equals(grup.getGroupAdmin())){
                    CodeGroup.setText("Your Group Code : " + grup.getGroupId());
                    anggota_grup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i=new Intent(GroupDetailActivity.this, GroupAnggotaActivity.class);
                            i.putExtra("GroupId", GroupId);
                            i.putExtra("MemberName", MemberName);
                            startActivity(i);
                            finish();
                        }
                    });
                }else{
                    anggota_grup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i=new Intent(GroupDetailActivity.this, GroupAnggotaActivity.class);
                            i.putExtra("GroupId", GroupId);
                            i.putExtra("MemberName", MemberName);
                            startActivity(i);
                            finish();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        track_anggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupDetailActivity.this, TrackActivity.class);
                i.putExtra("GroupId", GroupId);
                i.putExtra("MemberName", MemberName);
                startActivity(i);
                finish();
            }
        });

        jadwal_anggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupDetailActivity.this, GroupScheduleActivity.class);
                i.putExtra("GroupId", GroupId);
                i.putExtra("MemberName", MemberName);
                startActivity(i);
                finish();
            }
        });

        chat_anggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupDetailActivity.this, ChatActivity.class);
                i.putExtra("GroupId", GroupId);
                i.putExtra("MemberName", MemberName);
                startActivity(i);
                finish();
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
        mView.setNavigationItemSelectedListener(GroupDetailActivity.this);
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.Group_Menu){
            Toast.makeText(this, "Grup Menu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupDetailActivity.this, GroupActivity.class));
            finish();
        }
        else if(id == R.id.Schedule_Menu){
            Toast.makeText(this, "Jadwal", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupDetailActivity.this, ScheduleActivity.class));
        }
        else if(id == R.id.Location_Menu){
            Toast.makeText(this, "Lokasi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupDetailActivity.this, LocationActivity.class));
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
        Intent i=new Intent(GroupDetailActivity.this, GroupActivity.class);
        startActivity(i);
        finish();
    }
}
