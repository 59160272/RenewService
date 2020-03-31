package com.example.renewseviceqq;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.Compressor;

import static android.app.Activity.RESULT_OK;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileFragment";
    private CircleImageView mProfilePhoto;

    private EditText userName,userPhone,userAddress;
    private TextView userEmail;
    private Uri imgUrl;
    private boolean isChanged = false;
    private final int PICK_IMAGE_REQUEST = 71;
    private String downloadImageUrl;


    StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private FirebaseStorage storage;
    private String userID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_editprofile);
        mProfilePhoto = findViewById(R.id.profile_photo);
        TextView mSave =  findViewById(R.id.saveChanges);
        userName = findViewById(R.id.userfname);
        userPhone = findViewById(R.id.phone_number);
        userAddress = findViewById(R.id.description);
        userEmail = findViewById(R.id.femail);
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getInstance().getReference("UserImages");
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        ImageView backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                finish();

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
                                String email = task.getResult().getString("userEmail");

//                          imgUrl = Uri.parse(image);
                                userName.setText(fullname);
                                userPhone.setText(phone);
                                userAddress.setText(address);
                                userEmail.setText(email);
                                RequestOptions placeholderRequest = new RequestOptions();
                                placeholderRequest.placeholder(R.mipmap.ic_launcher);
                                Glide.with(EditProfileActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(mProfilePhoto);
                            }
                        } else {

                            String error = task.getException().getMessage();
                            Toast.makeText(EditProfileActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                        }
                    }
                });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname = userName.getText().toString();
                final String phone = userPhone.getText().toString();
                final String address = userAddress.getText().toString();
                final String email = userEmail.getText().toString();

                if(imgUrl != null) {
                    final StorageReference filePath = mStorageRef.child("UserImages/" + UUID.randomUUID().toString());
                    final UploadTask uploadTask = filePath.putFile(imgUrl);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    // Toast.makeText(ShareActivity.this, "uploaded Successfully.", Toast.LENGTH_LONG).show();
                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {


                                                throw task.getException();
                                            }

                                            downloadImageUrl = filePath.getDownloadUrl().toString();
                                            return filePath.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                downloadImageUrl = task.getResult().toString();

                                    }
                                            storeFirestore(null, fullname,phone,address,email);
                                }
                            });
                        }
                    });
                 }

            }
        });

        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);

            }
        });

    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String fullname, String phone, String address, String email)
    {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user_id",userID );
        userMap.put("userName",fullname);
        userMap.put("userPhone",phone);
        userMap.put("userAddress",address);
        userMap.put("userEmail",email);
        userMap.put("userPhoto", downloadImageUrl);

        mStore.collection("users").document(userID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "แก้ไขบัญชีผู้ใช้สำเร็จ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));


                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(EditProfileActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            imgUrl = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                imgUrl);
                mProfilePhoto.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

}
