package com.example.myapplication;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView t1,t2,t3;
    RelativeLayout l;
    public ViewHolder(@NonNull View itemView)
    {
        super(itemView);
        System.out.println("View");
        t1=(TextView)itemView.findViewById(R.id.t1);
        t2=(TextView)itemView.findViewById(R.id.t2);
        t3=(TextView)itemView.findViewById(R.id.t3);
        l=(RelativeLayout)itemView.findViewById(R.id.row);
    }
}
