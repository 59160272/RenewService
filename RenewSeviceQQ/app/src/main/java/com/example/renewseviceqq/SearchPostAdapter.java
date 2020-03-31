package com.example.renewseviceqq;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.ViewHolder>  {

    public List<BlogPost> SearchPostList;
    FragmentActivity activity;
    public Context mContext;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;




    public SearchPostAdapter(FragmentActivity activity, List<BlogPost> SearchPostList){

        this.SearchPostList = SearchPostList;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_searchpost, parent, false);
        mContext = parent.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        String image = SearchPostList.get(position).getImage();
        holder.setSearchImageView(image);

        String user_id = SearchPostList.get(position).getUser_id();

        String topic = SearchPostList.get(position).getTopic();
        holder.setSearchTopic(topic);

        String spinner = SearchPostList.get(position).getAddress();
        holder.setSearchProvide(spinner);




    }


    @Override
    public int getItemCount() {
        return SearchPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private ImageView SearchImageView;
        private TextView SearchTopic, SearchProvide;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("userId", SearchPostList.get(position).getUser_id());
                    postDetailActivity.putExtra("title", SearchPostList.get(position).getTopic());
                    postDetailActivity.putExtra("ToolbarTitle", SearchPostList.get(position).getTopic());
                    postDetailActivity.putExtra("postImage", SearchPostList.get(position).getImage());
                    postDetailActivity.putExtra("description", SearchPostList.get(position).getDesc());
                    postDetailActivity.putExtra("budget", SearchPostList.get(position).getBudget());
                    postDetailActivity.putExtra("address", SearchPostList.get(position).getAddress());
                    long timestamp = (long) SearchPostList.get(position).getTimestamp().getTime();
                    postDetailActivity.putExtra("postDate", timestamp);
                    postDetailActivity.putExtra("category", SearchPostList.get(position).getCategory());
                    postDetailActivity.putExtra("phone", SearchPostList.get(position).getPhone());
                    postDetailActivity.putExtra("postid", SearchPostList.get(position).getPostid());
                    postDetailActivity.putExtra("userPhoto", SearchPostList.get(position).getUserPhoto());
                    postDetailActivity.putExtra("userName", SearchPostList.get(position).getUserName());

                    mContext.startActivity(postDetailActivity);

                }
            });

        }



        public void setSearchImageView(String downloadUri){

            SearchImageView = mView.findViewById(R.id.Post_Img);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.mipmap.ic_launcher);

            Glide.with(mContext).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
            ).into(SearchImageView);

        }

        public void setSearchTopic(String topicText){

            SearchTopic = mView.findViewById(R.id.SearchPost_Title);
            SearchTopic.setText(topicText);

        }

        public void setSearchProvide(String spinnerText){

            SearchProvide = mView.findViewById(R.id.Post_provice);
            SearchProvide.setText(spinnerText);

        }



    }



}
