package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
    CardView takeatt,seeatt,sendmessage,uploadnotes;
    ArrayList<String> list=new ArrayList<>();
    String dp,nm,yr;
    String sem, sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        logout=findViewById(R.id.btn_logout);
        dept=findViewById(R.id.tx_dept);
        name=findViewById(R.id.tx_name);
        takeatt=findViewById(R.id.CDTATT);
        seeatt=findViewById(R.id.CDVATT);
        sendmessage=findViewById(R.id.CDSDMSS);
        uploadnotes=findViewById(R.id.CDULPDF);

        String uid=getIntent().getStringExtra("uid");
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("TEACHERS");

        reference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    nm=dataSnapshot.child("NAME").getValue(String.class);
                    if (!dataSnapshot.getKey().equals("NAME") && !dataSnapshot.getKey().equals("EMAIL") && !dataSnapshot.getKey().equals("PASS")) {
                        dp = dataSnapshot.getKey();
                        reference.child(uid).child(dp).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                    yr = dataSnapshot1.getKey();

                                    if (snapshot.child(yr).child("SEMESTER-1").exists()) {
                                        sem = "SEMESTER-1";
                                    } else {
                                        sem = "SEMESTER-2";
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
                intent.putStringArrayListExtra("list", list);
                intent.putExtra("from","takeatt");
                startActivity(intent);
            }
        });
        seeatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherActivity.this,TakeAttSubjectActivity.class);
                intent.putExtra("uid",uid);
                intent.putStringArrayListExtra("list", list);
                intent.putExtra("from","seeatt");
                startActivity(intent);
            }
        });

        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherActivity.this,TakeAttSubjectActivity.class);
                intent.putExtra("from","sendmess");
                intent.putStringArrayListExtra("list", list);
                startActivity(intent);
            }
        });

        uploadnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TeacherActivity.this,TakeAttSubjectActivity.class);
                intent.putExtra("from","upload");
                intent.putStringArrayListExtra("list", list);
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