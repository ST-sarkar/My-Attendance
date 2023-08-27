package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText email,pass;
    Button btn_Login;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.ed_email);
        pass=findViewById(R.id.ed_pass);
        btn_Login=findViewById(R.id.btn_login);
        loadingbar = new ProgressDialog(this);


        mAuth= FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em=email.getText().toString();
                String ps=pass.getText().toString();

                if(!em.isEmpty() && !ps.isEmpty()) {
                    loadingbar.setMessage("Logging in...");
                    loadingbar.show();

                    mAuth.signInWithEmailAndPassword(em,ps)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    List<String> user=new ArrayList<>();
                                    user.add(0,"admin");
                                    user.add(1,"Teachers");
                                    user.add(2,"students");
                                    for (int i=0;i<user.size();i++){
                                        databaseReference=firebaseDatabase.getReference().child(user.get(i));

                                        int finalI = i;
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){

                                                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){

                                                        if(dataSnapshot.getKey().equals(mAuth.getUid())){
                                                            if(finalI ==0) {
                                                                loadingbar.dismiss();
                                                                Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                                                                intent2.putExtra("uid",mAuth.getUid());
                                                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                                startActivity(intent2);
                                                                finish();
                                                            } else  if (finalI==1){
                                                            loadingbar.dismiss();
                                                            Intent intent1 = new Intent(LoginActivity.this, TeacherActivity.class);
                                                            intent1.putExtra("uid",mAuth.getUid());
                                                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                            startActivity(intent1);
                                                            finish();
                                                            }else {
                                                            loadingbar.dismiss();
                                                            Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                                                            intent.putExtra("uid",mAuth.getUid());
                                                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                    }
                                                }else {
                                                    Toast.makeText(LoginActivity.this, "User data not found in database", Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "error: "+e, Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                }
                            });


                }
            }
        });
    }


}