package com.example.renewseviceqq;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.renewseviceqq.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {
    Toolbar toolbar;
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list;
    ImageView BackArror;
    private FloatingActionButton addPostBtn,home,search;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private BlogRecyclerAdapter blogRecyclerAdapter;
    Float translationY = 100f;
    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    TextView tvSearch,tvPost;
    boolean isOpen=false;
   // OvershootInterpolator interpolator = new OvershootInterpolator();
    private static final String TAG = "HomeFragment";
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        blog_list = new ArrayList<>();
        blog_list_view = view.findViewById(R.id.blog_list_view);

        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        mStore = FirebaseFirestore.getInstance();

        blogRecyclerAdapter = new BlogRecyclerAdapter(blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        blog_list_view.setAdapter(blogRecyclerAdapter);
        tvSearch = view.findViewById(R.id.search_text);
        tvPost = view.findViewById(R.id.post_text);
        home = view.findViewById(R.id.fabhome);
        addPostBtn = view.findViewById(R.id.add_post_btn);
        search = view.findViewById(R.id.fabsearch);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpen){
                    openMenu();
                    tvSearch.setVisibility(View.VISIBLE);
                    tvPost.setVisibility(View.VISIBLE);
                }else {
                    closeMenu();
                    tvSearch.setVisibility(View.INVISIBLE);
                    tvPost.setVisibility(View.INVISIBLE);
                }
            }
        });



        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(newPostIntent);
            }
        });

       search.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
               startActivity(searchIntent);
           }
       });


        blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                if (reachedBottom) {

                    loadMorePost();

                }

            }
        });

        Query firstQuery = mStore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e == null) {
                    if (!documentSnapshots.isEmpty()) {
                        if (isFirstPageFirstLoad) {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                            blog_list.clear();

                        }
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                                if (isFirstPageFirstLoad) {

                                    blog_list.add(blogPost);

                                } else {

                                    blog_list.add(0, blogPost);

                                }

                                blogRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        isFirstPageFirstLoad = false;

                    }
                }
            }

        });



        ImageView profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent  = new Intent(getActivity(), AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

        /*addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent = new Intent(getActivity(), NewPostActivity.class);
                startActivity(newPostIntent);
            }
        });*/



        return view;
    }

    private void openMenu() {
        isOpen=true;
        addPostBtn.animate().translationY(-getResources().getDimension(R.dimen.stan_55));
        search.animate().translationY(-getResources().getDimension(R.dimen.stan_105));

        tvPost.animate().translationY(-getResources().getDimension(R.dimen.stan_55));
        tvSearch.animate().translationY(-getResources().getDimension(R.dimen.stan_105));

    }
    private void closeMenu() {
        isOpen=false;
        addPostBtn.animate().translationY(0);
        search.animate().translationY(0);

        tvPost.animate().translationY(0);
        tvSearch.animate().translationY(0);


    }

    public void loadMorePost(){

        if(mAuth.getCurrentUser() != null) {

            Query nextQuery = mStore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(3);

            nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                                blog_list.add(blogPost);

                                blogRecyclerAdapter.notifyDataSetChanged();
                            }

                        }
                    }

                }
            });

        }

    }




}
