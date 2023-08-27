package com.example.myattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TeachDataActivity extends AppCompatActivity {
    Spinner dept,year,sem;
    Button next,add;
    List<String> minlist=new ArrayList<>();
    List<String> listdept=new ArrayList<>();
    List<String> listyear=new ArrayList<>();
    List<String> listsem=new ArrayList<>();
    ArrayAdapter<String> deptAdapter;
    ArrayAdapter<String> yearAdapter,semAdapter;
    String dp,yr,semester,subject;
    EditText sub;
    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_data);

        dept=findViewById(R.id.spinner_dept);
        year=findViewById(R.id.spinner_year);
        sem=findViewById(R.id.spinner_sem);
        sub=findViewById(R.id.ed_sub);
        add=findViewById(R.id.btn_addtech);
        next=findViewById(R.id.btn_next);

        listdept.add("CSE");
        listdept.add("E&TC");
        listdept.add("CIVIL");
        listdept.add("MECH");
        listdept.add("EE");

        listyear.add("FY");
        listyear.add("SY");
        listyear.add("TY");
        listyear.add("BE");

        listsem.add("semester-1");
        listsem.add("semester-2");

        deptAdapter=new ArrayAdapter<>(TeachDataActivity.this, android.R.layout.simple_spinner_dropdown_item,listdept);
        dept.setAdapter(deptAdapter);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dp=parent.getSelectedItem().toString();
                //listdept.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(TeachDataActivity.this, "Please select one of the departement", Toast.LENGTH_SHORT).show();
            }
        });

        yearAdapter=new ArrayAdapter<>(TeachDataActivity.this, android.R.layout.simple_spinner_dropdown_item,listyear);
        year.setAdapter(yearAdapter);

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yr=parent.getSelectedItem().toString();
                //listyear.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(TeachDataActivity.this, "Please select one of the year", Toast.LENGTH_SHORT).show();
            }
        });

        semAdapter=new ArrayAdapter<>(TeachDataActivity.this, android.R.layout.simple_spinner_dropdown_item,listsem);
        sem.setAdapter(semAdapter);

        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester=parent.getSelectedItem().toString();
                //listyear.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(TeachDataActivity.this, "Please select one of the semester", Toast.LENGTH_SHORT).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject=sub.getText().toString();
                if(!subject.isEmpty()){
                    minlist.add(dp);
                    minlist.add(yr);
                    minlist.add(semester);
                    minlist.add(subject);

                    Toast.makeText(TeachDataActivity.this,"information added successfully", Toast.LENGTH_SHORT).show();
                    sub.setText("");
                    flag=1;
                }
                else{
                    Toast.makeText(TeachDataActivity.this, "Give subject name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==1) {
                    Intent intent = new Intent(TeachDataActivity.this, AddTeacherActivity.class);
                    intent.putStringArrayListExtra("list", (ArrayList<String>) minlist);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(TeachDataActivity.this, "subject is essential", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}