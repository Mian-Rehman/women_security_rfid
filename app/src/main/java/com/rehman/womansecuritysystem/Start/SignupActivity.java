package com.rehman.womansecuritysystem.Start;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rehman.womansecuritysystem.Model.UserModel;
import com.rehman.womansecuritysystem.R;

public class SignupActivity extends AppCompatActivity {

    EditText ed_name,ed_phoneNumber,ed_email,ed_password,ed_confirmPassword,ed_userName;
    Button btn_create;
    String name,phoneNumber,email,password,confirmPassword,userUID ="",username;

    ProgressDialog progressDialog;
    String accountType;
    TextView title_text;
    ImageView back_image;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        initViews();

        Intent intent = getIntent();
        accountType = intent.getStringExtra("accountType");
        title_text.setText("Create " + accountType + " Account");

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        btn_create.setOnClickListener(v -> {

            progressDialog = ProgressDialog.show(SignupActivity.this, "", "Please wait", true);
            name = ed_name.getText().toString().trim();
            phoneNumber = ed_phoneNumber.getText().toString().trim();
            email = ed_email.getText().toString().trim();
            password = ed_password.getText().toString().trim();
            confirmPassword = ed_confirmPassword.getText().toString().trim();
            username = ed_userName.getText().toString().trim();

            if (isValid(name,phoneNumber,email,password,username))
            {
                if (password.equals(confirmPassword))
                {
                    savedDataToFirebase();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "password Don't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void savedDataToFirebase()
    {

        DatabaseReference  reference=FirebaseDatabase.getInstance().getReference(accountType);
        Query query=reference.orderByChild("userName").equalTo(username);

         query.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {

              if (snapshot.exists())
              {
                  String checkUser=snapshot.child(username).child("userName").getValue(String.class);

                  assert checkUser != null;
                  if (checkUser.equals(username))
                  {
                      progressDialog.dismiss();
                      ed_userName.setError("Username Already Exist!");
                      ed_userName.requestFocus();
                      ed_userName.setText("");
                      return;

                  }
                  else
                  {
                      String accountCreationKey = reference.push().getKey();
                      UserModel model = new UserModel(name,username,phoneNumber,email,password,accountCreationKey,
                              accountType);
                      String key=reference.child(username).getKey();
                      reference.child(key).setValue(model);
                      Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                      progressDialog.dismiss();
                  }
              }
              else
              {

                  String accountCreationKey = reference.push().getKey();
                  UserModel model = new UserModel(name,username,phoneNumber,email,password,accountCreationKey,
                          accountType);
                  String key=reference.child(username).getKey();
                  reference.child(key).setValue(model);
                  Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                  progressDialog.dismiss();
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

    }

    private boolean isValid(String name, String phoneNumber, String email,
                            String password,String username)
    {
        if (name.isEmpty())
        {
            ed_name.setError("field required");
            ed_name.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (username.isEmpty())
        {
            ed_userName.setError("field required");
            ed_userName.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        if (phoneNumber.isEmpty() || phoneNumber.length() <10)
        {
            ed_phoneNumber.setError("field required");
            ed_phoneNumber.requestFocus();
            progressDialog.dismiss();
            return false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern) || email.isEmpty())
        {
            ed_email.setError("invalid Email");
            ed_email.requestFocus();
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



        if (confirmPassword.isEmpty())
        {
            ed_confirmPassword.setError("field required");
            ed_confirmPassword.requestFocus();
            progressDialog.dismiss();
            return false;
        }
        return true;
    }

    private void initViews()
    {
        ed_name = findViewById(R.id.ed_name);
        ed_phoneNumber = findViewById(R.id.ed_phoneNumber);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_confirmPassword = findViewById(R.id.ed_confirmPassword);
        btn_create = findViewById(R.id.btn_create);
        title_text = findViewById(R.id.title_text);
        back_image = findViewById(R.id.back_image);
        ed_userName = findViewById(R.id.ed_userName);
    }
}