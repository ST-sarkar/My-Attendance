package com.example.myattendance.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myattendance.R;
import com.example.myattendance.modul.removeUserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AdapterRemoveUser extends RecyclerView.Adapter<AdapterRemoveUser.MyviewHolder> {
    ArrayList<removeUserModel> studlist;
    HashMap<String,String> teacherList;
    String[] studclass;

    public AdapterRemoveUser(ArrayList<removeUserModel> studlist,HashMap<String,String> teacherList,String[] studclass) {
        this.studlist = studlist;
        this.teacherList=teacherList;
        this.studclass=studclass;
    }

    @NonNull
    @Override
    public AdapterRemoveUser.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRemoveUser.MyviewHolder holder, int position) {
        removeUserModel removeUserModel=studlist.get(position);
        holder.roll.setText(removeUserModel.getRoll());
        holder.name.setText(removeUserModel.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("Confirm")
                        .setMessage("You are sure to delete account");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==DialogInterface.BUTTON_POSITIVE)
                        {
                            FirebaseAuth auth=FirebaseAuth.getInstance();
                            auth.signInWithEmailAndPassword(studlist.get(holder.getAdapterPosition()).getEmail(), studlist.get(holder.getAdapterPosition()).getPass())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("STUDENTS");
                                            databaseReference.child(studlist.get(holder.getAdapterPosition()).getUid()).removeValue();
                                            DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("ATTENDANCE");
                                            try {
                                                for (String str : teacherList.keySet()) {

                                                    databaseReference1.child(str).child(studclass[0]).child(studclass[1]).child(studclass[2])
                                                            .child(Objects.requireNonNull(teacherList.get(str))).child(studlist.get(holder.getAdapterPosition()).getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(holder.itemView.getContext(), "successfuly deleted attendance", Toast.LENGTH_SHORT).show();
                                                                    }else {
                                                                        Toast.makeText(holder.itemView.getContext(), "attendance not deleted", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                    Toast.makeText(holder.itemView.getContext(), "inside delete attendance", Toast.LENGTH_SHORT).show();
                                                }
                                            }catch (Exception e){
                                                Toast.makeText(holder.itemView.getContext(), "Attendance deleting error", Toast.LENGTH_SHORT).show();
                                            }
                                            auth.getCurrentUser().delete();
                                            studlist.remove(holder.getAdapterPosition());
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                            Toast.makeText(holder.itemView.getContext(), "Student Successfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==DialogInterface.BUTTON_NEGATIVE)
                        {
                            dialog.dismiss();
                        }
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studlist.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView roll,name;
        CardView cd;
        public MyviewHolder(@NonNull View itemView) {
            super(itemView);
            roll=itemView.findViewById(R.id.tx_roll);
            name=itemView.findViewById(R.id.tx_name);
            cd=itemView.findViewById(R.id.cardv);
        }
    }
}
