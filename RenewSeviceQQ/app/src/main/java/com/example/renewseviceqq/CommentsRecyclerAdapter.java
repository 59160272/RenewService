package com.example.renewseviceqq;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {
    public List<Comments> commentsList;
    public Context context;

    public CommentsRecyclerAdapter(List<Comments> commentsList){

        this.commentsList = commentsList;

    }

    @NonNull
    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        String uid  = commentsList.get(position).getUser_id();
        String commentMessage = commentsList.get(position).getMessage();
        holder.setComment_message(commentMessage);

        String userName = commentsList.get(position).getUserName();
        holder.setUserName(userName);



    }

    @Override
    public int getItemCount() {
        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView comment_message, userName_Comments;
        private ImageView img_user;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setComment_message(String message) {

            comment_message = mView.findViewById(R.id.TextComments);
            comment_message.setText(message);

        }

        public void setUserName(String userName) {

            userName_Comments = mView.findViewById(R.id.CommentUsername);
            userName_Comments.setText(userName);

        }

        public void setComImage(String downloadUri) {

            img_user = mView.findViewById(R.id.avatarImageView);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.ic_stub);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
            ).into(img_user);

        }


    }
}