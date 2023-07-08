package com.rehman.womansecuritysystem.Driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rehman.womansecuritysystem.Adapter.StudentCallAdapter;
import com.rehman.womansecuritysystem.Model.ChildModel;
import com.rehman.womansecuritysystem.R;

import java.util.ArrayList;
import java.util.Locale;

public class StudentCallActivity extends AppCompatActivity {

    ImageView back_image;
    RecyclerView recycleView;
    ArrayList<ChildModel> mDataList;
    StudentCallAdapter adapter;
    String username,accountType,userAccountType;
    TextToSpeech speech;
    String speechText;
    TextView name_text;
    SeekBar seekPitch,seekSpeed;
    Button btn_speech;
    String second;
    ArrayList<String> mList;
    StringBuilder builder = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_call);

        initViews();

        SharedPreferences preferences  = getSharedPreferences("CURRENT", MODE_PRIVATE);
        accountType = preferences.getString("accountType","");
        username = preferences.getString("userName","");

        textSpeech();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

//        getChildList();
        childListCall();
        btn_speech.setOnClickListener(v -> {
            name_text.setText("");
            builder = new StringBuilder();
            mList.clear();
            for (ChildModel childModel : mDataList)
            {
                String name = "Class: " + childModel.getClassName() + ", " + "Roll No: " + childModel.getRollNumber() + " , " + childModel.getChildName() + " : ";
                mList.add(name);
            }

            builder = new StringBuilder();
            for (int i=0;i<mList.size();i++)
            {
                String name = mList.get(i);
                builder.append(name.toString()).append(" Calling to the gate\n");
            }
            second  = builder.toString();
            name_text.setText(second);
          voiceInput(second);
        });



    }

    private void childListCall()
    {
        mDataList=new ArrayList<>();
        mList = new ArrayList<>();

        StringBuilder builder = new StringBuilder();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Students");
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

    private void voiceInput(String speechText)
    {
        float pitch = (float) seekPitch.getProgress() / 50;
        if(pitch < 0.1) pitch = 0.1f;
        float speed = (float) seekSpeed.getProgress() / 50;
        if (speed < 0.1) speed = 0.1f;

        speech.setPitch(pitch);
        speech.setSpeechRate(speed);
        speech.speak(speechText,TextToSpeech.QUEUE_FLUSH,null);

    }

    public void textSpeech()
    {
        speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS)
                {
                    int result = speech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(StudentCallActivity.this, "Language not found", Toast.LENGTH_SHORT).show();
                    }else
                    {
//                        Toast.makeText(StudentCallActivity.this, "Language found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        recycleView = findViewById(R.id.recycleView);
        name_text = findViewById(R.id.name_text);
        seekPitch=findViewById(R.id.seekPitch);
        seekSpeed=findViewById(R.id.seekSpeed);
        btn_speech=findViewById(R.id.btn_speech);
    }

    private void getChildList()
    {
        DatabaseReference reference  = FirebaseDatabase.getInstance().getReference("Students");

        mDataList=new ArrayList<>();
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new StudentCallAdapter(this,mDataList,this);
        recycleView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists())
                {
                    ChildModel model = snapshot.getValue(ChildModel.class);
                   if (model.getStudentGetDriver().equals(username))
                   {
                       assert model != null;
                       mDataList.add(model);
                       adapter.notifyDataSetChanged();
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

}