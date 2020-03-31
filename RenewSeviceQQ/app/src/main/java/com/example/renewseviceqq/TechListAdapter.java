package com.example.renewseviceqq;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class TechListAdapter extends RecyclerView.Adapter<TechListViewHolder> {
    TechListViewProfileActivity TechListViewProfileActivity;
    public List<TechKey> showTechList;
    Context context;
    private FirebaseAuth mAuth;

    public TechListAdapter(TechListViewProfileActivity TechListViewProfileActivity, List<TechKey> showTechList){
        this.showTechList = showTechList;
        this.TechListViewProfileActivity = TechListViewProfileActivity;
    }


    @Override
    public TechListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_profiletech, parent, false);
        mAuth = FirebaseAuth.getInstance();
        context = parent.getContext();
        TechListViewHolder ListTechViewHolder = new TechListViewHolder(itemView);

        ListTechViewHolder.setOnClickListener(new TechListViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                Intent techProfileDetailActivity = new Intent(TechListViewProfileActivity, TechProfileDetailActivity.class);
                techProfileDetailActivity.putExtra("title", showTechList.get(position).getTechTitle());
                techProfileDetailActivity.putExtra("titleToolbar", showTechList.get(position).getTechTitle());
                techProfileDetailActivity.putExtra("coverImage", showTechList.get(position).getTechImage());
                techProfileDetailActivity.putExtra("description", showTechList.get(position).getTechDesc());
                techProfileDetailActivity.putExtra("address", showTechList.get(position).getTechAddress());
                techProfileDetailActivity.putExtra("category", showTechList.get(position).getTechSpinner());
                techProfileDetailActivity.putExtra("phone", showTechList.get(position).getTechPhone());
                techProfileDetailActivity.putExtra("techUserId", showTechList.get(position).getTechUserId());
                TechListViewProfileActivity.startActivity(techProfileDetailActivity);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


        return ListTechViewHolder;
    }

    @Override
    public void onBindViewHolder(TechListViewHolder holder, int i) {
        holder.mTitleTech.setText((showTechList.get(i).getTechTitle()));
        String image = showTechList.get(i).getTechImage();
        holder.techImage(image);
    }

    @Override
    public int getItemCount() {
        return showTechList.size();
    }
}
