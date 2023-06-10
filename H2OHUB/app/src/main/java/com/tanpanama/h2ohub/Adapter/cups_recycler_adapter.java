package com.tanpanama.h2ohub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanpanama.h2ohub.Dashboard.cups_recycler_interface;
import com.tanpanama.h2ohub.Data.CupsData;
import com.tanpanama.h2ohub.R;

import java.util.ArrayList;

public class cups_recycler_adapter extends RecyclerView.Adapter<cups_recycler_adapter.Holder> {
    private Context ctx;
    private ArrayList<CupsData> cd;
    private final cups_recycler_interface cri;
    private boolean isRegular;

    public cups_recycler_adapter(Context ctx, ArrayList<CupsData> cd, cups_recycler_interface cri, boolean isRegular){
        this.ctx = ctx;
        this.cd = cd;
        this.cri = cri;
        this.isRegular = isRegular;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view;
        if(isRegular){
            view = inflater.inflate(R.layout.activity_cups_item, parent, false);
        }
        else{
            view = inflater.inflate(R.layout.activity_cups_mini_item, parent, false);
        }

        return new Holder(view, cri);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.iv.setImageBitmap(cd.get(position).getBitmapPicture());
        holder.tv.setText(cd.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cd.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tv;
        public Holder(@NonNull View itemView,  cups_recycler_interface cri) {
            super(itemView);

            iv = itemView.findViewById(R.id.ivCups);
            tv = itemView.findViewById(R.id.tv_cups);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cri != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            cri.onItemClicked(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(cri != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            cri.onItemLongClicked(position);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
