package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {
    ImageButton logout;
    TextView dept,name;
    Button takeatt,seeatt;
    List<String> list=new ArrayList<>();
    String dp,nm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        logout=findViewById(R.id.btn_logout);
        dept=findViewById(R.id.tx_dept);
        name=findViewById(R.id.tx_name);
        takeatt=findViewById(R.id.btn_takeatt);
        seeatt=findViewById(R.id.btn_seeatt);

        String uid=getIntent().getStringExtra("uid");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Teachers");

        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    nm=dataSnapshot.child("name").getValue(String.class);
                    if (!dataSnapshot.getKey().equals("name") && !dataSnapshot.getKey().equals("usertype")) {
                        dp = dataSnapshot.getKey();
                        reference.child(uid).child(dp).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    String yr = dataSnapshot1.getKey();
                                    String sem, sub;
                                    if (snapshot.child(yr).child("semester-1").exists()) {
                                        sem = "semester-1";
                                    } else {
                                        sem = "semester-2";
                                    }
                                    sub = snapshot.child(yr).child(sem).getValue(String.class);
                                    list.add(dp);
                                    list.add(yr);
                                    list.add(sem);
                                    list.add(sub);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        name.setText(nm);
        dept.setText(dp);

        takeatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherActivity.this,TakeAttSubjectActivity.class);
                intent.putExtra("uid",uid);
                intent.putStringArrayListExtra("list", (ArrayList<String>) list);
                intent.putExtra("from","takeatt");
                startActivity(intent);
            }
        });
        seeatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherActivity.this,TakeAttSubjectActivity.class);
                intent.putExtra("uid",uid);
                intent.putStringArrayListExtra("list", (ArrayList<String>) list);
                intent.putExtra("from","seeatt");
                startActivity(intent);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(TeacherActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}