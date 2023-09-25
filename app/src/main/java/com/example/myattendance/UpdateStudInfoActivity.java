package com.example.myattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateStudInfoActivity extends AppCompatActivity {
    EditText name,roll;
    Button update;
    String uid;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stud_info);

        name=findViewById(R.id.ed_name);
        roll=findViewById(R.id.ed_roll);
        update=findViewById(R.id.btn_update);
        uid=getIntent().getStringExtra("uid");
        toolbar=findViewById(R.id.mainToolbar);
        toolbar.setTitle("Update Info");
        setSupportActionBar(toolbar);


        String uid=getIntent().getStringExtra("uid");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("STUDENTS");
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && !roll.getText().toString().isEmpty() && roll.getText().toString().compareTo("000")>0 && roll.getText().toString().compareTo("999")<0) {
                    reference.child(uid).child("NAME").setValue(name.getText().toString());
                    String rl=roll.getText().toString();
                    int l=rl.length();
                    if(l==1) {
                        reference.child(uid).child("ROLL").setValue("0"+rl);
                    }else if(l>2){
                        reference.child(uid).child("ROLL").setValue(String.valueOf(rl.charAt(l-2)+rl.charAt(l-1)));
                    }else {
                        reference.child(uid).child("ROLL").setValue(rl);
                    }
                    Toast.makeText(UpdateStudInfoActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UpdateStudInfoActivity.this, "name and roll number are essential", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}