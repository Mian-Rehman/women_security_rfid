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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.Driver.StudentCallActivity;
import com.rehman.womansecuritysystem.Driver.StudentDetailsActivity;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StudentCallAdapter extends RecyclerView.Adapter<StudentCallAdapter.MyViewHolder>
{
    private Context context;
    private List<ChildModel> mDatalist;
    StudentCallActivity studentCallActivity;
    ArrayList<String> mList;

    public StudentCallAdapter(Context context, List<ChildModel> mDatalist, StudentCallActivity studentCallActivity) {
        this.context = context;
        this.mDatalist = mDatalist;
        this.studentCallActivity = studentCallActivity;

    }

    @NonNull
    @Override
    public StudentCallAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(context).inflate(R.layout.student_list_layout,parent,false);

        return new MyViewHolder(myview);



    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StudentCallAdapter.MyViewHolder holder, int position) {


        SharedPreferences preferences  = context.getSharedPreferences("CURRENT", Context.MODE_PRIVATE);
        String accountType = preferences.getString("accountType","");
        String username = preferences.getString("userName","");

        holder.btn_remove.setVisibility(View.GONE);
        holder.btn_add.setVisibility(View.VISIBLE);


        ChildModel model = mDatalist.get(position);
        if (model.getStudentGetDriver().equals(username))
        {
            holder.tv_parentName.setText("Parent Name: "+model.getFullName());
            holder.tv_childName.setText("Child Name: "+model.getChildName());
            holder.tv_schoolName.setText("Institute Name: "+model.getInstituteName());
        }


        holder.btn_remove.setOnClickListener(v -> {
            holder.btn_add.setVisibility(View.VISIBLE);
            holder.btn_remove.setVisibility(View.GONE);
           FirebaseDatabase.getInstance().getReference("StudentsDropPick").child(username)
                   .child("studentDropKey").removeValue();
        });

        holder.btn_add.setOnClickListener(v -> {
            String currentTime = getTimeWithAmPm();
            String currentDate = getCurrentdate();
            holder.btn_add.setVisibility(View.GONE);
            holder.btn_remove.setVisibility(View.VISIBLE);

                 mList = new ArrayList<>();
            Toast.makeText(context, mList.toString(), Toast.LENGTH_SHORT).show();


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

    private String getTimeWithAmPm()
    {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getCurrentdate()
    {
        return new SimpleDateFormat("dd/LLL/yyyy", Locale.getDefault()).format(new Date());
    }

    public interface IMethodCaller
    {
        void desireMethod();
    }
}
