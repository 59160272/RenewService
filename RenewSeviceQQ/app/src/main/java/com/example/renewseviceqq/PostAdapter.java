package com.example.renewseviceqq;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<ViewHolder> {
    PostListViewActivity PostListViewActivity;
    public List<ShowPost> showPostList;
    ProgressDialog pd;
    Context context;
    private FirebaseAuth mAuth;


    public PostAdapter(PostListViewActivity PostListViewActivity, List<ShowPost> showPostList) {
        this.showPostList = showPostList;
        this.PostListViewActivity = PostListViewActivity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.showpostbyuser, parent, false);
        context = parent.getContext();
        mAuth = FirebaseAuth.getInstance();
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                //this will be called when user click item
                String title = showPostList.get(position).getTopic();
                Toast.makeText(PostListViewActivity,title,Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(PostListViewActivity);
                String[] options = {"ดูใบประกาศ", "แก้ไขใบประกาศ", "ลบ"};
                //Options to display
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == 0) {
                            //View Post Click
                            Intent postDetailActivity = new Intent(PostListViewActivity, PostDetailActivity.class);
                            //postDetailActivity.putExtra("userId", showPostList.get(position).getUser_id());
                            postDetailActivity.putExtra("title", showPostList.get(position).getTopic());
                            postDetailActivity.putExtra("ToolbarTitle", showPostList.get(position).getTopic());
                            postDetailActivity.putExtra("postImage", showPostList.get(position).getImage());
                            postDetailActivity.putExtra("description", showPostList.get(position).getDesc());
                            postDetailActivity.putExtra("budget", showPostList.get(position).getBudget());
                            postDetailActivity.putExtra("address", showPostList.get(position).getAddress());
                            postDetailActivity.putExtra("phone", showPostList.get(position).getPhone());
                            long timestamp = (long) showPostList.get(position).getTimestamp().getTime();
                            postDetailActivity.putExtra("postDate", timestamp);
                            postDetailActivity.putExtra("category", showPostList.get(position).getCategory());
                            postDetailActivity.putExtra("userName", showPostList.get(position).getUserName());
                            PostListViewActivity.startActivity(postDetailActivity);
                        }
                        if (which == 1) {
                            //Update Post Click
                            String title = showPostList.get(position).getTopic();
                            String description = showPostList.get(position).getDesc();
                            String budget = showPostList.get(position).getBudget();
                            String category = showPostList.get(position).getCategory();
                            String postImage = showPostList.get(position).getImage();
                            String address = showPostList.get(position).getAddress();
                            String id = showPostList.get(position).getPostid();
                            String phone = showPostList.get(position).getPhone();

                            Intent intent = new Intent(PostListViewActivity, UpdatePostActivity.class);
                            intent.putExtra("pId", id);
                            intent.putExtra("pCategory", category);
                            intent.putExtra("pTitle", title);
                            intent.putExtra("pDes", description);
                            intent.putExtra("pBudget", budget);
                            intent.putExtra("pPhone", phone);
                            intent.putExtra("pImage", postImage);
                            intent.putExtra("pAddress", address);


                            PostListViewActivity.startActivity(intent);
                        }
                        if (which == 2) {
                            //Delete Post Click
                            PostListViewActivity.deleteData(position);
                        }

                    }

                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                //this will be called when user click item
                //Create AlertDialog
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //String user_id = showPostList.get(position).getUser_id();
        viewHolder.mTitleTv.setText((showPostList.get(i).getTopic()));
        String image = showPostList.get(i).getImage();
        viewHolder.blogImage(image);

        long millisecond = showPostList.get(i).getTimestamp().getTime();
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
        viewHolder.setTime(dateString);
    }
    @Override
    public int getItemCount() {
        return showPostList.size();
    }

}
