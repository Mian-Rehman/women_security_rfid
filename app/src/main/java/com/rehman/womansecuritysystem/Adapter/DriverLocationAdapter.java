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
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.Parent.DriverDetailsActivity;
import com.rehman.womansecuritysystem.R;

import java.util.List;

public class DriverLocationAdapter extends RecyclerView.Adapter<DriverLocationAdapter.MyViewHolder>
{
    private Context context;
    private List<ChildModel> mDatalist;

    public DriverLocationAdapter(Context context, List<ChildModel> mDatalist) {
        this.context = context;
        this.mDatalist = mDatalist;
    }

    @NonNull
    @Override
    public DriverLocationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(context).inflate(R.layout.driver_location_layout,parent,false);

        return new MyViewHolder(myview);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DriverLocationAdapter.MyViewHolder holder, int position) {


        SharedPreferences preferences  = context.getSharedPreferences("CURRENT", Context.MODE_PRIVATE);
        String accountType = preferences.getString("accountType","");
        String username = preferences.getString("userName","");

        ChildModel model = mDatalist.get(position);
        if (model.getStudentGetDriver().equals("none"))
        {
            holder.card.setVisibility(View.GONE);
            Log.d("TAG", "onBindViewHolder: ");
        }else
        {
            holder.tv_parentName.setText("Parent Name: "+model.getFullName());
            holder.tv_childName.setText("Child Name: "+model.getChildName());
            holder.tv_driver.setText("Driver: "+model.getStudentGetDriver());
        }

        holder.tv_lastLocation.setOnClickListener(v -> {
            Intent intent = new Intent(context, DriverLoctionDetailsActivity.class);
            intent.putExtra("driverUsername",model.getStudentGetDriver());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_parentName,tv_childName,tv_driver,tv_lastLocation;
        CardView card;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_parentName = itemView.findViewById(R.id.tv_parentName);
            tv_childName = itemView.findViewById(R.id.tv_childName);
            tv_driver = itemView.findViewById(R.id.tv_driver);
            tv_lastLocation = itemView.findViewById(R.id.tv_lastLocation);
            card = itemView.findViewById(R.id.card);
        }
    }
}
