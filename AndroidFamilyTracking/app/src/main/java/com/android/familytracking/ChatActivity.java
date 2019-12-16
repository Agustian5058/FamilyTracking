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
import android.view.MenuItem;
import android.view.View;
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

public class ChatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolbar;
    private NavigationView mView;
    private static final String TAG = "CHAT_ACTIVITY";
    private FloatingActionButton send;
    private EditText messages;
    private ListView chatList;
    private List<Chat> mChatList;
    private String GroupId, MemberName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        GroupId = intent.getStringExtra("GroupId");
        MemberName = intent.getStringExtra("MemberName");

        send = (FloatingActionButton)findViewById(R.id.send);
        messages = (EditText)findViewById(R.id.messages);
        chatList = (ListView)findViewById(R.id.messages_list);

        mChatList = new ArrayList<Chat>();

        displayMessages();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the input field and push a new instance
                // of Chat to the Firebase database
                DatabaseReference Reference = FirebaseDatabase.getInstance()
                        .getReference("Chat").child(GroupId);
                final String id = Reference.push().getKey();//generate unique key id

                Chat chat = new Chat();
                chat.setMessageText(messages.getText().toString());
                chat.setMessageUser(MemberName);
                Reference.child(id).setValue(chat);
                // Clear the input
                messages.setText("");

                displayMessages();
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
        mView.setNavigationItemSelectedListener(ChatActivity.this);
    }

    public void displayMessages(){
        DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Chat").child(GroupId);
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChatList.clear();
                for(DataSnapshot groupSnapshot:dataSnapshot.getChildren()){
                    Chat message = groupSnapshot.getValue(Chat.class);
                    mChatList.add(message);
                }
                ChatList adapter = new ChatList(ChatActivity.this, mChatList);
                chatList.setAdapter(adapter);
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
            startActivity(new Intent(ChatActivity.this, GroupActivity.class));
            finish();
        }
        else if(id == R.id.Schedule_Menu){
            Toast.makeText(this, "Jadwal", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChatActivity.this, ScheduleActivity.class));
        }
        else if(id == R.id.Location_Menu){
            Toast.makeText(this, "Lokasi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ChatActivity.this, LocationActivity.class));
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
        Intent i=new Intent(ChatActivity.this, GroupDetailActivity.class);
        i.putExtra("GroupId", GroupId);
        i.putExtra("MemberName", MemberName);
        startActivity(i);
        finish();
    }
}
