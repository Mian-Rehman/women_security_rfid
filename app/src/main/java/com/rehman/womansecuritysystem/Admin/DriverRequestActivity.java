package com.rehman.womansecuritysystem.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rehman.womansecuritysystem.Adapter.AdminRequestAdapter;
import com.rehman.womansecuritysystem.Adapter.MyStudentListAdapter;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.Model.RequestModel;
import com.rehman.womansecuritysystem.R;

import java.util.ArrayList;

public class DriverRequestActivity extends AppCompatActivity {

    RecyclerView recycleView;
    ImageView back_image;
    ArrayList<RequestModel> mDataList;
    AdminRequestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request);

        initViews();
        getDriverList();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void  getDriverList()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("AdminDriverRequest");

        mDataList=new ArrayList<>();
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new AdminRequestAdapter(this,mDataList);
        recycleView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    RequestModel model = snapshot.getValue(RequestModel.class);
                    assert model != null;
                        mDataList.add(model);
                        adapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        recycleView = findViewById(R.id.recycleView);
    }
}