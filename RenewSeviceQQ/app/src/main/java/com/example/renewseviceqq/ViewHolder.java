package com.example.renewseviceqq;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView mTitleTv,mDescTv,mDate;
    ImageView mImage;
    Context mContext;
    View mView;
    public ViewHolder(@NonNull View itemView) {
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
        mTitleTv = itemView.findViewById(R.id.post_title);
        mDate = itemView.findViewById(R.id.postList_Date);
    }
    private ViewHolder.ClickListener mClickListener;
    public  interface  ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
    public void blogImage (String downloadUri){
        mImage = itemView.findViewById(R.id.postListView_Image);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(mImage).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
        ).into(mImage);
    }
    public void setTime(String date) {

        mDate = mView.findViewById(R.id.postList_Date);
        mDate.setText(date);

    }
}
