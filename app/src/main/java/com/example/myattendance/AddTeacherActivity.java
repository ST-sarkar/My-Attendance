package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddTeacherActivity extends AppCompatActivity {

    EditText email,pass,name;
    Button add;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        email=findViewById(R.id.ed_email);
        pass=findViewById(R.id.ed_pass);
        name=findViewById(R.id.ed_name);
        add=findViewById(R.id.btn_addtech);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("TEACHERS");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty() && !name.getText().toString().isEmpty())
                {
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    Intent intent=getIntent();
                                    ArrayList<String> intentlist=intent.getStringArrayListExtra("list");
                                    databaseReference.child(firebaseAuth.getUid()).child("NAME").setValue(name.getText().toString());
                                    databaseReference.child(firebaseAuth.getUid()).child("EMAIL").setValue(email.getText().toString());
                                    databaseReference.child(firebaseAuth.getUid()).child("PASS").setValue(pass.getText().toString());

                                    for(int i=0;i<intentlist.size();i=i+4)
                                    {
                                            databaseReference.child(firebaseAuth.getUid()).child(intentlist.get(i).toUpperCase()).child(intentlist.get(i+1).toUpperCase()).child(intentlist.get(i+2).toUpperCase()).setValue(intentlist.get(i+3).toUpperCase());

                                    }
                                    name.setText("");
                                    email.setText("");
                                    pass.setText("");
                                    Toast.makeText(AddTeacherActivity.this, "Teacher added successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddTeacherActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();

                                }
                            });
                }else{
                    Toast.makeText(AddTeacherActivity.this, "email and password is essential", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}