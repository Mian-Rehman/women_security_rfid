package com.rehman.womansecuritysystem.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rehman.womansecuritysystem.Adapter.MyChildDriverAdapter;
import com.rehman.womansecuritysystem.Adapter.UserListAdapter;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.Model.UserModel;
import com.rehman.womansecuritysystem.R;

import java.util.ArrayList;

public class AdminDriverListActivity extends AppCompatActivity {

    ImageView back_image;
    RecyclerView recycleView;
    ArrayList<UserModel> mDataList;
    UserListAdapter adapter;
    String username,accountType,userAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_driver_list);

        initViews();



        Intent intent = getIntent();
        userAccountType = intent.getStringExtra("userAccountType");

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        getChildList();
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        recycleView = findViewById(R.id.recycleView);
    }

    private void getChildList()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference(userAccountType);

        mDataList=new ArrayList<>();
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserListAdapter(this,mDataList);
        recycleView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    UserModel model = snapshot.getValue(UserModel.class);
                    assert model != null;
                        mDataList.add(model);
                        adapter.notifyDataSetChanged();
                }
            }

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
}