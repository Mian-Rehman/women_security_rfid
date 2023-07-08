package com.rehman.womansecuritysystem.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.Parent.MyChildActivity;
import com.rehman.womansecuritysystem.Parent.ParentAddActivity;
import com.rehman.womansecuritysystem.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildListAdapter extends RecyclerView.Adapter<ChildListAdapter.MyViewHolder>
{
    private Context context;
    private List<ChildModel> mDatalist;

    public ChildListAdapter(Context context, List<ChildModel> mDatalist) {
        this.context = context;
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(context).inflate(R.layout.my_child_list_item,parent,false);

        return new MyViewHolder(myview);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        SharedPreferences preferences  = context.getSharedPreferences("CURRENT", Context.MODE_PRIVATE);
        String accountType = preferences.getString("accountType","");
        String username = preferences.getString("userName","");

        ChildModel model = mDatalist.get(position);
        if (model.getParentUsername().equals(username))
        {
            holder.tv_parentName.setText("Parent Name: "+model.getFullName());
            holder.tv_childName.setText("Child Name: "+model.getChildName());
            holder.tv_schoolName.setText("Institute Name: "+model.getInstituteName());
            holder.tv_address.setText("Institute Address: "+model.getAddress());
        }

        holder.edit_image.setOnClickListener(v -> {
            Intent intent = new Intent(context, ParentAddActivity.class);
            intent.putExtra("fullName",model.getFullName());
            intent.putExtra("childName",model.getChildName());
            intent.putExtra("className",model.getClassName());
            intent.putExtra("instituteName",model.getInstituteName());
            intent.putExtra("instituteAddress",model.getInstituteAddress());
            intent.putExtra("phoneNumber",model.getPhoneNumber());
            intent.putExtra("address",model.getAddress());
            intent.putExtra("addKey",model.getAddKey());
            intent.putExtra("userUsername",model.getUserUsername());
            intent.putExtra("userAccountType",model.getUserAccountType());
            intent.putExtra("studentGetDriver",model.getStudentGetDriver());
            intent.putExtra("parentUsername",model.getParentUsername());
            intent.putExtra("activity","adapter");
            intent.putExtra("rollNumber",model.getRollNumber());
            context.startActivity(intent);
        });

        holder.delete_Image.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference().child("Students")
                    .child(model.getAddKey()).removeValue();
            ((MyChildActivity)context).finish();
            context.startActivity(new Intent(context,MyChildActivity.class));
        });


    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_parentName,tv_childName,tv_schoolName,tv_address;
        ImageView edit_image,delete_Image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_parentName = itemView.findViewById(R.id.tv_parentName);
            tv_childName = itemView.findViewById(R.id.tv_childName);
            tv_schoolName = itemView.findViewById(R.id.tv_schoolName);
            edit_image = itemView.findViewById(R.id.edit_image);
            delete_Image = itemView.findViewById(R.id.delete_Image);
            tv_address = itemView.findViewById(R.id.tv_address);

        }
    }
}
