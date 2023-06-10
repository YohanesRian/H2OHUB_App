package com.tanpanama.h2ohub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanpanama.h2ohub.Dashboard.Setting.setting_recycler_interface;
import com.tanpanama.h2ohub.Dashboard.cups_recycler_interface;
import com.tanpanama.h2ohub.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class setting_recycler_adapter  extends RecyclerView.Adapter<setting_recycler_adapter.Holder>{
    private ArrayList<String> title;
    private ArrayList<String> data;
    private Context ctx;
    private setting_recycler_interface sri;

    public setting_recycler_adapter(Context ctx, ArrayList<String> title, ArrayList<String> data, setting_recycler_interface sri){
        this.title = title;
        this.data = data;
        this.ctx = ctx;
        this.sri = sri;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_setting_item, parent, false);

        return new Holder(view, sri);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.tv_data.setText(data.get(position));
        holder.tv_title.setText(title.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        TextView tv_title, tv_data;
        public Holder(@NonNull View itemView, setting_recycler_interface sri) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_data = itemView.findViewById(R.id.tv_data);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(sri != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            sri.onItemClicked(position);
                        }
                    }
                }
            });
        }
    }
}
