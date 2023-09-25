package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myattendance.Adapter.AdapterRecView;
import com.example.myattendance.modul.StudAttInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StudAttViewActivity extends AppCompatActivity {
    String dept, year, sem, sub, uid, tuid;
    Toolbar toolbar;
    Button btn_checkatt;
    TextView txpresent, txtotal, txpercent, isdefualter;
    ArrayList<String> datelist=new ArrayList<>();
    ArrayList<String> presentlist=new ArrayList<>();
    //Map<String,String > attdata=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_att_view);

        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        txpresent = findViewById(R.id.tx_present);
        txpercent = findViewById(R.id.tx_percent);
        txtotal = findViewById(R.id.tx_total);
        isdefualter = findViewById(R.id.tx_isdefualt);
        btn_checkatt=findViewById(R.id.btn_checkatt);

        dept = getIntent().getStringExtra("department");
        year = getIntent().getStringExtra("year");
        sem = getIntent().getStringExtra("semester");
        sub = getIntent().getStringExtra("subject");
        uid = getIntent().getStringExtra("suid");


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TEACHERS");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot teacherSnapshot = task.getResult();
                    if (teacherSnapshot.exists()) {

                        for (DataSnapshot dataSnapshot : teacherSnapshot.getChildren()) {
                            DataSnapshot semSnapshot=dataSnapshot.child(dept.toUpperCase()).child(year.toUpperCase()).child(sem.toUpperCase());
                            String str = semSnapshot.getValue(String.class);
                            //Toast.makeText(StudAttViewActivity.this, "datasnapshot1 :" + str+sub, Toast.LENGTH_SHORT).show();
                            if (str!=null && str.equalsIgnoreCase(sub)) {
                                tuid = dataSnapshot.getKey();
                                //Toast.makeText(StudAttViewActivity.this, "inside to get tuid:"+tuid, Toast.LENGTH_SHORT).show();

                                float[] percent = {0.0f};
                                int[] prcount = {0};
                                int[] totalcount = {0};
                                //Toast.makeText(StudAttViewActivity.this, "tuid3:"+tuid, Toast.LENGTH_SHORT).show();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ATTENDANCE");
                                String path = tuid + "/" + dept.toUpperCase() + "/" + year.toUpperCase() + "/" + sem.toUpperCase() + "/" + sub.toUpperCase() + "/" + uid;
                                reference.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        //Toast.makeText(StudAttViewActivity.this, "oncomplete", Toast.LENGTH_SHORT).show();
                                        if(task.isSuccessful()) {
                                            if(task.getResult().exists()) {

                                                DataSnapshot snapshot = task.getResult();

                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    //Toast.makeText(StudAttViewActivity.this, "in snapshot", Toast.LENGTH_SHORT).show();

                                                    if (dataSnapshot.getKey().equals("ATTENDANCE DATES")) {
                                                        totalcount[0] = (int) dataSnapshot.getChildrenCount();
                                                        //Toast.makeText(StudAttViewActivity.this, "inside attendance dates", Toast.LENGTH_SHORT).show();

                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                            datelist.add(dataSnapshot2.getKey());
                                                            presentlist.add(String.valueOf(dataSnapshot2.getValue(boolean.class)));

                                                            //attdata.put(dataSnapshot2.getKey(), String.valueOf(dataSnapshot2.getValue(boolean.class)));

                                                            if (dataSnapshot2.getValue(boolean.class).equals(true)) {
                                                                prcount[0]++;
                                                            }

                                                        }
                                                        if (totalcount[0] > 0) {
                                                            Toast.makeText(StudAttViewActivity.this, "percent:" + percent[0], Toast.LENGTH_SHORT).show();
                                                            percent[0] = ((float) prcount[0] / totalcount[0]) * 100.0f;
                                                        }
                                                        txtotal.setText(String.valueOf(totalcount[0]));
                                                        txpresent.setText(String.valueOf(prcount[0]));
                                                        txpercent.setText(String.format("%.2f", percent[0]) + "%");

                                                        if (percent[0] < 75.0f) {
                                                            isdefualter.setText("You are in Defualter list");
                                                            isdefualter.setTextColor(Color.RED);
                                                        } else{
                                                            isdefualter.setText("You are not in Defualter list");
                                                            isdefualter.setTextColor(Color.GREEN);
                                                        }
                                                        btn_checkatt.setClickable(true);
                                                    }
                                                }
                                            }else {
                                                Toast.makeText(StudAttViewActivity.this, "attendance data not present", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        isdefualter.setVisibility(View.INVISIBLE);
                                        btn_checkatt.setClickable(false);
                                        Toast.makeText(StudAttViewActivity.this, "Student has no attendance data", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                break;
                            }

                        }
                    } else {
                        Toast.makeText(StudAttViewActivity.this, "task not exist", Toast.LENGTH_SHORT).show();
                    }
            }else {
                    Toast.makeText(StudAttViewActivity.this, "task unssuccesful", Toast.LENGTH_SHORT).show();
                }
                }
            });

        btn_checkatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StudAttViewActivity.this,stDetailAttActivity.class);
                intent.putStringArrayListExtra("datelist",datelist);
                intent.putStringArrayListExtra("presentlist",presentlist);
                startActivity(intent);
            }
        });

    }
/*
    private void getDetails() {
        float[] percent = {0};
        int[] prcount = {0};
        int[] totalcount = {0};
        Toast.makeText(this, "tuid3:"+tuid, Toast.LENGTH_SHORT).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("attendance");
        String path = tuid + "/" + dept + "/" + year + "/" + sem + "/" + sub.toLowerCase() + "/" + uid;
        reference.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Toast.makeText(StudAttViewActivity.this, "oncomplete", Toast.LENGTH_SHORT).show();

                DataSnapshot snapshot = task.getResult();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //Toast.makeText(StudAttViewActivity.this, "in snapshot", Toast.LENGTH_SHORT).show();

                    if (dataSnapshot.getKey().equals("Attendance Dates")) {
                        totalcount[0] = (int) dataSnapshot.getChildrenCount();
                        //Toast.makeText(StudAttViewActivity.this, "inside attendance dates", Toast.LENGTH_SHORT).show();

                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            if ((Integer.parseInt(dataSnapshot2.getKey()) >= Integer.parseInt(fromdate)) && (Integer.parseInt(dataSnapshot2.getKey()) <= Integer.parseInt(todate))) {
                                attdata.put(dataSnapshot2.getKey(), String.valueOf(dataSnapshot2.getValue(boolean.class)));
                            }
                            if (dataSnapshot2.getValue(boolean.class).equals(true)) {
                                prcount[0]++;
                            }

                        }
                        percent[0] = (float) (prcount[0] / totalcount[0]) * 100.0f;
                        txtotal.setText(String.valueOf(totalcount[0]));
                        txpresent.setText(String.valueOf(prcount[0]));
                        txpercent.setText(String.valueOf(percent[0]));
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudAttViewActivity.this, "error:" + e, Toast.LENGTH_SHORT).show();
            }
        });


        if (percent[0] < 75) {
            isdefualter.setText("You are in Defualter list");
            isdefualter.setTextColor(Color.RED);
        } else {
            isdefualter.setText("You are not in Defualter list");
            isdefualter.setTextColor(Color.GREEN);

        }



    }
*/

}