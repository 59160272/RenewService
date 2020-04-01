package com.example.renewseviceqq;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TechListViewProfileActivity extends AppCompatActivity {
    private static final String TAG = "TechListViewProfileActivity";
    //ListView
    List<TechKey> showTechList = new ArrayList<>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    private TechListAdapter ShowTechListAdapter;
    TextView hideText;
    //FireStore
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private String userID;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techpflistview);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        pd = new ProgressDialog(this);
        hideText = findViewById(R.id.hideTextViewTech);
        ///initialize views
        mRecyclerView = findViewById(R.id.RecyclerView_TechPFListView);
        mRecyclerView.setHasFixedSize((true));
        ShowTechListAdapter = new TechListAdapter(this,showTechList);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        showData();


        ImageView backAccount = (ImageView) findViewById(R.id.backAccount);
        backAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showData() {
        pd.setTitle("Loading ....");
        pd.show();
        Query firstQuery = mStore.collectionGroup("userTech").whereEqualTo("techUserId",userID);
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                showTechList.clear();
                pd.dismiss();
                if (e == null) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            TechKey TechKey = doc.getDocument().toObject(TechKey.class);
                            showTechList.add(TechKey);
                            ShowTechListAdapter.notifyDataSetChanged();
                            hideText.setVisibility(View.GONE);
                        }
                        mRecyclerView.setAdapter(ShowTechListAdapter);
                    }
                }
            }
        });

    }

}
