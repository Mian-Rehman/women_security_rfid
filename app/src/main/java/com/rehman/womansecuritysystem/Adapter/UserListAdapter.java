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

import com.rehman.womansecuritysystem.Admin.UserDetailsActivity;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.Model.UserModel;
import com.rehman.womansecuritysystem.Parent.DriverDetailsActivity;
import com.rehman.womansecuritysystem.R;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder>
{
    private Context context;
    private List<UserModel> mDatalist;

    public UserListAdapter(Context context, List<UserModel> mDatalist) {
        this.context = context;
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(context).inflate(R.layout.user_list_layout,parent,false);

        return new MyViewHolder(myview);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {

        UserModel model = mDatalist.get(position);
        holder.tv_name.setText("Name: "+model.getName());
        holder.tv_email.setText("Email: "+model.getEmail());
        holder.tv_phone.setText("Name: "+model.getPhoneNumber());

        holder.tv_moreDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserDetailsActivity.class);
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
        return mDatalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

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
