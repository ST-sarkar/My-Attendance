package com.example.myattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.myattendance.Adapter.pdfViewAdapter;
import com.example.myattendance.modul.fileInfo;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class PDFViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    pdfViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        recyclerView = findViewById(R.id.recvew_pdf);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Define the query to retrieve 'fileInfo' data from Firebase
        Query query = FirebaseDatabase.getInstance()
                .getReference("Notes")
                .child(getIntent().getStringExtra("dept"))
                .child(getIntent().getStringExtra("year"))
                .child(getIntent().getStringExtra("semester"));

        // Configure the options for the FirebaseRecyclerAdapter
        FirebaseRecyclerOptions<fileInfo> options =
                new FirebaseRecyclerOptions.Builder<fileInfo>()
                        .setQuery(query, fileInfo.class)
                        .build();

        // Initialize the adapter
        adapter = new pdfViewAdapter(options);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}