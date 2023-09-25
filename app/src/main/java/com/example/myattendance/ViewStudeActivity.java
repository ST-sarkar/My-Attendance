package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myattendance.modul.studView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewStudeActivity extends AppCompatActivity {
    TextView list;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stude);

        list=findViewById(R.id.tx_studlist);

        String dept,year,sem;
        dept=getIntent().getStringExtra("dept");
        year=getIntent().getStringExtra("year");
        sem=getIntent().getStringExtra("sem");

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Roll No").append("\t\t\t").append("Name of Students").append("\n");
        databaseReference= FirebaseDatabase.getInstance().getReference("STUDENTS");

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    DataSnapshot snapshot=task.getResult();
                    if(snapshot.exists()){
                        for(DataSnapshot dataSnapshot:task.getResult().getChildren()){
                            studView st=dataSnapshot.getValue(studView.class);
                            if (st!=null){
                                //Toast.makeText(ViewStudeActivity.this, "dept"+st.getDepartment()+st.getYear()+st.getSemester()+st.getRoll()+st.getName(), Toast.LENGTH_SHORT).show();
                                if (st.getDEPARTMENT().equals(dept) && st.getYEAR().equals(year) && st.getSEMESTER().equals(sem)) {
                                    stringBuilder.append(st.getROLL()).append("\t\t\t\t").append(st.getNAME()).append("\n");
                                }
                            }
                            list.setText(stringBuilder);
                        }
                    }else {
                        Toast.makeText(ViewStudeActivity.this, "Student data not exist"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ViewStudeActivity.this, "Student data not fetched successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}