package com.example.myattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sendMessActivity extends AppCompatActivity {
    String year,dept,sem;
    EditText message;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mess);

        message=findViewById(R.id.ed_message);
        submit=findViewById(R.id.btn_submit);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Messages");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=message.getText().toString();
                Intent intent = getIntent();
                year = intent.getStringExtra("year");
                dept = intent.getStringExtra("dept");
                sem = intent.getStringExtra("semester");
                if(!str.isEmpty() && !year.isEmpty() && !dept.isEmpty() && !sem.isEmpty()) {
                    databaseReference.child(dept).child(year).child(sem).setValue(str);
                    message.setText("");
                }
            }
        });
    }
}