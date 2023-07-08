package com.rehman.womansecuritysystem.Driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rehman.womansecuritysystem.Adapter.MyStudentListAdapter;
import com.rehman.womansecuritysystem.Adapter.StudentListAdapter;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.R;

import java.util.ArrayList;

public class MyStudentList extends AppCompatActivity {

    RecyclerView recycleView;
    ImageView back_image;
    ArrayList<ChildModel> mDataList;
    MyStudentListAdapter adapter;
    String username,accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_student_list);

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        initViews();
        getChildList();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void getChildList()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Students");

        mDataList=new ArrayList<>();
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MyStudentListAdapter(this,mDataList);
        recycleView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    ChildModel model = snapshot.getValue(ChildModel.class);
                    assert model != null;
                    if (model.getStudentGetDriver().equals(username))
                    {
                        mDataList.add(model);
                        adapter.notifyDataSetChanged();
                    }
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