package com.example.renewseviceqq;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SearchTechFragment extends Fragment {
    private RecyclerView RecyclerView_TechSearch;
    private List<TechKey> techKeyList = new ArrayList<>();;
    private UserTechListAdapter UserTechListAdapter;
    ProgressDialog pd;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private Toolbar Search_toolbar;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_searchtech, container, false);
        RecyclerView_TechSearch = view.findViewById(R.id.RecyclerView_TechSearch);
        UserTechListAdapter = new UserTechListAdapter(getActivity(), techKeyList);
        RecyclerView_TechSearch.setLayoutManager(new LinearLayoutManager(container.getContext()));
        RecyclerView_TechSearch.setAdapter(UserTechListAdapter);
        pd = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();



        /*Search_toolbar = (Toolbar) view.findViewById(R.id.Search_toolbar);
        if (Search_toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(Search_toolbar);
        }
        Search_toolbar.setTitle(null);*/


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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchUserTech(s);
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
        Query firstQuery = mStore.collection("userTech");
        firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                techKeyList.clear();
                pd.dismiss();
                if (e == null) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            TechKey TechKey = doc.getDocument().toObject(TechKey.class);
                            techKeyList.add(TechKey);
                            UserTechListAdapter.notifyDataSetChanged();
                        }
                        RecyclerView_TechSearch.setAdapter(UserTechListAdapter);
                    }
                }

            }
        });

    }

    private void searchUserTech(final String searchQuery) {
        pd.setTitle("Searching....");
        pd.show();
        Query allUsersTech = mStore.collection("userTech");
        allUsersTech.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                techKeyList.clear();
                pd.dismiss();
                if (e == null) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            TechKey techKey = doc.getDocument().toObject(TechKey.class);
                            if(techKey.getTechTitle().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            techKey.getTechSpinner().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            techKey.getTechAddress().toLowerCase().contains(searchQuery.toLowerCase())||
                            techKey.getTechKeyword().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            techKey.getTechDesc().toLowerCase().contains(searchQuery.toLowerCase())){
                                techKeyList.add(techKey);
                            }

                            UserTechListAdapter.notifyDataSetChanged();
                        }
                        RecyclerView_TechSearch.setAdapter(UserTechListAdapter);
                    }
                }
            }

        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_search_btn){
            Toast.makeText(getActivity(),"Search",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
