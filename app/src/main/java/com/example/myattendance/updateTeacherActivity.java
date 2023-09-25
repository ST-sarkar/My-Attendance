package com.example.myattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class updateTeacherActivity extends AppCompatActivity {
    TextView updatetx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        updatetx=findViewById(R.id.tx_updinfo);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("TEACHERS");

        Intent intent=getIntent();
        ArrayList<String> intentlist=intent.getStringArrayListExtra("List");

        if(!intentlist.isEmpty()) {
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("Successfully updated teacher data :\n");
            for (int i = 0; i < intentlist.size(); i = i + 4) {
                databaseReference.child(intent.getStringExtra("uid")).child(intentlist.get(i).toUpperCase()).child(intentlist.get(i + 1).toUpperCase()).child(intentlist.get(i + 2).toUpperCase()).setValue(intentlist.get(i + 3).toUpperCase());
                stringBuilder.append(intent.getStringExtra("uid")+"->"+intentlist.get(i)+"->"+intentlist.get(i+1)+"->"+intentlist.get(i+2)+"->"+intentlist.get(i+3)+"\n");
            }

            updatetx.setText(stringBuilder);
        }else {
            Toast.makeText(this, "data not updated", Toast.LENGTH_SHORT).show();
        }


    }
}