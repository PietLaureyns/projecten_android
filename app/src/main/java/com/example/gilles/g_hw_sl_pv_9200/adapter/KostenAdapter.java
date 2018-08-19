package com.example.gilles.g_hw_sl_pv_9200.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gilles.g_hw_sl_pv_9200.Activities.KostActivity;
import com.example.gilles.g_hw_sl_pv_9200.R;
import com.example.gilles.g_hw_sl_pv_9200.model.Kost;

import java.util.Collections;
import java.util.List;

public class KostenAdapter extends RecyclerView.Adapter<KostenViewHolder>  {

    List<Kost> list = Collections.emptyList();
    private Context context;

    public KostenAdapter(List<Kost> list, Context context){
        this.list = list;
        //for(Kost k : list)
            this.context = context;
    }

    public List<Kost> getList(){
        return list;
    }

    @Override
    public KostenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.kost_list_item, parent, false);
        KostenViewHolder holder = new KostenViewHolder(v);
        Log.d(this.getClass().getSimpleName(), "Creating viewholder");
        return holder;
    }

    @Override
    public void onBindViewHolder(KostenViewHolder holder, final int position) {
        holder.setData(list.get(position));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
        Log.d(this.getClass().getSimpleName(), "Binding position " + position);
    }

    public void filterList(List<Kost> kostenList){
        list = kostenList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void onItemClick(int position){
        Intent intent = new Intent(context, KostActivity.class);
        intent.putExtra("kost", list.get(position));
        context.startActivity(intent);
    }

}
