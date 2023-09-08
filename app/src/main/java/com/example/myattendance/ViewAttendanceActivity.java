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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("attendance");
        String path = uid + "/" + dept + "/" + year + "/" + sem + "/" + sub;
        try {

            reference.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String suid = snapshot.getKey().toString();
                            String roll = snapshot.child("rollno").getValue(String.class);
                            String name = snapshot.child("name").getValue(String.class);
                            int[] pcount = {0};
                            int[] totalcount = {0};
                            float[] percent = {0.0f};

                            reference.child(path).child(suid).child("Attendance Dates").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                                            ViewStudAtt viewStudAtt = new ViewStudAtt(roll, name, String.valueOf(percent[0]));
                                            Vstudents.add(viewStudAtt);
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
                    }else{
                        Toast.makeText(ViewAttendanceActivity.this, "data not exists", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "error" + e, Toast.LENGTH_SHORT).show();
        }
    }
}

        /*
        reference.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String roll=dataSnapshot.child("rollno").getValue(String.class);
                    String name=dataSnapshot.child("name").getValue(String.class);
                    long percent=0;
                    long prcount=0;
                    long totalcount=0;
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren() ){

                        if (dataSnapshot1.getKey().equals("Attendance Dates")) {
                            totalcount = dataSnapshot1.getChildrenCount();

                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                if (dataSnapshot2.getValue(boolean.class).equals(true)) {
                                    prcount = prcount + 1;
                                    //Toast.makeText(ViewAttendanceActivity.this, "" + prcount, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                    percent=(prcount/totalcount)*100;
                    ViewStudAtt viewStudAtt=new ViewStudAtt(roll,name,String.valueOf(percent));
                    Vstudents.add(viewStudAtt);

                }
                if(!Vstudents.isEmpty()) {
                    adViewRecview = new AdViewRecview(Vstudents);
                    Viewrecview.setAdapter(adViewRecview);
                }else {
                    Toast.makeText(ViewAttendanceActivity.this, "list is empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
/*
    private String getRollName(String suid) {
        //List<String> grollname=new ArrayList<>();
        final String[] groll = new String[1];
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("students");
        reference1.child(suid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

               /* grollname.add(snapshot.child("roll").getValue(String.class));
                grollname.add(snapshot.child("name").getValue(String.class));
                Toast.makeText(ViewAttendanceActivity.this, ""+grollname.get(0), Toast.LENGTH_SHORT).show();
                groll[0] =snapshot.child("roll").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return groll[0];
        //return grollname;
    }
*/
