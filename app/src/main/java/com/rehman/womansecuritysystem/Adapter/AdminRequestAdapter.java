package com.rehman.womansecuritysystem.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.rehman.womansecuritysystem.Admin.RequestDetailsActivity;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.Model.RequestModel;
import com.rehman.womansecuritysystem.Parent.MyChildActivity;
import com.rehman.womansecuritysystem.Parent.ParentAddActivity;
import com.rehman.womansecuritysystem.R;

import java.util.List;

public class AdminRequestAdapter extends RecyclerView.Adapter<AdminRequestAdapter.MyViewHolder>
{
    private Context context;
    private List<RequestModel> mDatalist;

    public AdminRequestAdapter(Context context, List<RequestModel> mDatalist) {
        this.context = context;
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public AdminRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(context).inflate(R.layout.request_list_layout,parent,false);

        return new MyViewHolder(myview);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdminRequestAdapter.MyViewHolder holder, int position) {


        RequestModel model = mDatalist.get(position);

        holder.tv_driverusername.setText(model.getDriverUsername());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RequestDetailsActivity.class);
            intent.putExtra("requestKey",model.getRequestKey());
            intent.putExtra("driverUsername",model.getDriverUsername());
            intent.putExtra("requestMessage",model.getRequestMessage());
            intent.putExtra("requestStatus",model.getRequestStatus());
            intent.putExtra("accountType",model.getAccountType());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_driverusername;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_driverusername = itemView.findViewById(R.id.tv_driverusername);


        }
    }
}
