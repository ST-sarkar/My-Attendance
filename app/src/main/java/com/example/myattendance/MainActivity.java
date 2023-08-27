package com.example.myattendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    CardView CDAU,CDAS,CDRU,CDVU;
    ImageButton btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CDAU=findViewById(R.id.cdAU);
        CDAS=findViewById(R.id.cdAS);
        CDVU=findViewById(R.id.cdVU);
        CDRU=findViewById(R.id.cdRU);
        btn_logout=findViewById(R.id.btn_logout);

        CDAU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionDialog();
            }
        });
        CDAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AddSubActivity.class);
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

    }

    private void showOptionDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select User")
                .setItems(R.array.user_selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    //code for after selection
                        switch (which)
                        {
                            case 0:Intent intent=new Intent(MainActivity.this,StudDataActivity.class);
                                   startActivity(intent);
                                   break;
                            case 1:Intent intent1=new Intent(MainActivity.this,TeachDataActivity.class);
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