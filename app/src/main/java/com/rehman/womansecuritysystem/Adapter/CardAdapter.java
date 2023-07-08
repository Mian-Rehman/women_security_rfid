package com.rehman.womansecuritysystem.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rehman.womansecuritysystem.Driver.DriverLoctionDetailsActivity;
import com.rehman.womansecuritysystem.Model.CardModel;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder>
{
    private Context context;
    private List<CardModel> mDatalist;

    public CardAdapter(Context context, List<CardModel> mDatalist) {
        this.context = context;
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(context).inflate(R.layout.card_layout,parent,false);

        return new MyViewHolder(myview);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardAdapter.MyViewHolder holder, int position) {

        CardModel model = mDatalist.get(position);
        holder.tv_currentDate.setText("Date: "+ model.getCurrentDate());
        holder.tv_time.setText("Time: "+ model.getCurrentTime());

    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_currentDate,tv_time,tv_driver;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_currentDate = itemView.findViewById(R.id.tv_currentDate);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
