package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

public class StudentActivity extends AppCompatActivity {
    ImageButton logout;
    Button update, seeatt;
    TextView dept, year, sem, name;
    Spinner sp_subject;
    ArrayAdapter<String> adaptersub;
    String uid, sdept, syear, ssem, sname, subject="";
    //Map<String, String> datalist = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        logout = findViewById(R.id.btn_logout);
        update = findViewById(R.id.update_info_button);
        seeatt = findViewById(R.id.Seeatt_Button);
        dept = findViewById(R.id.tx_dept);
        year = findViewById(R.id.tx_year);
        sem = findViewById(R.id.tx_sem);
        name = findViewById(R.id.tx_name);
        sp_subject = findViewById(R.id.st_spinnerSub);
        uid = getIntent().getStringExtra("uid");

        Intent intent = new Intent(StudentActivity.this, StudAttViewActivity.class);
        List<String> sublist = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students");

        reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        sname = String.valueOf(dataSnapshot.child("name").getValue());
                        sdept = String.valueOf(dataSnapshot.child("department").getValue());
                        ssem = String.valueOf(dataSnapshot.child("semester").getValue());
                        syear = String.valueOf(dataSnapshot.child("year").getValue());

                        dept.setText(sdept);
                        sem.setText(ssem);
                        year.setText(syear);
                        name.setText(sname);

                        //getsubjects(sdept,syear,ssem);
                        if (sdept != null && syear != null && ssem != null) {
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Subjects");
                            reference1.child(sdept).child(syear).child(ssem).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if(task.getResult().exists()){
                                        DataSnapshot dataSnapshot=task.getResult();
                                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                            sublist.add(dataSnapshot1.getKey());
                                        }
                                        if (!sublist.isEmpty()) {
                                            adaptersub = new ArrayAdapter<>(StudentActivity.this, android.R.layout.simple_spinner_item, sublist);
                                            adaptersub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                            sp_subject.setAdapter(adaptersub);
                                        }else {
                                            Toast.makeText(StudentActivity.this, "empty sublist", Toast.LENGTH_SHORT).show();
                                        }
                                        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                //Toast.makeText(StudentActivity.this, "under onitemselected", Toast.LENGTH_SHORT).show();

                                                    //sp_subject.setSelection(position);
                                                    subject = parent.getItemAtPosition(position).toString();
                                                    intent.putExtra("subject", subject);
                                                    //Toast.makeText(StudentActivity.this, "subject:"+subject, Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                                Toast.makeText(StudentActivity.this, "nothing selected", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        intent.putExtra("department",sdept);
                                        intent.putExtra("year",syear);
                                        intent.putExtra("semester",ssem);
                                        intent.putExtra("suid",uid);

                                    }else {
                                        Toast.makeText(StudentActivity.this, "Subject data not found ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(StudentActivity.this, "error:"+e, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Toast.makeText(StudentActivity.this, "sdept ,syaer,ssem are null in get subjects", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(StudentActivity.this, "student data not present", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StudentActivity.this, "error:"+e, Toast.LENGTH_SHORT).show();

            }
        });

        /*reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                       // if(dataSnapshot.getKey().equals(uid)) {
                            //Toast.makeText(StudentActivity.this, "inside loop"+uid, Toast.LENGTH_SHORT).show();
                            Log.d("inside loop",uid);
                            DataSnapshot dsdept = dataSnapshot.child("department");
                            sdept=dsdept.getValue(String.class);
                            Log.d("sdept",sdept);
                            DataSnapshot dssem = dataSnapshot.child("semester");
                            ssem=dssem.getValue(String.class);
                            Log.d("ssem",ssem);
                            DataSnapshot dsyear = dataSnapshot.child("year");
                            syear=dsyear.getValue(String.class);
                            Log.d("syear",syear);
                            DataSnapshot dsname = dataSnapshot.child("name");
                            sname=dsname.getValue(String.class);

                    dept.setText(sdept);
                    sem.setText(ssem);
                    year.setText(syear);
                    name.setText(sname);


                    intent.putExtra("subject",subject);
                    intent.putExtra("department",sdept);
                    intent.putExtra("year",syear);
                    intent.putExtra("semester",ssem);
                    intent.putExtra("suid",uid);

                            //Log.d("sname",sname);
                            datalist.put("sdept",sdept);
                            datalist.put("ssem",ssem);
                            datalist.put("syear",syear);
                            datalist.put("sname",sname);
                            //Log.d("sname",datalist.get("sname"));
                    getsubjects(datalist);

                    if (sublist!=null) {
                        adaptersub = new ArrayAdapter<>(StudentActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, sublist);
                        sp_subject.setAdapter(adaptersub);
                    }else {
                        Toast.makeText(StudentActivity.this, "empty sublist", Toast.LENGTH_SHORT).show();
                    }

                    sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            subject=sublist.get(position);
                            intent.putExtra("subject",subject);
                            Log.d("subject",subject);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                       // }
                    //}
                }else{
                    Toast.makeText(StudentActivity.this, "student data not found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(this, "studentActivity"+sdept+syear+ssem, Toast.LENGTH_SHORT).show();
        Log.d("inside loop",uid);
       // Log.d("sdept",datalist.get("sdept"));
       // Log.d("ssem",ssem);
       // Log.d("syear",syear);
       // Log.d("sname",sname);
*/

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, UpdateStudInfoActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(StudentActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //Log.d("subject",subject);
        seeatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });

    }
}