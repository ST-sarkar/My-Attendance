package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    CardView CDAU,CDAS,CDRU,CDVU;
    ImageButton btn_logout;
    Button update;
    EditText email;
    Boolean matchFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CDAU=findViewById(R.id.cdAU);
        CDAS=findViewById(R.id.cdAS);
        CDVU=findViewById(R.id.cdVU);
        CDRU=findViewById(R.id.cdRU);
        btn_logout=findViewById(R.id.btn_logout);
        update=findViewById(R.id.btn_upd_techer);
        email=findViewById(R.id.tx_email_tech);


        String uid=getIntent().getStringExtra("uid");

        CDAU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from="addUser";
                showOptionDialog(from);
            }
        });
        CDAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddSubActivity.class);
                startActivity(intent);
            }
        });

        CDVU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from="viewUser";
                showOptionDialog(from);
            }
        });

        CDRU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from="removeUser";
                Intent intent=new Intent(MainActivity.this,StudDataActivity.class);
                intent.putExtra("from",from);
                startActivity(intent);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();
                if (!em.isEmpty()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TEACHERS");

                    // Initialize a flag to keep track of whether a match was found
                    final boolean[] matchFound = {false};

                    databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                        String emailFromDatabase = dataSnapshot.child("EMAIL").getValue(String.class);

                                        // Check if the email from the database matches the input email
                                        if (emailFromDatabase != null && emailFromDatabase.equalsIgnoreCase(em)) {
                                            Intent intent = new Intent(MainActivity.this, TeachDataActivity.class);
                                            intent.putExtra("uid", dataSnapshot.getKey()); // Use getKey() here
                                            intent.putExtra("from", "updateTeacher");
                                            startActivity(intent);

                                            // Set the flag to true since a match was found
                                            matchFound[0] = true;
                                            break; // Exit the loop since a match was found
                                        }
                                    }
                                }

                                // Check if no match was found and display a message
                                if (!matchFound[0]) {
                                    Toast.makeText(MainActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Enter an email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showOptionDialog(String from) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select User")
                .setItems(R.array.user_selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //code for after selection
                        switch (which)
                        {
                            case 0:Intent intent=new Intent(MainActivity.this,StudDataActivity.class);
                                   intent.putExtra("from",from);
                                   startActivity(intent);
                                   break;
                            case 1:Intent intent1=new Intent(MainActivity.this,TeachDataActivity.class);
                                   intent1.putExtra("from",from);
                                   startActivity(intent1);
                                   break;
                        }

                    }
                })
                .setNegativeButton("Cancel",null);

        AlertDialog dialog= builder.create();
        dialog.show();

    }
}