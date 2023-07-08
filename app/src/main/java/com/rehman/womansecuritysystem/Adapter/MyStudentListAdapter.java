package com.rehman.womansecuritysystem.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rehman.womansecuritysystem.Driver.StudentDetailsActivity;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.R;

import java.util.List;

public class MyStudentListAdapter extends RecyclerView.Adapter<MyStudentListAdapter.MyViewHolder>
{
    private Context context;
    private List<ChildModel> mDatalist;

    public MyStudentListAdapter(Context context, List<ChildModel> mDatalist) {
        this.context = context;
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public MyStudentListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(context).inflate(R.layout.student_list_layout,parent,false);

        return new MyViewHolder(myview);



    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyStudentListAdapter.MyViewHolder holder, int position) {


        SharedPreferences preferences  = context.getSharedPreferences("CURRENT", Context.MODE_PRIVATE);
        String accountType = preferences.getString("accountType","");
        String username = preferences.getString("userName","");

        holder.btn_add.setVisibility(View.GONE);

        ChildModel model = mDatalist.get(position);
        if (model.getStudentGetDriver().equals(username))
        {
            holder.tv_parentName.setText("Parent Name: "+model.getFullName());
            holder.tv_childName.setText("Child Name: "+model.getChildName());
            holder.tv_schoolName.setText("Institute Name: "+model.getInstituteName());
        }

        holder.btn_add.setOnClickListener(v -> {
            holder.btn_add.setVisibility(View.GONE);
            holder.btn_remove.setVisibility(View.VISIBLE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students");
            reference.child(model.getAddKey()).child("studentGetDriver").setValue(username);
        });

        holder.btn_remove.setOnClickListener(v -> {
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.btn_remove.setVisibility(View.GONE);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students");
            reference.child(model.getAddKey()).child("studentGetDriver").setValue("none");
        });

        holder.tv_info.setOnClickListener(v -> {
            Intent intent  =  new Intent(context, StudentDetailsActivity.class);
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
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_parentName,tv_childName,tv_schoolName,tv_info;
        Button btn_add,btn_remove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_parentName = itemView.findViewById(R.id.tv_parentName);
            tv_childName = itemView.findViewById(R.id.tv_childName);
            tv_schoolName = itemView.findViewById(R.id.tv_schoolName);
            btn_add = itemView.findViewById(R.id.btn_add);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            tv_info = itemView.findViewById(R.id.tv_info);

        }
    }
}
