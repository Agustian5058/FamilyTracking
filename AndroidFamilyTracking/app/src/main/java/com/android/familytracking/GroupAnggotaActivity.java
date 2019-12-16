package com.android.familytracking;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupAnggotaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolbar;
    private NavigationView mView;
    private static final String TAG = "GRP_AGT_ACTIVITY";
    private ListView memberGroupList;
    private List<MemberGroup> mMemberGroupList;
    private String GroupId, MemberName, MemberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_anggota);
        Intent intent = getIntent();

        DBHandler SQLite = new DBHandler(this);
        MemberId = SQLite.getMember();

        GroupId = intent.getStringExtra("GroupId");
        MemberName = intent.getStringExtra("MemberName");
        memberGroupList = (ListView)findViewById(R.id.anggota_list);
        mMemberGroupList = new ArrayList<MemberGroup>();

        displayMember();

        mLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToolbar = (Toolbar)findViewById(R.id.navigation_actionbar);

        setSupportActionBar(mToolbar);
        mToogle = new ActionBarDrawerToggle(this, mLayout, R.string.open, R.string.close);

        mLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mView = (NavigationView) findViewById(R.id.navigation_view);
        mView.setNavigationItemSelectedListener(GroupAnggotaActivity.this);
    }

    public void displayMember(){
        DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Group").child(GroupId).child("MemberGroup");
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMemberGroupList.clear();
                for(DataSnapshot groupSnapshot:dataSnapshot.getChildren()){
                    MemberGroup memberGroup = groupSnapshot.getValue(MemberGroup.class);
                    if(memberGroup.getMemberId().equals(MemberId)){
                        registerForContextMenu(memberGroupList);
                    }else{
                        mMemberGroupList.add(memberGroup);
                    }
                }
                MemberGroupList adapter = new MemberGroupList(GroupAnggotaActivity.this, mMemberGroupList);
                memberGroupList.setAdapter(adapter);
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
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int IndexSelected = info.position;
        if(item.getTitle()=="Delete") {
            DatabaseReference Reference = FirebaseDatabase.getInstance()
                    .getReference("Group").child(GroupId).child("MemberGroup");
            Reference.child(mMemberGroupList.get(IndexSelected).getMemberId()).removeValue();
            final DatabaseReference rootRef = FirebaseDatabase.getInstance()
                    .getReference("Member").child(mMemberGroupList.get(IndexSelected).getMemberId());
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Member member = dataSnapshot.getValue(Member.class);
                    if(GroupId.equals(member.getGroup1())){
                        member.setGroup1(member.getGroup2());
                        member.setGroup2("");
                    }else{
                        member.setGroup2("");
                    }
                    rootRef.setValue(member);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            displayMember();
        } else {
            return false;
        }
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.Group_Menu){
            Toast.makeText(this, "Grup Menu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupAnggotaActivity.this, GroupActivity.class));
            finish();
        }
        else if(id == R.id.Schedule_Menu){
            Toast.makeText(this, "Jadwal", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupAnggotaActivity.this, ScheduleActivity.class));
        }
        else if(id == R.id.Location_Menu){
            Toast.makeText(this, "Lokasi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupAnggotaActivity.this, LocationActivity.class));
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
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(GroupAnggotaActivity.this, GroupDetailActivity.class);
        i.putExtra("GroupId", GroupId);
        i.putExtra("MemberName", MemberName);
        startActivity(i);
        finish();
    }
}
