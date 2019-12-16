package com.android.familytracking;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "REGISTER_ACTIVITY";
    private String name, email;
    private String MemberId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Mengambil Nama & Email dari Google
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        name = user.getDisplayName();
        email = user.getEmail();

        Log.e(TAG, "Member Baru, Masukkan ke Database");
        CheckMemberId(email);

        final TextView TVEmail = (TextView) findViewById(R.id.Email);
        TVEmail.setText(email);
        final EditText ETName = (EditText) findViewById(R.id.Name);
        ETName.setText(name);
        final Button Register = (Button) findViewById(R.id.Register);
        DBHandler SQLite = new DBHandler(getApplicationContext());

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(ETName.getText() )){
                    ETName.setError("Name required");
                    return;
                }else{
                    if (insertNewMember(TVEmail.getText().toString(), ETName.getText().toString())){
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    }else{
                        Log.e(TAG, "Insert New Member False");
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        CheckMemberId(email);
    }

    private void CheckMemberId(final String email){
        DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Member");
        Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot groupSnapshot:dataSnapshot.getChildren()){
                    Member member = groupSnapshot.getValue(Member.class);
                    if (member.getMemberEmail().equals(email)){
                        MemberId = member.getMemberId();
                        Log.e(TAG, "Member Id = " + MemberId);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean insertNewMember(String email, String name){
        DatabaseReference Reference = FirebaseDatabase.getInstance()
                .getReference("Member");//root member
        DBHandler SQLite = new DBHandler(this);
        SQLiteDatabase db = SQLite.getWritableDatabase();

        String id = Reference.push().getKey();//generate unique key id
        if (SQLite.insert(id)){
            Log.e(TAG, "Member Id = " + id + " Added to SQLite");
        }

        Member member = new Member();
        member.setMemberEmail(email);
        member.setMemberName(name);
        member.setMemberId(id);

        Reference.child(id).setValue(member);
        Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show();
        return true;
    }
}
