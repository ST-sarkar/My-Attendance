package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myattendance.modul.studView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class viewTechActivity extends AppCompatActivity {
    TextView tlist;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tech);

        tlist=findViewById(R.id.tx_teachview);
        String dept,year,sem,sub;
        dept=getIntent().getStringExtra("dept");
        year=getIntent().getStringExtra("year");
        sem=getIntent().getStringExtra("semester");

        databaseReference= FirebaseDatabase.getInstance().getReference("Teachers");
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Name of Teacher");

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        for(DataSnapshot dataSnapshot:task.getResult().getChildren()){
                            Toast.makeText(viewTechActivity.this, "inside for,dept:"+dept+year+sem, Toast.LENGTH_SHORT).show();
                            if(dataSnapshot.child(dept).child(year).child(sem).getKey().equals(sem)){
                                Toast.makeText(viewTechActivity.this, "inside if,dept:"+dept+year+sem, Toast.LENGTH_SHORT).show();
                                    stringBuilder.append(dataSnapshot.child("name").getValue(String.class)).append("\n");
                            }

                        }
                        tlist.setText(stringBuilder);
                    }else {
                        Toast.makeText(viewTechActivity.this, "Teacher data not exist", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(viewTechActivity.this, "Teacher data not fetched", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}