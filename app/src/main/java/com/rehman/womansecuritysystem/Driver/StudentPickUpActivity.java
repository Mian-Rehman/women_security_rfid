package com.rehman.womansecuritysystem.Driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.rehman.womansecuritysystem.Adapter.CardAdapter;
import com.rehman.womansecuritysystem.Adapter.MyChildDriverAdapter;
import com.rehman.womansecuritysystem.Model.CardModel;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.R;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentPickUpActivity extends AppCompatActivity {

    ImageView back_image;
    Button btn_next;
    String accountType,username,currentTime,currentDate;
    RecyclerView recycleView;
    ArrayList<CardModel> mDataList;
    CardAdapter adapter;
    String cardNumber,accountCreationKey;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_pick_up);

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");
        accountCreationKey = preferences.getString("accountCreationKey","");



        back_image=findViewById(R.id.back_image);
        btn_next=findViewById(R.id.btn_next);
        recycleView=findViewById(R.id.recycleView);

        currentDate = getCurrentdate();

        btn_next.setVisibility(View.GONE);

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        btn_next.setOnClickListener(v -> {
            startActivity(new Intent(StudentPickUpActivity.this,StudentCallActivity.class));
        });

        getcardNumber();

//
    }
    private void getcardNumber()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("RFIDDetails")
                .child(accountCreationKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    cardNumber = snapshot.child("rfidNumber").getValue(String.class);
                    btn_next.setVisibility(View.VISIBLE);
                    getCardsValue();
                }else
                {
                    Toast.makeText(StudentPickUpActivity.this, "Not exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getCardsValue()
    {
        mDataList=new ArrayList<>();
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CardAdapter(this,mDataList);
        recycleView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cardEntries");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    CardModel model  = snapshot.getValue(CardModel.class);
                    assert model != null;
                    if (cardNumber.equals(model.getRfidCardNumber()))
                    {
                        if (currentDate.equals(model.getCurrentDate()))
                        {
                            btn_next.setVisibility(View.VISIBLE);
                            mDataList.add(model);
                            adapter.notifyDataSetChanged();
                        }

                    }else
                    {
                        Toast.makeText(StudentPickUpActivity.this, "card not found", Toast.LENGTH_SHORT).show();
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

    private String getTimeWithAmPm()
    {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getCurrentdate()
    {
        return new SimpleDateFormat("dd/LLL/yyyy", Locale.getDefault()).format(new Date());
    }

}