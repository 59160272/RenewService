package com.example.renewseviceqq;

import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TechProfileDetailActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String userID,userID2;
    private FirebaseFirestore mStore;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    FirebaseUser firebaseUser;
    private TextView tvTechTitle,tvTechDesc,tvTechAddress,tvTechSpinner,tvToolbarTitle,tvTechPhone;
    Uri imgUser;
    private ImageView imgBack,techImgCover,userPhoto;
    private String tUserId, tTitile, tDesc, tAddress, tImageCover,tCategory,tToolbar,tPhone,tKeyId;
    private RecyclerView commentsRecyclerView;
    private List<Comments> commentsList;
    private Button sendButton;
    private EditText commentEditText;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techprofiledetail);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        userID2 = mAuth.getCurrentUser().getDisplayName();
        imgUser = mAuth.getCurrentUser().getPhotoUrl();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getInstance().getReference();
        firebaseUser = mAuth.getInstance().getCurrentUser();

        imgBack = findViewById(R.id.backArrowTech);
        techImgCover = findViewById(R.id.CoverImgTh);


        tvToolbarTitle = findViewById(R.id.techTitleToolbar);
        tvTechTitle = findViewById(R.id.titleCompany);
        tvTechDesc = findViewById(R.id.techDesc);
        tvTechAddress = findViewById(R.id.techAddress);
        tvTechPhone = findViewById(R.id.techPhone);
        tvTechSpinner = findViewById(R.id.spinnerCompany);


        userPhoto = findViewById(R.id.avatarImageView);
        sendButton = findViewById(R.id.sendButton);
        commentEditText = findViewById(R.id.commentEditText);
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

        //RecyclerView Firebase List
        commentsList = new ArrayList<>();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentsList);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsRecyclerView.setAdapter(commentsRecyclerAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tTitile = bundle.getString("title");
            tvTechTitle.setText(tTitile);
            tToolbar = bundle.getString("titleToolbar");
            tvToolbarTitle.setText(tToolbar);
            tDesc = bundle.getString("description");
            tvTechDesc.setText(tDesc);
            tAddress = bundle.getString("address");
            tvTechAddress.setText(tAddress);
            tPhone = bundle.getString("phone");
            tvTechPhone.setText(tPhone);
            tCategory = bundle.getString("category");
            tvTechSpinner.setText(tCategory);
            tImageCover = bundle.getString("coverImage");
            Glide.with(this).load(tImageCover).into(techImgCover);
            tKeyId = bundle.getString("IDtech");
            tUserId = bundle.getString("techUserId");

        }


        mStore.collection("userTech/" + tUserId + "/Comments").addSnapshotListener(TechProfileDetailActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshots.isEmpty()) {

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (e == null) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                Comments comments = doc.getDocument().toObject(Comments.class);
                                commentsList.add(comments);
                                commentsRecyclerAdapter.notifyDataSetChanged();


                            }
                            commentsRecyclerView.setAdapter(commentsRecyclerAdapter);
                        }
                    }

                }
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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String comment_message = commentEditText.getText().toString();

                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("userPhoto", imgUser);
                    commentsMap.put("userName", userID2);
                    commentsMap.put("message", comment_message);
                    commentsMap.put("user_id", userID);
                    commentsMap.put("timestamp", FieldValue.serverTimestamp());

                    mStore.collection("userTech/" + tUserId + "/Comments").add(commentsMap).addOnCompleteListener
                            (new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {

                                    if (!task.isSuccessful()) {

                                        Toast.makeText(TechProfileDetailActivity.this, "Error Posting Comment : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    } else {

                                        commentEditText.setText("");

                                    }
                                }

                            });

            }


        });



    }

    private void SendComment(){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser != null) {

                    String uname = firebaseUser.getDisplayName();
                    Uri uimg = firebaseUser.getPhotoUrl();
                    String comment_message = commentEditText.getText().toString();
                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("userPhoto", uimg);
                    commentsMap.put("userName", uname);
                    commentsMap.put("message", comment_message);
                    commentsMap.put("user_id", userID);
                    commentsMap.put("timestamp", FieldValue.serverTimestamp());

                    mStore.collection("userTech/" + tUserId + "/Comments").add(commentsMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            commentEditText.setText("");


                        }
                    });
                        /*mStore.collection("userTech/" + tUserId + "/Comments").add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (!task.isSuccessful()) {

                                    Toast.makeText(TechProfileDetailActivity.this, "Error Posting Comment : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                } else {

                                    commentEditText.setText("");

                                }
                            }

                        });*/


                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
