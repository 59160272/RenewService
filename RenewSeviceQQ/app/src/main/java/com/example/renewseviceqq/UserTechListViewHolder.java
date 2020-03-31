package com.example.renewseviceqq;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class UserTechListViewHolder extends RecyclerView.ViewHolder {
    TextView mTitleTech,mSpinnerTech,mDesc,mAddress,mKeywordTech;
    ImageView mImageTech,blogCommentBtn;
    View mView;
    public UserTechListViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());

                return true;
            }
        });
        mTitleTech = itemView.findViewById(R.id.ScTech_Name);
        mSpinnerTech = itemView.findViewById(R.id.ScTech_cag);
        mDesc = itemView.findViewById(R.id.ScTech_desc);
        mAddress = itemView.findViewById(R.id.ScTech_provice);
        mKeywordTech = itemView.findViewById(R.id.ScTech_Keyword);
        blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
    }
    private UserTechListViewHolder.ClickListener mClickListener;
    public  interface  ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(UserTechListViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
    public void techImage (String downloadUri){
        mImageTech = itemView.findViewById(R.id.ScTech_Img);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(mImageTech).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
        ).into(mImageTech);
    }
}
