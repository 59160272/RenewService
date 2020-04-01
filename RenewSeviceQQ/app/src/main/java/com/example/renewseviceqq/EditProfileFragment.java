package com.example.renewseviceqq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";
    private ImageView mProfilePhoto;
    private EditText userName,userPhone,userAddress;
    private TextView userEmail;
    private Uri imgUrl;
    private String downloadlink;
    private boolean isChanged = false;
    private final int PICK_IMAGE_REQUEST = 71;
    private String downloadImageUrl;
    DatabaseReference databaseIncidents;
    StorageReference mStorageRef;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage storage;
    String userID;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_editprofile, container, false);
        mProfilePhoto = view.findViewById(R.id.profile_photo);
        TextView mSave = view.findViewById(R.id.saveChanges);
        userName = view.findViewById(R.id.userfname);
        userPhone = view.findViewById(R.id.phone_number);
        userAddress = view.findViewById(R.id.description);
        userEmail = view.findViewById(R.id.femail);
        //databaseIncidents = FirebaseDatabase.getInstance().getReference("Incidents");
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();


            mStore.collection("users").document(userID).get().
                    addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().exists()) {
                                    String image = task.getResult().getString("image");
                                    String fullname = task.getResult().getString("Name");
                                    String phone = task.getResult().getString("Phone");
                                    String address = task.getResult().getString("Address");
                                    String email = task.getResult().getString("Email");

                                    userName.setText(fullname);
                                    userPhone.setText(phone);
                                    userAddress.setText(address);
                                    userEmail.setText(email);
                                    Glide.with(getActivity())
                                            .load(image)
                                            .into(mProfilePhoto);
                                }
                            } else {
                                Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        /*mStore.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    phone.setText(documentSnapshot.getString("Phone"));
                    fullname.setText(documentSnapshot.getString("Name"));
                    //profilename.setText(documentSnapshot.getString("Profilename"));
                    address.setText(documentSnapshot.getString("Address"));
                    email.setText(documentSnapshot.getString("Email"));
                }
                /*DocumentSnapshot documentSnapshot = task.getResult();
                phone.setText(documentSnapshot.getString("Phone"));
                fullname.setText(documentSnapshot.getString("Name"));
                //profilename.setText(documentSnapshot.getString("Profilename"));
                address.setText(documentSnapshot.getString("Address"));
                email.setText(documentSnapshot.getString("Email"));
            }
        });*/

        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "The user Settings are updated.", Toast.LENGTH_LONG).show();
                Fileuploader();
            }
        });
        //setProfileImage();

        ImageView backArrow = (ImageView) view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                getActivity().finish();
            }
        });
        return view;
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    /* private void uploadImage() {
         if (imgUrl != null) {
             final ProgressDialog progressDialog = new ProgressDialog(getActivity());
             progressDialog.setTitle("Editing...");
             progressDialog.show();

             final StorageReference Editproref = mStorageRef.child("images/"+ UUID.randomUUID().toString());
             Editproref.putFile(imgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     final String Uri = Editproref.getDownloadUrl().toString();
                     Map<String, Object> postMap = new HashMap<>();
                     postMap.put("mProfilePhoto", Uri);
                     mStore.collection("EditProfile").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                         @Override
                         public void onComplete(@NonNull Task<DocumentReference> task) {
                             if (task.isSuccessful()) {
                                 Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();
                             }
                         }

                     });
                     progressDialog.dismiss();
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     progressDialog.dismiss();
                     Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     downloadlink = imgUrl.toString();
                     Picasso.get().load(downloadlink).into(mProfilePhoto);
                 }
             })
                     .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {


                     double progress = (100.0 * taskSnapshot.getBytesTransferred()
                             / taskSnapshot.getTotalByteCount());
                     progressDialog.setMessage("Saving " + (int)progress + "%");
                 }
             });

         }
     }*/
    private void Fileuploader() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        final StorageReference filePath = mStorageRef.child("UserImages/" + UUID.randomUUID().toString());
        final UploadTask uploadTask = filePath.putFile(imgUrl);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                {
                    double progress
                            = (100.0
                            * taskSnapshot.getBytesTransferred()
                            / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int)progress + "%");
                }
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

                                    //Toast.makeText(ShareActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                    SaveProductInfoToDatabase();
                                }
                            }
                        });
                    }
                });
    }
    private void SaveProductInfoToDatabase()
    {

            Map<String, Object> userMap = new HashMap<>();
            //userMap.put("user_id",userID );
            //userMap.put("timestamp", FieldValue.serverTimestamp());
            userMap.put("image", downloadImageUrl);
            userMap.put("Name", userName);
            userMap.put("Phone", userPhone);
            userMap.put("Address", userAddress);

            mStore.collection("users").document(userID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {

                        Toast.makeText(getActivity(), "The user Settings are updated.", Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(getActivity(), AccountFragment.class);
                        startActivity(mainIntent);
                        getActivity().finish();

                    } else {

                        String error = task.getException().getMessage();
                        Toast.makeText(getActivity(), "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                    }


                }
            });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgUrl = data.getData();
            mProfilePhoto.setImageURI(imgUrl);
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),imgUrl);
                mProfilePhoto.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
