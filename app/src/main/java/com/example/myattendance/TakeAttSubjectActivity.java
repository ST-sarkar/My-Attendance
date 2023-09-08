package com.example.myattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class TakeAttSubjectActivity extends AppCompatActivity {
    Spinner subject;
    List<String> listsub=new ArrayList<>();
    List<String> list=new ArrayList<>();
    ArrayAdapter<String> adapter;
    String from;
    Button next;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_att_subject);

        subject=findViewById(R.id.spinner_subject);
        next=findViewById(R.id.btn_next);
        list=getIntent().getStringArrayListExtra("list");
        from=getIntent().getStringExtra("from");
        for(int i=3;i<list.size();i=i+4)
        {
            listsub.add(list.get(i));
        }

        adapter=new ArrayAdapter<>(TakeAttSubjectActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,listsub);
        subject.setAdapter(adapter);

        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(from.equals("takeatt")) {
                        flag=0;
                        Intent intent=new Intent(TakeAttSubjectActivity.this,AttendanceActivity.class);
                        intent.putExtra("dept",list.get(4*position));
                        intent.putExtra("year",list.get(4*position+1));
                        intent.putExtra("semester",list.get(4*position+2));
                        intent.putExtra("subject",list.get(4*position+3));
                        intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

                }else if(from.equals("seeatt")) {
                    flag=1;
                    Intent intent=new Intent(TakeAttSubjectActivity.this,ViewAttendanceActivity.class);
                    intent.putExtra("dept",list.get(4*position));
                    intent.putExtra("year",list.get(4*position+1));
                    intent.putExtra("semester",list.get(4*position+2));
                    intent.putExtra("subject",list.get(4*position+3));
                    intent.putExtra("uid",getIntent().getStringExtra("uid"));
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });

                }else if(from.equals("sendmess")){
                    Intent intent=new Intent(TakeAttSubjectActivity.this,sendMessActivity.class);
                    intent.putExtra("dept",list.get(4*position));
                    intent.putExtra("year",list.get(4*position+1));
                    intent.putExtra("semester",list.get(4*position+2));
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });
                }else {
                    Intent intent=new Intent(TakeAttSubjectActivity.this,pdfUploadActivity.class);
                    intent.putExtra("dept",list.get(4*position));
                    intent.putExtra("year",list.get(4*position+1));
                    intent.putExtra("semester",list.get(4*position+2));
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }
}