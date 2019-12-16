package com.android.familytracking;

import android.content.Intent;
import android.os.Handler;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolbar;
    private NavigationView mView;
    private static final String TAG = "GROUP_ACTIVITY";
    private ImageButton Grup1, Grup2;
    private TextView Grup1Name, Grup2Name;
    private String MemberId, MemberName;
    private String[] Grup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        DBHandler SQLite = new DBHandler(this);
        MemberId = SQLite.getMember();

        Grup = CheckGrup();

        Grup1 = (ImageButton) findViewById(R.id.Grup1);
        Grup2 = (ImageButton) findViewById(R.id.Grup2);
        Grup1Name = (TextView) findViewById(R.id.Grup1Name);
        Grup2Name = (TextView) findViewById(R.id.Grup2Name);


        Log.e(TAG, "Before Delay");
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Grup[0].equals("")){
                    Grup1.setImageResource(R.drawable.add_group);
                    Grup1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAddGroupDialog(1);
                        }
                    });
                    Grup1Name.setText("Add Group");
                }else{
                    Grup1.setImageResource(R.drawable.group);
                    CheckGrupName(Grup[0], 1);
                    Grup1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i=new Intent(GroupActivity.this, GroupDetailActivity.class);
                            i.putExtra("GroupId", Grup[0]);
                            i.putExtra("MemberName", MemberName);
                            startActivity(i);
                            finish();
                        }
                    });
                    if(Grup[1].equals("")){
                        Grup2.setImageResource(R.drawable.add_group);
                        Grup2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showAddGroupDialog(2);
                            }
                        });
                        Grup2Name.setText("Add Group");
                    }else{
                        Grup2.setImageResource(R.drawable.group);
                        CheckGrupName(Grup[1], 2);
                        Grup2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i=new Intent(GroupActivity.this, GroupDetailActivity.class);
                                i.putExtra("GroupId", Grup[1]);
                                i.putExtra("MemberName", MemberName);
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                }
            }
        }, 2000);
        Log.e(TAG, "After Delay");

        mLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToolbar = (Toolbar)findViewById(R.id.navigation_actionbar);

        setSupportActionBar(mToolbar);
        mToogle = new ActionBarDrawerToggle(this, mLayout, R.string.open, R.string.close);

        mLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mView = (NavigationView) findViewById(R.id.navigation_view);
        mView.setNavigationItemSelectedListener(GroupActivity.this);
    }

    private String[] CheckGrup(){
        final String[] grup = {"", ""};
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Member");
        DatabaseReference Reference2 = rootRef.child(MemberId);
        Reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                grup[0] = member.getGroup1();
                grup[1] = member.getGroup2();
                MemberName = member.getMemberName();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return grup;
    }

    private void CheckGrupName(String Code, final int GrupKe){
        final String[] grup = {""};
        DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Group");
        Reference.child(Code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Group group = dataSnapshot.getValue(Group.class);
                grup[0] = group.getGroupName();
                if (GrupKe == 1){
                    Grup1Name.setText(grup[0]);
                }
                else if (GrupKe == 2){
                    Grup2Name.setText(grup[0]);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void showAddGroupDialog(final int IdGroup){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.group_add_layout, null);
        dialogBuilder.setView(dialogView);
        final EditText ETNameGroup = (EditText) dialogView.findViewById(R.id.GroupName);
        final EditText ETCodeGroup = (EditText) dialogView.findViewById(R.id.GroupCode);
        final Button Create = (Button) dialogView.findViewById(R.id.CreateGroup);
        final Button Join = (Button) dialogView.findViewById(R.id.JoinGroup);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ETNameGroup.getText())){
                    ETNameGroup.setError("Name required");
                    return;
                }else{
                    insertNewGroup(ETNameGroup.getText().toString(), IdGroup);
                    Toast.makeText(getApplicationContext(), "New Group Added", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });

        Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ETCodeGroup.getText())){
                    ETCodeGroup.setError("Code required");
                    return;
                }else{
                    if(joinNewGroup(ETCodeGroup.getText().toString(), IdGroup)){
                        Toast.makeText(getApplicationContext(), "Join New Group", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }else{
                        ETCodeGroup.setError("Code Unavailable");
                    }
                }
            }
        });
        dialogBuilder.setTitle("Menambahkan Grup Baru");
    }

    public void insertNewGroup(String GroupName, final int IdGroup){
        DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Group");//root member
        final String id = Reference.push().getKey();//generate unique key id

        Group group = new Group();
        group.setGroupId(id);
        group.setGroupName(GroupName);
        group.setGroupAdmin(MemberId);
        Reference.child(id).setValue(group);
        MemberGroup memberGroup = new MemberGroup();
        memberGroup.setMemberId(MemberId);
        Reference.child(id).child("MemberGroup").child(MemberId).setValue(memberGroup);

        final Member[] member = {new Member()};
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Member");
        final DatabaseReference Reference2 = rootRef.child(MemberId);
        Reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                member[0] = dataSnapshot.getValue(Member.class);
                SendingEmail sender = new SendingEmail(id, member[0].getMemberEmail());
                if(IdGroup == 1){
                    member[0].setGroup1(id);
                }else{
                    member[0].setGroup2(id);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Reference2.setValue(member[0]);
                startActivity(new Intent(GroupActivity.this, GroupActivity.class));
                finish();
            }
        }, 2000);

        Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show();
    }

    public boolean joinNewGroup(final String GroupId, final int IdGroup){
        final boolean[] AvailableGroup = {false};
        final DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Group").child(GroupId);//root member child

        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    MemberGroup memberGroup = new MemberGroup();
                    memberGroup.setMemberId(MemberId);
                    Reference.child("MemberGroup").child(MemberId).setValue(memberGroup);
                    AvailableGroup[0] = true;
                    final Member[] member = {new Member()};
                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Member");
                    final DatabaseReference Reference2 = rootRef.child(MemberId);
                    Reference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            member[0] = dataSnapshot.getValue(Member.class);
                            if(IdGroup == 1){
                                member[0].setGroup1(GroupId);
                            }else{
                                member[0].setGroup2(GroupId);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                    final Handler handler = new Handler();
                    Log.e(TAG, "Before Delay");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Reference2.setValue(member[0]);
                            startActivity(new Intent(GroupActivity.this, GroupActivity.class));
                            finish();
                        }
                    }, 2000);
                    Log.e(TAG, "After Delay");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show();
        return AvailableGroup[0];
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.Group_Menu){
            Toast.makeText(this, "Grup Menu", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupActivity.this, GroupActivity.class));
            finish();
        }
        else if(id == R.id.Schedule_Menu){
            Toast.makeText(this, "Jadwal", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupActivity.this, ScheduleActivity.class));
        }
        else if(id == R.id.Location_Menu){
            Toast.makeText(this, "Lokasi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GroupActivity.this, LocationActivity.class));
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
        Intent i=new Intent(GroupActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
