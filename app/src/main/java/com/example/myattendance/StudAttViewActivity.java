package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

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
    String dept, year, sem, sub, uid, tuid,todate,fromdate;
    Toolbar toolbar;
    Button btn_seeatt;
    TextView txpresent, txtotal, txpercent, isdefualter,displayatt;
    EditText from,to;
    Map<String,String > attdata=new HashMap<>();

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
        from=findViewById(R.id.ed_fromdate);
        to=findViewById(R.id.ed_todate);
        displayatt=findViewById(R.id.tx_attdisplay);
        btn_seeatt=findViewById(R.id.btn_att);

        dept = getIntent().getStringExtra("department");
        year = getIntent().getStringExtra("year");
        sem = getIntent().getStringExtra("semester");
        sub = getIntent().getStringExtra("subject");
        uid = getIntent().getStringExtra("suid");
        Log.d("dept",dept);
        Log.d("year",year);
        Log.d("sem",sem);
        Log.d("sub",sub);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Teachers");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot teacherSnapshot = task.getResult();
                    if (teacherSnapshot.exists()) {

                        for (DataSnapshot dataSnapshot : teacherSnapshot.getChildren()) {
                            DataSnapshot semSnapshot=dataSnapshot.child(dept).child(year).child(sem);
                            String str = semSnapshot.getValue(String.class);
                            Toast.makeText(StudAttViewActivity.this, "datasnapshot1 :" + str+sub, Toast.LENGTH_SHORT).show();
                            if (str!=null && str.equalsIgnoreCase(sub)) {
                                tuid = dataSnapshot.getKey();
                                Toast.makeText(StudAttViewActivity.this, "inside to get tuid:"+tuid, Toast.LENGTH_SHORT).show();
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
        todate=to.getText().toString();
        fromdate=from.getText().toString();

        if(!todate.isEmpty()&&!fromdate.isEmpty()){
            btn_seeatt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Integer.parseInt(fromdate) < Integer.parseInt(todate)){
                        Toast.makeText(StudAttViewActivity.this, "tuid2:"+tuid, Toast.LENGTH_SHORT).show();
                        getDetails(fromdate,todate);
                    }
                }
            });

        }
    }

    private void getDetails(String fromdate, String todate) {
        final long[] percent = {0};
        final long[] prcount = {0};
        final long[] totalcount = {0};
        Toast.makeText(this, "tuid3:"+tuid, Toast.LENGTH_SHORT).show();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("attendance");
        String path = tuid + "/" + dept + "/" + year + "/" + sem + "/" + sub.toLowerCase() + "/" + uid;
        reference.child(path).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Toast.makeText(StudAttViewActivity.this, "oncomplete", Toast.LENGTH_SHORT).show();

                DataSnapshot snapshot = task.getResult();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Toast.makeText(StudAttViewActivity.this, "in snapshot", Toast.LENGTH_SHORT).show();

                    if (dataSnapshot.getKey().equals("Attendance Dates")) {
                        totalcount[0] = dataSnapshot.getChildrenCount();
                        Toast.makeText(StudAttViewActivity.this, "inside attendance dates", Toast.LENGTH_SHORT).show();

                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            if ((Integer.parseInt(dataSnapshot2.getKey()) >= Integer.parseInt(fromdate)) && (Integer.parseInt(dataSnapshot2.getKey()) <= Integer.parseInt(todate))) {
                                attdata.put(dataSnapshot2.getKey(), String.valueOf(dataSnapshot2.getValue(boolean.class)));
                            }
                            if (dataSnapshot2.getValue(boolean.class).equals(true)) {
                                prcount[0] += 1;
                            }

                        }
                        percent[0] = (prcount[0] / totalcount[0]) * 100;
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

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Dates\t\t\t\t\t Attendance").append(System.getProperty("line.separator"));
                for (String key : attdata.keySet()) {
                    if (attdata.get(key).equals("true")) {
                        stringBuilder.append(key).append("   Present").append(System.getProperty("line.separator"));
                    } else {
                        stringBuilder.append(key).append("   Absent").append(System.getProperty("line.separator"));
                    }
                }

                displayatt.setText(stringBuilder.toString());

    }


}