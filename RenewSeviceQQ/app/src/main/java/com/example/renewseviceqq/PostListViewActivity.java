package com.example.renewseviceqq;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostListViewActivity extends AppCompatActivity {
    private static final String TAG = "PostListViewActivity";
    List<ShowPost> showPostList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recyclerview
    RecyclerView.LayoutManager layoutManager;

    //FireStore
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private PostAdapter PostAdapter;
    private String userID;

    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlistdetail);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        pd = new ProgressDialog(this);

       ///initialize views
        mRecyclerView = findViewById(R.id.RecyclerView_PostListView);

        //set recycler view properties
        mRecyclerView.setHasFixedSize((true));
        PostAdapter = new PostAdapter(PostListViewActivity.this,showPostList);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        showData();


        ImageView backAccount = (ImageView) findViewById(R.id.backAccount);
        backAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to 'AccountFragment'");
                finish();
            }
        });
    }

    private void showData() {
        pd.setTitle("Loading ....");
        pd.show();
        Query firstQuery = mStore.collectionGroup("Posts").whereEqualTo("user_id",userID);

        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                showPostList.clear();
                pd.dismiss();
                if (e == null) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            ShowPost showPost = doc.getDocument().toObject(ShowPost.class);
                            showPostList.add(showPost);
                            PostAdapter.notifyDataSetChanged();
                        }
                        mRecyclerView.setAdapter(PostAdapter);
                    }
                }
            }
        });


    }

    public void deleteData (int index){
        pd.setTitle("Deleting .....");
        pd.show();

        mStore.collection("Posts").document(showPostList.get(index).getPostid()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(PostListViewActivity.this, "ลบสำเร็จ...", Toast.LENGTH_SHORT).show();
                        showData();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(PostListViewActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
