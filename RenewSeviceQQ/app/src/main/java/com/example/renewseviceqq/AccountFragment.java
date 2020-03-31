package com.example.renewseviceqq;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment {
    Toolbar toolbar,toolbar2;
    private ProgressBar mProgressBar;
    TextView userName, email, profilename, userPhone, userAddress;
    Button btnPostList,btnViewPf;
    ImageView mProfilePhoto,mRegisterTech;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;
    private PostListViewActivity postListViewFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        userName = view.findViewById(R.id.tvName);
        mProfilePhoto = view.findViewById(R.id.profile_photo);
        mRegisterTech = view.findViewById(R.id.ImgRegisterTech);
        userAddress = view.findViewById(R.id.description);
        userPhone = view.findViewById(R.id.phoneweb);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();


        btnPostList = view.findViewById(R.id.listAnnBtn);
        btnPostList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostListViewActivity.class);
                startActivity(intent);
            }
        });
        btnViewPf = view.findViewById(R.id.btnViewPfTech);
        btnViewPf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TechListViewProfileActivity.class);
                startActivity(intent);
            }
        });

        mRegisterTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TechRegisterActivity.class);
                startActivity(intent);
            }
        });

        //setupToolbar();

        TextView editProfile = view.findViewById(R.id.textEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG,"Onclick: navigating to "+ mContext.getString(R.string.edit_profile_fragment));
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra(getString(R.string.calling_activity),getString(R.string.edit_profile_fragment));
                startActivity(intent);
            }
        });



            mStore.collection("users").document(userID).get().
                    addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    String image = task.getResult().getString("userPhoto");
                                    String fullname = task.getResult().getString("userName");
                                    String phone = task.getResult().getString("userPhone");
                                    String address = task.getResult().getString("userAddress");


                                    userName.setText(fullname);
                                    userPhone.setText(phone);
                                    userAddress.setText(address);
                                    RequestOptions requestOptions = new RequestOptions();
                                    requestOptions.placeholder(R.mipmap.ic_launcher);

                                    Glide.with(getActivity()).applyDefaultRequestOptions(requestOptions).load(image).thumbnail(
                                    ).into(mProfilePhoto);
                                    /*Glide.with(getActivity())
                                            .load(image)
                                            .into(mProfilePhoto);*/
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


        toolbar = (Toolbar)view.findViewById(R.id.profileToolBar);
        //setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.profileMenu:
                }
                return false;
            }
        });

        ImageView profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent  = new Intent(getActivity(), AccountSettingsActivity.class);
                startActivity(intent);
            }
        });





        return view;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode  == RESULT_OK) {
            switch (requestCode) {

                case 1:
                    mRegisterTech.setVisibility(View.GONE);
            }

        }
    }


}
