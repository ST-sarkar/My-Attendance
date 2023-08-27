package com.example.myattendance.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myattendance.R;
import com.example.myattendance.modul.StudAttInfo;
import com.example.myattendance.modul.ViewStudAtt;

import java.util.ArrayList;
import java.util.List;

public class AdViewRecview extends RecyclerView.Adapter<AdViewRecview.MyViewHolderview> {
    List<ViewStudAtt> Vstudents;
    public AdViewRecview(List<ViewStudAtt> students) {
        this.Vstudents=students;
    }

    @NonNull
    @Override
    public AdViewRecview.MyViewHolderview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View View= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewatt_row,parent,false);
        return new MyViewHolderview(View);
    }

    @Override
    public void onBindViewHolder(@NonNull AdViewRecview.MyViewHolderview holder, int position) {
        ViewStudAtt viewStudAtt=Vstudents.get(position);

        holder.txroll.setText(viewStudAtt.getRoll());
        holder.txname.setText(viewStudAtt.getName());
        holder.txpercent.setText(viewStudAtt.getPrecent());

    }

    @Override
    public int getItemCount() {
        return Vstudents.size();
    }

    public static class MyViewHolderview extends RecyclerView.ViewHolder{
        public TextView txroll,txname,txpercent;
        public MyViewHolderview(@NonNull View itemView) {
            super(itemView);
            txname=itemView.findViewById(R.id.tx_vname);
            txpercent=itemView.findViewById(R.id.tx_percent);
            txroll=itemView.findViewById(R.id.tx_vroll);
        }
    }
}
