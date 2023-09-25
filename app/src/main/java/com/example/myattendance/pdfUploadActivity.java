package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myattendance.modul.fileInfo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class pdfUploadActivity extends AppCompatActivity {
    EditText filename;
    TextView txbrw;
    ImageButton upload,cancle;
    ImageView browse,pdf;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_upload);

        filename=findViewById(R.id.ed_filename);
        upload=findViewById(R.id.btn_upload);
        browse=findViewById(R.id.img_browse);
        pdf=findViewById(R.id.img_pdf);
        cancle=findViewById(R.id.btn_cancle);
        txbrw=findViewById(R.id.tx_browse);

        pdf.setVisibility(View.INVISIBLE);
        cancle.setVisibility(View.INVISIBLE);


        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancle.setVisibility(View.INVISIBLE);
                pdf.setVisibility(View.INVISIBLE);
                browse.setVisibility(View.VISIBLE);
                txbrw.setVisibility(View.VISIBLE);

                filename.setText("");
            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"select file"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname!=null) {
                    uploadproccess(fname);
                }else{
                    Toast.makeText(pdfUploadActivity.this, "Choose File", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void uploadproccess(Uri fname) {
        Intent intent=getIntent();
        ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("Uploading File");
        pd.show();
        databaseReference= FirebaseDatabase.getInstance().getReference("NOTES");
        storageReference= FirebaseStorage.getInstance().getReference();
        if(!filename.getText().toString().isEmpty()) {
            StorageReference reference = storageReference.child("UploadedNotes/" + System.currentTimeMillis() + ".pdf");
            reference.putFile(fname)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    fileInfo obj = new fileInfo(filename.getText().toString(), uri.toString());
                                    databaseReference.child(intent.getStringExtra("dept")).child(intent.getStringExtra("year"))
                                            .child(intent.getStringExtra("semester")).child(databaseReference.push().getKey()).setValue(obj);
                                    pd.dismiss();
                                    Toast.makeText(pdfUploadActivity.this, "file successfully uploaded", Toast.LENGTH_SHORT).show();
                                    browse.setVisibility(View.VISIBLE);
                                    txbrw.setVisibility(View.VISIBLE);
                                    pdf.setVisibility(View.INVISIBLE);
                                    cancle.setVisibility(View.INVISIBLE);
                                    filename.setText("");


                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            Float percent=(100.0f*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                            pd.setMessage("Uploaded------"+percent+"%");
                        }
                    });
        }else {
            Toast.makeText(this, "please give file name!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK){
            fname=data.getData();
            browse.setVisibility(View.INVISIBLE);
            txbrw.setVisibility(View.INVISIBLE);
            pdf.setVisibility(View.VISIBLE);
            cancle.setVisibility(View.VISIBLE);
        }
    }
}