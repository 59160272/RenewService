package com.example.renewseviceqq;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
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

public class SearchPostFragment extends Fragment {
    private RecyclerView RecyclerView_PostSearch;
    private List<BlogPost> SearchPostList = new ArrayList<>();;
    private SearchPostAdapter SearchPostAdapter;
    ProgressDialog pd;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private Toolbar SearchPost_toolbar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchpost, container, false);
        RecyclerView_PostSearch = view.findViewById(R.id.RecyclerView_PostSearch);
        SearchPostAdapter = new SearchPostAdapter(getActivity(), SearchPostList);
        RecyclerView_PostSearch.setLayoutManager(new LinearLayoutManager(container.getContext()));
        RecyclerView_PostSearch.setAdapter(SearchPostAdapter);
        pd = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

       /*SearchPost_toolbar = (Toolbar) view.findViewById(R.id.SearchPost_toolbar);

            ((AppCompatActivity)getActivity()).setSupportActionBar(SearchPost_toolbar);

        SearchPost_toolbar.setTitle("Posts");*/

        showData();

        return  view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);

        //SearchView
        MenuItem item = menu.findItem(R.id.action_search_btn);
        SearchView searchPostView = (SearchView) MenuItemCompat.getActionView(item);
        searchPostView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchPost(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }
    private void showData() {
        pd.setTitle("Loading....");
        pd.show();
        Query firstQuery = mStore.collection("Posts");
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                SearchPostList.clear();
                pd.dismiss();
                if (e == null) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            BlogPost BlogPost = doc.getDocument().toObject(BlogPost.class);
                            SearchPostList.add(BlogPost);
                            SearchPostAdapter.notifyDataSetChanged();
                        }
                        RecyclerView_PostSearch.setAdapter(SearchPostAdapter);
                    }
                }
            }
        });

    }
    private void searchPost(final String searchQuery) {
        pd.setTitle("Searching....");
        pd.show();
        Query allUsersTech = mStore.collection("Posts");
        allUsersTech.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                SearchPostList.clear();
                pd.dismiss();
                if (e == null) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                            if(blogPost.getTopic().toLowerCase().contains(searchQuery.toLowerCase()) ||
                                    blogPost.getCategory().toLowerCase().contains(searchQuery.toLowerCase()) ||
                                    blogPost.getDesc().toLowerCase().contains(searchQuery.toLowerCase())||
                                    blogPost.getAddress().toLowerCase().contains(searchQuery.toLowerCase())){
                                SearchPostList.add(blogPost);
                            }

                            SearchPostAdapter.notifyDataSetChanged();
                        }
                        RecyclerView_PostSearch.setAdapter(SearchPostAdapter);
                    }
                }
            }

        });

    }


}
