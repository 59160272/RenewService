package com.example.renewseviceqq;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    public List<BlogPost> users_list;
    FirebaseAuth mAuth;
    public Context mContext;
    Dialog myDialog;

    public UsersAdapter(Context mContext, List<BlogPost> users_list){

        this.mContext = mContext;
        this.users_list = users_list;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_info_user_post, parent, false);
        //ViewHolder vHolder = new ViewHolder(v);
        mAuth = FirebaseAuth.getInstance();
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return users_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private LinearLayout item_contact;
        private Button view_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            item_contact = (LinearLayout) itemView.findViewById(R.id.contact_item_id);
            view_btn = itemView.findViewById(R.id.view_btn);

        }
    }
}
