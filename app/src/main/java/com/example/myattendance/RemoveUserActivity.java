package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.myattendance.Adapter.AdapterRemoveTeacher;
import com.example.myattendance.Adapter.AdapterRemoveUser;
import com.example.myattendance.modul.removeTechModel;
import com.example.myattendance.modul.removeUserModel;
import com.example.myattendance.modul.studView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class RemoveUserActivity extends AppCompatActivity {
    RecyclerView removerecview;
    DatabaseReference databaseReference;
    ArrayList<removeUserModel> studlist = new ArrayList<>();
    ArrayList<removeTechModel> teacheruser = new ArrayList<>();
    HashMap<String, String> teacherlist = new HashMap<>();
    String[] studclass = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_user);

        removerecview = findViewById(R.id.recview_removestud);
        removerecview.setLayoutManager(new LinearLayoutManager(this));

        String dept, year, sem;
        dept = getIntent().getStringExtra("dept");
        year = getIntent().getStringExtra("year");
        sem = getIntent().getStringExtra("sem");

        if (getIntent().getStringExtra("user").equals("teacher")) {
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("TEACHERS");
            databaseReference1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                String tuid = dataSnapshot.getKey();
                                String sub = dataSnapshot.child(dept).child(year).child(sem).getValue(String.class);
                                String name = dataSnapshot.child("NAME").getValue(String.class);
                                String email = dataSnapshot.child("EMAIL").getValue(String.class);
                                String pass = dataSnapshot.child("PASS").getValue(String.class);
                                if (sub != null && sub.equalsIgnoreCase(sub)) {
                                    //Toast.makeText(RemoveUserActivity.this, "name" + sub, Toast.LENGTH_SHORT).show();
                                    removeTechModel removeModel = new removeTechModel(tuid, name, sub);
                                    teacheruser.add(removeModel);
                                }
                            }
                            if (!teacheruser.isEmpty()) {
                                AdapterRemoveTeacher adapterRemoveTeacher = new AdapterRemoveTeacher(teacheruser, dept, year, sem);
                                removerecview.setAdapter(adapterRemoveTeacher);
                                adapterRemoveTeacher.notifyDataSetChanged();
                            } else {
                                Toast.makeText(RemoveUserActivity.this, "teacher list is empty", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RemoveUserActivity.this, "Teacher data not present", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

        } else {
            databaseReference = FirebaseDatabase.getInstance().getReference("STUDENTS");

            databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                String uid = dataSnapshot.getKey();
                                studView st = dataSnapshot.getValue(studView.class);
                                if (st != null) {
                                    if (st.getDEPARTMENT().equals(dept) && st.getYEAR().equals(year) && st.getSEMESTER().equals(sem)) {
                                        removeUserModel data = new removeUserModel(st.getROLL(), st.getNAME(), uid, st.getEMAIL(), st.getPASS());
                                        studlist.add(data);
                                        studclass[0] = st.getDEPARTMENT();
                                        studclass[1] = st.getYEAR();
                                        studclass[2] = st.getSEMESTER();
                                    }
                                }
                            }

                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("TEACHERS");
                            databaseReference1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                                String tuid = dataSnapshot.getKey();
                                                String sub = dataSnapshot.child(dept).child(year).child(sem).getValue(String.class);
                                                if (sub != null && sub.equalsIgnoreCase(sem)) {
                                                    teacherlist.put(tuid, sub);
                                                }
                                            }
                                        }
                                        if (!studlist.isEmpty()) {
                                            AdapterRemoveUser adapterRemoveUser = new AdapterRemoveUser(studlist, teacherlist, studclass);
                                            removerecview.setAdapter(adapterRemoveUser);
                                            adapterRemoveUser.notifyDataSetChanged();
                                        } else {
                                            Toast.makeText(RemoveUserActivity.this, "student data not present", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RemoveUserActivity.this, "Teacher data not fetched successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RemoveUserActivity.this, "Student data not exist" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RemoveUserActivity.this, "Student data not fetched successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
