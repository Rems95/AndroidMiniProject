package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder>
{
    List<Trajet> data;
    Context context;
    public RecyclerAdapter(List<Trajet> data, Context context)

    {
        this.data = data;
        this.context=context;
        System.out.println("Nice:"+data);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.singlerow,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
    {
        final Trajet temp=data.get(position);
        System.out.println("Nice1:"+temp);
        holder.t1.setText(data.get(position).getName());
        holder.t2.setText(data.get(position).getFrom());
        holder.t3.setText(data.get(position).getTo() +"\n"+ "("+data.get(position).getSeat()+ ")" +"places");





    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}