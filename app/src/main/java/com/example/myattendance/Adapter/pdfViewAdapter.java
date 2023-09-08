package com.example.myattendance.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myattendance.R;
import com.example.myattendance.ViewPdfActivity;
import com.example.myattendance.modul.fileInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

public class pdfViewAdapter extends FirebaseRecyclerAdapter<fileInfo, pdfViewAdapter.Myviewhoder> {

    public pdfViewAdapter(@NonNull FirebaseRecyclerOptions<fileInfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Myviewhoder holder, int position, @NonNull fileInfo model) {
        holder.txfilename.setText(model.getFilename());
        holder.imgpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.imgpdf.getContext(), ViewPdfActivity.class);
                intent.putExtra("filename",model.getFilename());
                intent.putExtra("fileurl",model.getFileurl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.imgpdf.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public Myviewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pdf_singl_row,parent,false);

        return new Myviewhoder(view);
    }


    public class Myviewhoder extends RecyclerView.ViewHolder{
        TextView txfilename;
        ImageView imgpdf;

        public Myviewhoder(@NonNull View itemView) {
            super(itemView);
            txfilename=itemView.findViewById(R.id.tx_filename);
            imgpdf=itemView.findViewById(R.id.img_pdf);

        }
    }
}
