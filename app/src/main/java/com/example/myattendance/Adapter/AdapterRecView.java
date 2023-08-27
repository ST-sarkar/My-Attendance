package com.example.myattendance.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myattendance.R;
import com.example.myattendance.modul.StudAttInfo;
import com.example.myattendance.modul.StudAttendace;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdapterRecView extends RecyclerView.Adapter<AdapterRecView.MyViewHolder> {
    private List<StudAttInfo> studList;
    private Map<String,StudAttendace> markatt;
    private SimpleDateFormat dateFormat;
    int[] cl;

    public AdapterRecView(List<StudAttInfo> list,int[] cl) {
        this.studList=list;
        this.markatt=new HashMap<>();
        this.dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        this.cl=cl;
    }

    public Map<String,StudAttendace> getMarkAtt() {
        return markatt;
    }

    public void clearMarkAtt() {
         markatt.clear();
    }
    @NonNull
    @Override
    public AdapterRecView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View View=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
        return new MyViewHolder(View);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecView.MyViewHolder holder, int position) {
        StudAttInfo stinfo=studList.get(position);
        holder.name.setText(stinfo.getSname());
        holder.roll.setText(stinfo.getSroll());
        holder.checkBox.setChecked(stinfo.isPresent());
        if (stinfo.isPresent()) {
            holder.cd.setCardBackgroundColor(cl[1]); // Present color
        } else {
            holder.cd.setCardBackgroundColor(cl[0]); // Absent color
        }

        String date=dateFormat.format(new Date());
        String uid=stinfo.getSuid();
        String name=stinfo.getSname();
        String roll=stinfo.getSroll();
        boolean present=stinfo.isPresent();
        markatt.put(uid,new StudAttendace(date,present,roll,name));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adposition=holder.getAdapterPosition();
                if(adposition!=RecyclerView.NO_POSITION) {
                    StudAttInfo  studAttInfo=studList.get(adposition);
                    boolean present =!studAttInfo.isPresent();
                    studAttInfo.setPresent(present);
                    String date = dateFormat.format(new Date());
                    String uid = studAttInfo.getSuid();
                    String name=stinfo.getSname();
                    String roll=stinfo.getSroll();
                    markatt.put(uid, new StudAttendace(date, present,roll,name));
                    if (present) {
                        holder.cd.setCardBackgroundColor(cl[1]); // Present color
                    } else {
                        holder.cd.setCardBackgroundColor(cl[0]); // Absent color
                    }
                    holder.checkBox.setChecked(stinfo.isPresent());
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return studList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView roll,name;
        public CheckBox checkBox;
        public CardView cd;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            roll=itemView.findViewById(R.id.tx_roll);
            name=itemView.findViewById(R.id.tx_name);
            checkBox=itemView.findViewById(R.id.precheckbox);
            cd=itemView.findViewById(R.id.cardv);

        }
    }

}
