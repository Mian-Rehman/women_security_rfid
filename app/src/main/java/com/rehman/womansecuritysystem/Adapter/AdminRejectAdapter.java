package com.rehman.womansecuritysystem.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.auth.data.model.User;
import com.rehman.womansecuritysystem.Admin.AdminRequestDetailsActivitty;
import com.rehman.womansecuritysystem.Model.UserModel;
import com.rehman.womansecuritysystem.R;

import java.util.List;

public class AdminRejectAdapter extends RecyclerView.Adapter<AdminRejectAdapter.MyViewHolder>
{
    private Context context;
    private List<UserModel> mDataList;

    public AdminRejectAdapter(Context context, List<UserModel> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public AdminRejectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(context).inflate(R.layout.booking_list_layout,parent,false);

        return new MyViewHolder(myview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdminRejectAdapter.MyViewHolder holder, int position) {

        UserModel model = mDataList.get(position);
        holder.tv_name.setText("Name: "+model.getName());
        holder.tv_email.setText("Email: "+model.getEmail());
        holder.tv_phone.setText("Phone: "+model.getPhoneNumber());



        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminRequestDetailsActivitty.class);
            intent.putExtra("name",model.getName());
            intent.putExtra("userName",model.getUserName());
            intent.putExtra("phoneNumber",model.getPhoneNumber());
            intent.putExtra("email",model.getEmail());
            intent.putExtra("password",model.getPassword());
            intent.putExtra("accountCreationKey",model.getAccountCreationKey());
            intent.putExtra("accountType",model.getAccountType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        TextView tv_name,tv_email,tv_phone,tv_moreDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_moreDetails = itemView.findViewById(R.id.tv_moreDetails);
        }
    }
}
