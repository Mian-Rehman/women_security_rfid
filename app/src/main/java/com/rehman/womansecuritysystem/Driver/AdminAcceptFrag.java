package com.rehman.womansecuritysystem.Driver;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rehman.womansecuritysystem.Adapter.AdminRejectAdapter;
import com.rehman.womansecuritysystem.Model.UserModel;
import com.rehman.womansecuritysystem.R;

import java.util.ArrayList;


public class AdminAcceptFrag extends Fragment {

    RecyclerView recycleView;
    ArrayList<UserModel> mDataList;
    AdminRejectAdapter adapter;
    SwipeRefreshLayout swipeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_accept, container, false);

        initViews(view);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getDriverList();

                swipeLayout.setRefreshing(false);
            }
        });

        getDriverList();

        return view;
    }

    private void getDriverList()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Driver");

        mDataList=new ArrayList<>();
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new AdminRejectAdapter(getActivity(),mDataList);
        recycleView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    if (snapshot.child("checkVerification").exists())
                    {
                        String value = snapshot.child("checkVerification").child("accountVerification")
                                .getValue(String.class);
                        assert value != null;
                        if (value.equals("accept"))
                        {
                            UserModel model = snapshot.getValue(UserModel.class);
                            assert model != null;
                            mDataList.add(model);
                            adapter.notifyDataSetChanged();
                        }
                    }

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

    private void initViews(View view)
    {
        recycleView = view.findViewById(R.id.recycleView);
        swipeLayout = view.findViewById(R.id.swipeLayout);
    }
}