package com.example.myattendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StudDataActivity extends AppCompatActivity {
    Spinner dept,year,sem;
    Button confirm;
    List<String> listdept=new ArrayList<>();
    List<String> listyear=new ArrayList<>();
    List<String> listsem=new ArrayList<>();
    ArrayAdapter<String> deptAdapter;
    ArrayAdapter<String> yearAdapter,semAdapter;
    String dp,yr,semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_data);

        dept=findViewById(R.id.spinner_dept);
        year=findViewById(R.id.spinner_year);
        sem=findViewById(R.id.spinner_sem);
        confirm=findViewById(R.id.btn_confirmdata);

        listdept.add("CSE");
        listdept.add("E&TC");
        listdept.add("CIVIL");
        listdept.add("MECH");
        listdept.add("EE");

        listyear.add("FY");
        listyear.add("SY");
        listyear.add("TY");
        listyear.add("BE");

        listsem.add("SEMESTER-1");
        listsem.add("SEMESTER-2");

        deptAdapter=new ArrayAdapter<>(StudDataActivity.this, android.R.layout.simple_spinner_dropdown_item,listdept);
        dept.setAdapter(deptAdapter);

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dp=parent.getSelectedItem().toString();
                //listdept.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(StudDataActivity.this, "Please select one of the departement", Toast.LENGTH_SHORT).show();
            }
        });

        yearAdapter=new ArrayAdapter<>(StudDataActivity.this, android.R.layout.simple_spinner_dropdown_item,listyear);
        year.setAdapter(yearAdapter);

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yr=parent.getSelectedItem().toString();
                //listyear.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(StudDataActivity.this, "Please select one of the year", Toast.LENGTH_SHORT).show();
            }
        });

        semAdapter=new ArrayAdapter<>(StudDataActivity.this, android.R.layout.simple_spinner_dropdown_item,listsem);
        sem.setAdapter(semAdapter);

        sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester=parent.getSelectedItem().toString();
                //listyear.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(StudDataActivity.this, "Please select one of the semester", Toast.LENGTH_SHORT).show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from=getIntent().getStringExtra("from");
                if(from.equals("addUser")) {
                    Intent intent = new Intent(StudDataActivity.this, AddUserActivity.class);
                    intent.putExtra("dept", dp);
                    intent.putExtra("year", yr);
                    intent.putExtra("sem", semester);
                    startActivity(intent);
                } else if (from.equals("removeUser")) {

                    whichUserOption();

                } else{
                    Intent intent = new Intent(StudDataActivity.this, ViewStudeActivity.class);
                    intent.putExtra("dept", dp);
                    intent.putExtra("year", yr);
                    intent.putExtra("sem", semester);
                    startActivity(intent);
                }
            }
        });
    }

    public void whichUserOption() {
        AlertDialog.Builder builder=new AlertDialog.Builder(StudDataActivity.this)
                .setTitle("select user")
                .setItems(R.array.user_selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:Intent intent = new Intent(StudDataActivity.this, RemoveUserActivity.class);
                                intent.putExtra("dept", dp);
                                intent.putExtra("year", yr);
                                intent.putExtra("sem", semester);
                                intent.putExtra("user","student");
                                //Toast.makeText(StudDataActivity.this, "studdataact"+dp+yr+semester, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                break;
                            case 1:Intent intent1 = new Intent(StudDataActivity.this, RemoveUserActivity.class);
                                intent1.putExtra("dept", dp);
                                intent1.putExtra("year", yr);
                                intent1.putExtra("sem", semester);
                                intent1.putExtra("user","teacher");
                                //Toast.makeText(StudDataActivity.this, "studdataact"+dp+yr+semester, Toast.LENGTH_SHORT).show();
                                startActivity(intent1);
                                break;
                        }
                    }
                });
                AlertDialog alertDialog=builder.create();
                    alertDialog.show();
    }
}