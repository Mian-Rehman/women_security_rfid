package com.rehman.womansecuritysystem.Start;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.Admin.AdminMainActivity;
import com.rehman.womansecuritysystem.ConfirmActivity;
import com.rehman.womansecuritysystem.Driver.DriverMainActivity;
import com.rehman.womansecuritysystem.MainActivity;
import com.rehman.womansecuritysystem.Model.UserModel;
import com.rehman.womansecuritysystem.Parent.ParentMainActivity;
import com.rehman.womansecuritysystem.R;
import com.rehman.womansecuritysystem.Student.StudentMainActivity;

public class LoginActivity extends AppCompatActivity {

    ImageView back_image;
    TextView title_text,createAccount_text,text;
    EditText ed_username,ed_password;
    Button btn_signIn;
    String myAccountType;
    ProgressDialog progressDialog;
    String username,password,rollNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        initViews();
        Intent intent = getIntent();
        myAccountType = intent.getStringExtra("accountType");
        title_text.setText(myAccountType + " Sign in");

        if (myAccountType.equals("Student"))
        {
            createAccount_text.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }

        if (myAccountType.equals("Admin"))
        {
            text.setVisibility(View.GONE);
            createAccount_text.setVisibility(View.GONE);
        }

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        createAccount_text.setOnClickListener(v -> {
            Intent nextIntent = new Intent(LoginActivity.this, SignupActivity.class);
            nextIntent.putExtra("accountType",myAccountType);
            startActivity(nextIntent);
        });

        btn_signIn.setOnClickListener(v -> {
            progressDialog = ProgressDialog.show(this, "", "Please wait", true);
            username = ed_username.getText().toString().trim();
            password = ed_password.getText().toString().trim();

            if (isValid(username,password))
            {
                if (myAccountType.equals("Admin"))
                {
                    checkAdmin(username,password);

                }else if(myAccountType.equals("Student"))
                {
                   String accountType = "Parent";
                    userLogin(accountType);
                }
                else if (myAccountType.equals("Parent")){
                   String accountType = "Parent";
                    userLogin(accountType);
                }
                else if (myAccountType.equals("Driver"))
                {
                    String accountType = "Driver";
                    userLogin(accountType);
                }

            }
        });

    }


    private void checkAdmin(String username, String password)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admin");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String adminUsername = snapshot.child("adminUsername").getValue(String.class);
                    String adminPassword = snapshot.child("adminPassword").getValue(String.class);
                    assert adminUsername != null;
                    if (adminUsername.equals(username))
                    {
                        assert adminPassword != null;
                        if (adminPassword.equals(password))
                        {
                            startActivity(new Intent(LoginActivity.this,AdminMainActivity.class));
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            ed_password.setError("invalid password");
                            ed_password.requestFocus();
                            return;
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        ed_password.setError("invalid username");
                        ed_password.requestFocus();
                        return;
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Account Not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userLogin(String accountType)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(accountType);
        Query query=reference.orderByChild("userName").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    String loginUsername=snapshot.child(username).child("userName").getValue(String.class);
                    String loginPassword=snapshot.child(username).child("password").getValue(String.class);

                    if (loginUsername.equals(username))
                    {
                        if (loginPassword.equals(password))
                        {
                            UserModel model = snapshot.child(username).getValue(UserModel.class);

                            SharedPreferences sp=getSharedPreferences("CURRENT",MODE_PRIVATE);
                            SharedPreferences.Editor ed=sp.edit();

                            assert model != null;
                            ed.putString("name",model.getName());
                            ed.putString("userName",model.getUserName());
                            ed.putString("phoneNumber",model.getPhoneNumber());
                            ed.putString("email",model.getEmail());
                            ed.putString("password",model.getPassword());
                            ed.putString("accountType",model.getAccountType());
                            ed.putString("accountCreationKey",model.getAccountCreationKey());
                            ed.apply();

                            if (myAccountType.equals("Driver"))
                            {
                                startActivity(new Intent(LoginActivity.this, DriverMainActivity.class));
                                finish();
                            }else if (myAccountType.equals("Parent"))
                            {
                                startActivity(new Intent(LoginActivity.this, ParentMainActivity.class));
                                finish();
                            }else if (myAccountType.equals("Student"))
                            {
                                startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                                finish();
                            }



                        }
                        else
                        {
                            progressDialog.dismiss();
                            ed_password.setError("invalid password");
                            ed_password.requestFocus();
                            return;
                        }
                    }
                    else
                    {
                        progressDialog.dismiss();
                        ed_password.setError("invalid username");
                        ed_password.requestFocus();
                        return;
                    }

                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Account Not Found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews()
    {
        title_text = findViewById(R.id.title_text);
        back_image = findViewById(R.id.back_image);
        ed_username = findViewById(R.id.ed_username);
        ed_password = findViewById(R.id.ed_password);
        btn_signIn = findViewById(R.id.btn_signIn);
        createAccount_text = findViewById(R.id.createAccount_text);
        text = findViewById(R.id.text);
    }



    private boolean isValid(String username, String password)
    {

        if (username.isEmpty())
        {
            ed_username.setError("field required");
            ed_username.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (password.isEmpty())
        {
            ed_password.setError("field required");
            ed_password.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        return true;
    }
}