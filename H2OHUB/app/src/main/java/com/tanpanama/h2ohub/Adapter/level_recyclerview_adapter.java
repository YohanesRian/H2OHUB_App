package com.tanpanama.h2ohub.Adapter;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.GetStarted.level_serial;
import com.tanpanama.h2ohub.R;

import java.util.ArrayList;

public class level_recyclerview_adapter extends RecyclerView.Adapter<level_recyclerview_adapter.Holder> {


    private Context ctx;
    private ArrayList<level_serial> als;
    private int checkedPosition = 0;

    private LinearLayout prevll;


    public level_recyclerview_adapter(Context ctx, ArrayList<level_serial> als){
        this.als = als;
        this.ctx = ctx;
    }

    public void SetLevel(ArrayList<level_serial> als){
        this.als = als;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.activity_item_level_active, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(als.get(position));
    }

    @Override
    public int getItemCount() {
        return als.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView level;
        public TextView level_desc;
        public Holder(@NonNull View itemView){
            super(itemView);
            level = itemView.findViewById(R.id.level);
            level_desc = itemView.findViewById(R.id.level_desc);
        }

        void bind(final level_serial ls){
            LinearLayout ll = (LinearLayout) itemView.findViewById(R.id.item_level);
            if(checkedPosition == -1){
                ll.setBackgroundColor(itemView.getResources().getColor(R.color.Radio1));
            }
            else{
                if(checkedPosition == getAdapterPosition()){
                    ll.setBackgroundColor(itemView.getResources().getColor(R.color.Green));
                    prevll = ll;
                }
                else{
                    ll.setBackgroundColor(itemView.getResources().getColor(R.color.Radio1));
                }
            }

            level.setText(ls.getLevel());
            level_desc.setText(ls.getLevel_desc());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator colorFade1 = ObjectAnimator.ofObject(prevll, "backgroundColor", new ArgbEvaluator(), itemView.getResources().getColor(R.color.Green), itemView.getResources().getColor(R.color.Radio1));
                    ObjectAnimator colorFade2 = ObjectAnimator.ofObject(ll, "backgroundColor", new ArgbEvaluator(), itemView.getResources().getColor(R.color.Radio1), itemView.getResources().getColor(R.color.Green));
                    colorFade1.setDuration(150);
                    colorFade2.setDuration(150);
                    colorFade1.start();
                    colorFade2.start();


                    prevll = ll;
                    if(checkedPosition != getAdapterPosition()){
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }
    }

    public level_serial getSelected(){
        if(checkedPosition != -1){
            return als.get(checkedPosition);
        }
        return null;
    }
}
