package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myattendance.Adapter.AdViewRecview;
import com.example.myattendance.Adapter.AdapterRecView;
import com.example.myattendance.modul.StudAttInfo;
import com.example.myattendance.modul.StudAttendace;
import com.example.myattendance.modul.ViewStudAtt;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ViewAttendanceActivity extends AppCompatActivity {
    RecyclerView Viewrecview;
    String dept, year, sem, sub, uid;
    Toolbar toolbar;
    List<ViewStudAtt> Vstudents = new ArrayList<>();
    AdViewRecview adViewRecview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        Viewrecview = findViewById(R.id.view_recyclerview);
        Viewrecview.setLayoutManager(new LinearLayoutManager(ViewAttendanceActivity.this));
        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Attendance");

        dept = getIntent().getStringExtra("dept");
        year = getIntent().getStringExtra("year");
        sem = getIntent().getStringExtra("semester");
        sub = getIntent().getStringExtra("subject");
        uid = getIntent().getStringExtra("uid");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ATTENDANCE");

        String path = uid + "/" + dept.toUpperCase() + "/" + year.toUpperCase() + "/" + sem.toUpperCase() + "/" + sub.toUpperCase();
        try {

            reference.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String suid = snapshot.getKey().toString();
                                String roll = snapshot.child("ROLL").getValue(String.class);
                                String name = snapshot.child("NAME").getValue(String.class);
                                int[] pcount = {0};
                                int[] totalcount = {0};
                                float[] percent = {0.0f};

                                reference.child(path).child(suid).child("ATTENDANCE DATES").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().exists()) {
                                                totalcount[0] = (int) task.getResult().getChildrenCount();
                                                for (DataSnapshot ds : task.getResult().getChildren()) {
                                                    //totalcount[0]+=1;
                                                    if (ds.getValue(boolean.class).equals(true))
                                                        pcount[0]++;
                                                }
                                                //Toast.makeText(ViewAttendanceActivity.this, "pcount="+ pcount[0] +"total="+ totalcount[0], Toast.LENGTH_SHORT).show();
                                                if (totalcount[0] > 0) {
                                                    percent[0] = ((float) pcount[0] / totalcount[0]) * 100.0f;
                                                }
                                                ViewStudAtt viewStudAtt = new ViewStudAtt(roll, name, String.format("%.2f", percent[0]));
                                                Vstudents.add(viewStudAtt);
                                                Collections.sort(Vstudents, new ViewStudAttComparator());
                                                adViewRecview = new AdViewRecview(Vstudents);
                                                Viewrecview.setAdapter(adViewRecview);
                                            }
                                        } else {
                                            Toast.makeText(ViewAttendanceActivity.this, "attendance not fetch", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
//
                        } else {
                            Toast.makeText(ViewAttendanceActivity.this, "data not exists", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(ViewAttendanceActivity.this, "data not fetch", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewStudAttComparator implements Comparator<ViewStudAtt> {
        @Override
        public int compare(ViewStudAtt o1, ViewStudAtt o2) {
            // Compare by roll number in ascending order
            return o1.getRoll().compareTo(o2.getRoll());
        }
    }
}
