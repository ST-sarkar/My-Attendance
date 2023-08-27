package com.example.myattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddSubActivity extends AppCompatActivity {
    Spinner dept,year,sem;
    EditText sub;
    Button add;
    List<String> listdept=new ArrayList<>();
    List<String> listyear=new ArrayList<>();
    List<String> listsem=new ArrayList<>();
    ArrayAdapter<String> deptAdapter;
    ArrayAdapter<String> yearAdapter,semAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String dp,yr,semester,subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub);

        dept=findViewById(R.id.spinner_dept);
        year=findViewById(R.id.spinner_year);
        sem=findViewById(R.id.spinner_sem);
        sub=findViewById(R.id.ed_sub);
        add=findViewById(R.id.btn_addsub);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Subjects");

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

        deptAdapter=new ArrayAdapter<>(AddSubActivity.this, android.R.layout.simple_spinner_dropdown_item,listdept);
        dept.setAdapter(deptAdapter);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dp=parent.getSelectedItem().toString();
                //listdept.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddSubActivity.this, "Please select one of the departement", Toast.LENGTH_SHORT).show();
            }
        });

        yearAdapter=new ArrayAdapter<>(AddSubActivity.this, android.R.layout.simple_spinner_dropdown_item,listyear);
        year.setAdapter(yearAdapter);

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yr=parent.getSelectedItem().toString();
                //listyear.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddSubActivity.this, "Please select one of the year", Toast.LENGTH_SHORT).show();
            }
        });

        semAdapter=new ArrayAdapter<>(AddSubActivity.this, android.R.layout.simple_spinner_dropdown_item,listsem);
        sem.setAdapter(semAdapter);

        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester=parent.getSelectedItem().toString();
                //listyear.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddSubActivity.this, "Please select one of the semester", Toast.LENGTH_SHORT).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject=sub.getText().toString();
                if(!subject.isEmpty()){
                    databaseReference.child(dp).child(yr).child(semester).child(subject).setValue("");
                    Toast.makeText(AddSubActivity.this, subject+" Subject added successfully", Toast.LENGTH_SHORT).show();
                    sub.setText("");
                }
                else{
                    Toast.makeText(AddSubActivity.this, "Give subject name", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}