package com.example.renewseviceqq;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class TechListViewHolder extends RecyclerView.ViewHolder {
    TextView mTitleTech,mSpinnerTech;
    ImageView mImageTech;
    View mView;
    public TechListViewHolder(@NonNull View itemView) {
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
        mTitleTech = itemView.findViewById(R.id.ListViewTech);
        mSpinnerTech = itemView.findViewById(R.id.ListView_cag);

    }
    private TechListViewHolder.ClickListener mClickListener;
    public  interface  ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(TechListViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
    public void techImage (String downloadUri){
        mImageTech = itemView.findViewById(R.id.image_view);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(mImageTech).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
        ).into(mImageTech);
    }
}
