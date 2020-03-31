package com.example.renewseviceqq;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UserTechListAdapter extends RecyclerView.Adapter<UserTechListViewHolder> {

    public List<TechKey> techKeyList;
    FragmentActivity activity;
    Context context;
    private FirebaseAuth mAuth;

    public UserTechListAdapter(FragmentActivity activity, List<TechKey> techKeyList){
        this.techKeyList = techKeyList;
        this.activity = activity;

    }


    @Override
    public UserTechListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_list_view, parent, false);
        mAuth = FirebaseAuth.getInstance();
        context = parent.getContext();
        UserTechListViewHolder userTechListViewHolder = new UserTechListViewHolder(itemView);

        userTechListViewHolder.setOnClickListener(new UserTechListViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                Intent techProfileDetailActivity = new Intent(context, TechProfileDetailActivity.class);
                techProfileDetailActivity.putExtra("title", techKeyList.get(position).getTechTitle());
                techProfileDetailActivity.putExtra("titleToolbar", techKeyList.get(position).getTechTitle());
                techProfileDetailActivity.putExtra("coverImage", techKeyList.get(position).getTechImage());
                techProfileDetailActivity.putExtra("description", techKeyList.get(position).getTechDesc());
                techProfileDetailActivity.putExtra("address", techKeyList.get(position).getTechAddress());
                techProfileDetailActivity.putExtra("category", techKeyList.get(position).getTechSpinner());
                techProfileDetailActivity.putExtra("phone", techKeyList.get(position).getTechPhone());
                techProfileDetailActivity.putExtra("techUserId", techKeyList.get(position).getTechUserId());
                context.startActivity(techProfileDetailActivity);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


        return userTechListViewHolder;
    }

    @Override
    public void onBindViewHolder(UserTechListViewHolder holder, int i) {
        final String userTechId = techKeyList.get(i).getTechUserId();
        final String currentUserId = mAuth.getCurrentUser().getUid();
        holder.mTitleTech.setText((techKeyList.get(i).getTechTitle()));
        holder.mAddress.setText((techKeyList.get(i).getTechAddress()));
        holder.mSpinnerTech.setText((techKeyList.get(i).getTechSpinner()));
        holder.mDesc.setText((techKeyList.get(i).getTechDesc()));
        holder.mKeywordTech.setText((techKeyList.get(i).getTechKeyword()));
        String image = techKeyList.get(i).getTechImage();
        holder.techImage(image);

        holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentIntent = new Intent(context, TechProfileDetailActivity.class);
                commentIntent.putExtra("IDtech", userTechId);
                context.startActivity(commentIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return techKeyList.size();
    }
}
