package com.example.renewseviceqq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private Context mContext;
    EditText mEmail,mPassword,mCrPassword,mName,mPhone,mAddress;
    ImageView imageView;
    Button mRegisterBtn;
    TextView mLoginBtn;
    ProgressBar progressBar;
    String userID;


    ///--------firebase--------
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mCrPassword = findViewById(R.id.crpassword);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.creatext);
        mPhone = findViewById(R.id.input_phone);
        mAddress = findViewById(R.id.input_address);
        mName = findViewById(R.id.input_name);
        imageView = findViewById(R.id.imageView);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);





        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                final String username = mName.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                final String userphone = mPhone.getText().toString().trim();
                final String useraddress = mAddress.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String crPassword = mCrPassword.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    mName.setError("Full Name is Required.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(userphone)){
                    mPhone.setError("Phone Number is Required.");
                    return;
                }
                if(TextUtils.isEmpty(useraddress)){
                    mAddress.setError("Address is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if(password.length() <6){
                    mPassword.setError("Password must be >= 6 Characters");
                    return;
                }
                if(TextUtils.isEmpty(crPassword)){
                    mCrPassword.setError("Please confirm password");
                    return;
                }

                if(!password.equals(crPassword))
                {
                    mCrPassword.setError("Password do not match");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                //register the user in firebase

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "สร้างบัญชีผู้ใช้สำเร็จ", Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = mStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("userEmail",email);
                            user.put("userName",username);
                            user.put("userPhone",userphone);
                            user.put("userAddress",useraddress);
                            user.put("password",password);
                            user.put("user_id",userID);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseUser FBuser = mAuth.getCurrentUser();
                                    String userRecord = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                    FBuser.updateProfile(profileUpdates);


                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            });


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            Toast.makeText(RegisterActivity.this, "Error !"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                        }
                    }
                });

            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            sendToMain();

        }

    }

    private void sendToMain() {

        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
