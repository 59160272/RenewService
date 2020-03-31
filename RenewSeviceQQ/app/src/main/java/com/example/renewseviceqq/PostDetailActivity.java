package com.example.renewseviceqq;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PostDetailActivity extends AppCompatActivity {
    private static final String TAG = "PostDetailActivity";
    Context mContext;
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore mStore;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    ImageView BackArror;
    TextView txtPostTitle,txtCategory,txtPostDesc,txtAddress,txtBudget,txtTitle,txtTimeStamp,txtPhone;
    ImageView imgUserPost,imgPost;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String pId, pTitile, pDesc, pAddress, pBudget, pImagePost, pDate, pUserName,postCategory,pToolbarTitile,pPhone;
    Dialog myDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getInstance().getReference();




        BackArror = findViewById(R.id.backArrowPV);
        imgPost = findViewById(R.id.blog_image);
        imgUserPost = findViewById(R.id.blog_user_image);
        txtPhone = findViewById(R.id.post_phone);
        txtPostTitle = findViewById(R.id.tvPost);
        txtCategory = findViewById(R.id.post_category);
        txtPostDesc = findViewById(R.id.post_desc);
        txtAddress = findViewById(R.id.tvPost_address);
        txtBudget = findViewById(R.id.post_budget);
        txtTitle = findViewById(R.id.post_title);
        txtTimeStamp = findViewById(R.id.post_timestamp);


        myDialog = new Dialog(this);
        Button view_btn = findViewById(R.id.view_btn);
        myDialog.setContentView(R.layout.dialog_contact);
        final TextView dialog_name = (TextView) myDialog.findViewById(R.id.dialog_name);
        final TextView dialog_phone = (TextView) myDialog.findViewById(R.id.dialog_phone);

        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Bundle bundle = getIntent().getExtras();
                if(bundle != null) {
                    pUserName = bundle.getString("userName");
                    dialog_name.setText("คุณ "+pUserName);

                    pPhone = bundle.getString("phone");
                    dialog_phone.setText(pPhone);
                }

                myDialog.show();
            }
        });




        BackArror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
                // ...
            }
        };


            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                pToolbarTitile = bundle.getString("ToolbarTitle");
                txtPostTitle.setText(pToolbarTitile);
                pDesc = bundle.getString("description");
                txtPostDesc.setText(pDesc);
                pImagePost = bundle.getString("postImage");
                Glide.with(this).load(pImagePost).into(imgPost);
                pBudget = bundle.getString("budget");
                txtBudget.setText(pBudget+" บาท");
                pAddress = bundle.getString("address");
                txtAddress.setText(pAddress);
                pDate = timestampToString(bundle.getLong("postDate"));
                txtTimeStamp.setText(pDate);
                postCategory = bundle.getString("category");
                txtCategory.setText("ประเภท: "+postCategory);
                pPhone = bundle.getString("phone");
                txtPhone.setText(pPhone);
                pTitile = bundle.getString("title");
                txtTitle.setText(pTitile);

            }



    }


    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;


    }


}
