package com.example.renewseviceqq;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>  {

    public List<BlogPost> blog_list;
    public Context mContext;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;




    public BlogRecyclerAdapter(List<BlogPost> blog_list){

        this.blog_list = blog_list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
        mContext = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String desc_data = blog_list.get(position).getDesc();
        holder.setDescText(desc_data);

        String image = blog_list.get(position).getImage();
        holder.setBlogImage(image);

        String user_id = blog_list.get(position).getUser_id();

        String topic = blog_list.get(position).getTopic();
        holder.setTopicText(topic);


        long millisecond = blog_list.get(position).getTimestamp().getTime();
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
        holder.setTime(dateString);

    }


    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView descView;
        private ImageView blogImageView;
        private TextView blogTopic;
        private TextView blogDate;

        private TextView blogUserName;
        private CircleImageView blogUserImage;
        private Button view_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("userId", blog_list.get(position).getUser_id());
                    postDetailActivity.putExtra("title", blog_list.get(position).getTopic());
                    postDetailActivity.putExtra("ToolbarTitle", blog_list.get(position).getTopic());
                    postDetailActivity.putExtra("postImage", blog_list.get(position).getImage());
                    postDetailActivity.putExtra("description", blog_list.get(position).getDesc());
                    postDetailActivity.putExtra("budget", blog_list.get(position).getBudget());
                    postDetailActivity.putExtra("address", blog_list.get(position).getAddress());
                    long timestamp = (long) blog_list.get(position).getTimestamp().getTime();
                    postDetailActivity.putExtra("postDate", timestamp);
                    postDetailActivity.putExtra("category", blog_list.get(position).getCategory());
                    postDetailActivity.putExtra("phone", blog_list.get(position).getPhone());
                    postDetailActivity.putExtra("postid", blog_list.get(position).getPostid());
                    postDetailActivity.putExtra("userName", blog_list.get(position).getUserName());

                    mContext.startActivity(postDetailActivity);

                }
            });

        }

        public void setDescText(String descText) {

            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);
        }

        public void setBlogImage(String downloadUri){

            blogImageView = mView.findViewById(R.id.blog_image);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);

            Glide.with(mContext).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
            ).into(blogImageView);

        }

        public void setTopicText(String topicText){

            blogTopic = mView.findViewById(R.id.blog_topic);
            blogTopic.setText(topicText);

        }

        public void setTime(String date) {

            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }

    }



}
