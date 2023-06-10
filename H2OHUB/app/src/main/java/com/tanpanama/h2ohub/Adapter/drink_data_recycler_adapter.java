package com.tanpanama.h2ohub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanpanama.h2ohub.Data.DrinkData;
import com.tanpanama.h2ohub.Handler.dateHandler;
import com.tanpanama.h2ohub.R;

import java.util.ArrayList;

public class drink_data_recycler_adapter extends RecyclerView.Adapter<drink_data_recycler_adapter.Holder>{

    private Context ctx;
    private ArrayList<DrinkData> aldd;
    private dateHandler dh = new dateHandler();

    public drink_data_recycler_adapter(Context ctx, ArrayList<DrinkData> aldd){
        this.ctx = ctx;
        this.aldd = aldd;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.activity_drink_data_item, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        dh.setDate(aldd.get(position).getDate());
        holder.date.setText(dh.getDayName()+", "+dh.getFancyDate());
        holder.drank.setText(aldd.get(position).getTotal_drink()+"");
        holder.limit.setText(aldd.get(position).getMax_drink()+"");
        holder.target.setText(aldd.get(position).getTarget_drink()+"");
    }

    @Override
    public int getItemCount() {
        return aldd.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{

        TextView date, drank, limit, target;

        public Holder(@NonNull View itemView) {
            super(itemView);

            date  = itemView.findViewById(R.id.tv_date);
            drank  = itemView.findViewById(R.id.tv_drank);
            limit  = itemView.findViewById(R.id.tv_limit);
            target  = itemView.findViewById(R.id.tv_target);
        }
    }
}
