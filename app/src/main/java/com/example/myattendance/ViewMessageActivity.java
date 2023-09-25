package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewMessageActivity extends AppCompatActivity {
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        message=findViewById(R.id.tx_viewmess);

        Intent intent=getIntent();
        String year=intent.getStringExtra("year");
        String dept=intent.getStringExtra("dept");
        String sem=intent.getStringExtra("semester");

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("MESSAGES");
        if(!year.isEmpty() && !dept.isEmpty() && !sem.isEmpty()){
            databaseReference.child(dept).child(year).child(sem).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult().exists()){
                            String str=task.getResult().getValue(String.class);
                            message.setText(str);
                        }
                    }
                }
            });
        }
    }
}