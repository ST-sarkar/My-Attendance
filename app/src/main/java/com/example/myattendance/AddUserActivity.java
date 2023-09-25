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

public class AddUserActivity extends AppCompatActivity {
    TextView branch,year,sem;
    EditText email,pass;
    Button add;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String sdepartment,syear,ssem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        branch=findViewById(R.id.tx_branch);
        year=findViewById(R.id.tx_year);
        sem=findViewById(R.id.tx_sem);
        email=findViewById(R.id.ed_email);
        pass=findViewById(R.id.ed_pass);
        add=findViewById(R.id.btn_addst);

        Intent intent=getIntent();
        sdepartment=intent.getStringExtra("dept");
        syear=intent.getStringExtra("year");
        ssem=intent.getStringExtra("sem");
        branch.setText(sdepartment);
        year.setText(syear);
        sem.setText(ssem);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("STUDENTS");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty())
                {
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    databaseReference.child(firebaseAuth.getUid()).child("DEPARTMENT").setValue(sdepartment);
                                    databaseReference.child(firebaseAuth.getUid()).child("YEAR").setValue(syear);
                                    databaseReference.child(firebaseAuth.getUid()).child("SEMESTER").setValue(ssem);
                                    databaseReference.child(firebaseAuth.getUid()).child("NAME").setValue("ABC");
                                    databaseReference.child(firebaseAuth.getUid()).child("ROLL").setValue("00");
                                    databaseReference.child(firebaseAuth.getUid()).child("EMAIL").setValue(email.getText().toString());
                                    databaseReference.child(firebaseAuth.getUid()).child("PASS").setValue(pass.getText().toString());

                                    Toast.makeText(AddUserActivity.this, "Student added successfully", Toast.LENGTH_SHORT).show();

                                    email.setText("");
                                    pass.setText("");
                                    //Intent intent=new Intent(AddUserActivity.this,AddUserActivity.class);
                                    //startActivity(intent);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddUserActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();

                                }
                            });
                }else{
                    Toast.makeText(AddUserActivity.this, "email and password is essential", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}