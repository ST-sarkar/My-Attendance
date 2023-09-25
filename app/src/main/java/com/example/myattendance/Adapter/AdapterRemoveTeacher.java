package com.example.myattendance.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myattendance.R;
import com.example.myattendance.modul.removeTechModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.zip.Inflater;

public class AdapterRemoveTeacher extends RecyclerView.Adapter<AdapterRemoveTeacher.Teacherviewholder>{
    ArrayList<removeTechModel> teachlist;
    String dept,year,sem;

    public AdapterRemoveTeacher(ArrayList<removeTechModel> teachlist, String dept, String year, String sem) {
        this.teachlist = teachlist;
        this.dept = dept;
        this.year = year;
        this.sem = sem;
    }

    @NonNull
    @Override
    public AdapterRemoveTeacher.Teacherviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.removtechrow,parent,false);
        return new Teacherviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRemoveTeacher.Teacherviewholder holder, int position) {
        removeTechModel data=teachlist.get(position);
        holder.name.setText(data.getName());
        //Toast.makeText(holder.itemView.getContext(), "name"+data.getName(), Toast.LENGTH_SHORT).show();
        holder.subject.setText(data.getSubject());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Are you sure to delete");
                builder.setMessage("This will delete Whole data of teacher.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==DialogInterface.BUTTON_POSITIVE){

                        removeTechModel tdata=teachlist.get(holder.getAdapterPosition());

                            FirebaseDatabase.getInstance().getReference("ATTENDANCE").child(tdata.getTuid()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(holder.itemView.getContext(), "All attendance removed", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.itemView.getContext(), "error"+e, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            FirebaseDatabase.getInstance().getReference("TEACHER").child(tdata.getTuid()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(holder.itemView.getContext(), "Teacher removed", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.itemView.getContext(), "error"+e, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        dialog.dismiss();
                        teachlist.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();

                    }}
                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog= builder.create();
                alertDialog.show();
            }
        });
        holder.subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Are you sure to delete");
                builder.setMessage("This will remove only subject of teacher");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] databasebranch={"ATTENDANCE","TEACHERS"};
                        for (String i:databasebranch) {
                            FirebaseDatabase.getInstance().getReference(i).child(teachlist.get(holder.getAdapterPosition()).getTuid())
                                    .child(dept).child(year).child(sem).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(holder.itemView.getContext(), "Subject removed successufully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(holder.itemView.getContext(), "error" + e, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        dialog.dismiss();
                        teachlist.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog= builder.create();
                alertDialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return teachlist.size();
    }

    public class Teacherviewholder extends RecyclerView.ViewHolder {
        TextView name,subject;
        public Teacherviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tx_rmtname);
            subject=itemView.findViewById(R.id.tx_subject);
        }
    }
}
