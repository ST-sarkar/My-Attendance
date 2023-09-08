package com.example.myattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class stDetailAttActivity extends AppCompatActivity {
    EditText from,to;
    Toolbar toolbar;
    Button btn_seeatt;
    TextView displayatt;
    ScrollView scrollView;
    String todate,fromdate;
    ArrayList<String> datelist=new ArrayList<>();
    ArrayList<String> presentlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_detail_att);

        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        from=findViewById(R.id.ed_fromdate);
        to=findViewById(R.id.ed_todate);
        displayatt=findViewById(R.id.tx_attdisplay);
        btn_seeatt=findViewById(R.id.btn_att);
        scrollView=findViewById(R.id.scrollView);

        Intent intent=getIntent();
        datelist=intent.getStringArrayListExtra("datelist");
        presentlist=intent.getStringArrayListExtra("presentlist");

        btn_seeatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todate=to.getText().toString();
                fromdate=from.getText().toString();
                if(!todate.isEmpty()&&!fromdate.isEmpty()) {
                    Toast.makeText(stDetailAttActivity.this, "inside not empty", Toast.LENGTH_SHORT).show();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Dates\t\t\t\t\t\t\t\t\t\t\t\t\t\tAttendance").append(System.getProperty("line.separator"));
                    for (int i=0;i<datelist.size();i++) {
                        String str=datelist.get(i);
                        Toast.makeText(stDetailAttActivity.this, "inside for loop", Toast.LENGTH_SHORT).show();
                        if (fromdate.compareTo(str)<=0 && todate.compareTo(str)>=0) {
                            str=str.substring(6,8)+"/"+str.substring(4,6)+"/"+str.substring(0,4);
                            if (presentlist.get(i).equals("true")) {
                                stringBuilder.append(str).append("\t\t\t\t\t\t\t\t\tPresent").append(System.getProperty("line.separator"));
                            } else {
                                stringBuilder.append(str).append("   Absent").append(System.getProperty("line.separator"));
                            }
                            Toast.makeText(stDetailAttActivity.this, "inside from date", Toast.LENGTH_SHORT).show();
                        }
                        displayatt.setText(stringBuilder.toString());
                    }


                }
            }
        });


    }
}