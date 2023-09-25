package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myattendance.Adapter.AdapterRecView;
import com.example.myattendance.modul.StudAttInfo;
import com.example.myattendance.modul.StudAttendace;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnatt;
    String dept,year,sem,sub,uid;
    Toolbar toolbar;
    List<StudAttInfo> students=new ArrayList<>();
    AdapterRecView adapterRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnatt=findViewById(R.id.btn_att);
        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Attendance");

        dept=getIntent().getStringExtra("dept");
        year=getIntent().getStringExtra("year");
        sem=getIntent().getStringExtra("semester");
        sub=getIntent().getStringExtra("subject");
        uid=getIntent().getStringExtra("uid");

        int[] colors = {Color.RED, Color.GREEN};

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("STUDENTS");
        Query query=reference.orderByChild("ROLL");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String suid=dataSnapshot.getKey();
                    String dp,yr,semm,nm,rl;
                    dp=dataSnapshot.child("DEPARTMENT").getValue(String.class);
                    yr=dataSnapshot.child("YEAR").getValue(String.class);
                    semm=dataSnapshot.child("SEMESTER").getValue(String.class);
                    if(dept.equals(dp) && year.equals(yr) && sem.equals(semm)){
                        nm=dataSnapshot.child("NAME").getValue(String.class);
                        rl=dataSnapshot.child("ROLL").getValue(String.class);

                        StudAttInfo studAttInfo=new StudAttInfo(nm,rl,suid);
                        students.add(studAttInfo);
                    }
                }
                if(!students.isEmpty()) {
                    adapterRecView = new AdapterRecView(students, colors);
                    recyclerView.setAdapter(adapterRecView);
                    adapterRecView.notifyDataSetChanged();
                }else {
                    Toast.makeText(AttendanceActivity.this, "student att list ie empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // In your activity or fragment

// When the Confirm button is clicked
        btnatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the marked attendance data from the adapter
                Map<String,StudAttendace> markedAttendanceList = adapterRecView.getMarkAtt();
                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference();
                // Update the Firebase database with the marked attendance data
                for (String suid : markedAttendanceList.keySet()) {
                    StudAttendace attendance=markedAttendanceList.get(suid);
                    String studentId = suid;
                    boolean isPresent = attendance.isPresent();
                    String attendanceDate = attendance.getDate();
                    String sname=attendance.getName();
                    String sroll=attendance.getRoll();
                    String path = "ATTENDANCE/" + uid + "/" + dept.toUpperCase() + "/" + year.toUpperCase() + "/" + sem.toUpperCase() + "/" + sub.toUpperCase() + "/" + studentId;

                    reference1.child(path).child("ATTENDANCE DATES").child(attendanceDate).setValue(isPresent);
                    reference1.child(path).child("NAME").setValue(sname);
                    reference1.child(path).child("ROLL").setValue(sroll);

                }

                // Clear the markedAttendanceList after updating the database
                adapterRecView.clearMarkAtt();
                // Notify the adapter to update the views with the latest data
                adapterRecView.notifyDataSetChanged();
            }
        });
    }
}