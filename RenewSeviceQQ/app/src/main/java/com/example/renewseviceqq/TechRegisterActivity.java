package com.example.renewseviceqq;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static androidx.constraintlayout.solver.widgets.ConstraintWidget.GONE;

public class TechRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "TechRegisterActivity";
    //Firebase
    StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private FirebaseStorage storage;
    private String userID;

    //Upload Img
    private String downloadImageUrl,srtSort;
    private Uri techUrl;
    private static final int PICK_IMAGE_REQUEST = 22 ;

    //TechKey
    private EditText thTitle,thDesc,thAddress,thKeyword,thPhone;
    private ImageView thImg,backArrowTech,mRegisterTech;
    private RadioButton thCheckNiti,thCheckPeople,thSortOption;
    private Spinner thSpinner;
    private Button thButton;
    private ProgressDialog progressDialog;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registertech);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getInstance().getReference();

        //KeyTech
        thTitle = findViewById(R.id.tech_title);
        thDesc = findViewById(R.id.tech_desception);
        thAddress = findViewById(R.id.tech_address);
        thImg = findViewById(R.id.tech_img);
        backArrowTech = findViewById(R.id.backArrowTech);
        thCheckNiti = findViewById(R.id.checkbox_niti);
        thCheckPeople = findViewById(R.id.checkbox_people);
        thSpinner = findViewById(R.id.spinnerTech);
        thKeyword = findViewById(R.id.tech_keyword);
        thButton = findViewById(R.id.btnPostTech);
        thPhone = findViewById(R.id.tech_phone);
        radioGroup = findViewById(R.id.radioGroup);
        thSortOption = findViewById(R.id.checkbox_niti);
        mRegisterTech = findViewById(R.id.ImgRegisterTech);
        srtSort = "บุคลธรรมดา";
        //Back Activity
        backArrowTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to AccountFragment");
                finish();
            }
        });

        //SpinnerTech
        ArrayAdapter<CharSequence> TechAdapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        TechAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thSpinner.setAdapter(TechAdapter);
        thSpinner.setOnItemSelectedListener(this);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                thSortOption = radioGroup.findViewById(i);
                switch (i){
                    case R.id.checkbox_niti:
                        srtSort = thSortOption.getText().toString();
                        break;
                    case R.id.checkbox_people:
                        srtSort = thSortOption.getText().toString();
                        break;

                        default:
                }

            }
        });

        //ChooseImg
        thImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseImage();
            }
        });

        //Upload Img
        thButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FileUploader();
            }
        });
    }
    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();

        thSortOption = findViewById(radioId);

        Toast.makeText(this, "เลือก: " + thSortOption.getText(),
                Toast.LENGTH_SHORT).show();
    }

    private void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void FileUploader() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("กำลังบันทึก...");
        progressDialog.show();


            final StorageReference filePath = mStorageRef.child("Tech_Images/" + UUID.randomUUID().toString());
            final UploadTask uploadTask = filePath.putFile(techUrl);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(TechRegisterActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                ""
                                        + (int) progress + "%");
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

                                        TechInfoToDatabase();
                                    }
                                }
                            });
                        }
                    });

    }

    private void TechInfoToDatabase() {
        final String title = thTitle.getText().toString();
        final String desc = thDesc.getText().toString();
        final String radio = thSortOption.getText().toString();
        final String address = thAddress.getText().toString();
        final String spinnerTh = thSpinner.getSelectedItem().toString();
        final String techUserId = UUID.randomUUID().toString();
        final String keyword = thKeyword.getText().toString();
        final String phone = thPhone.getText().toString();
        Map<String, Object> TechMap = new HashMap<>();
        TechMap.put("techTitle", title);
        TechMap.put("techDesc", desc);
        TechMap.put("techAddress", address);
        TechMap.put("techPhone", phone);
        TechMap.put("techSpinner", spinnerTh);
        TechMap.put("techKeyword", keyword);
        TechMap.put("techRadio",radio);
        TechMap.put("IDtech", techUserId);
        TechMap.put("techUserId",userID );
        TechMap.put("techImage", downloadImageUrl);

       // mStore.collection("users").document(userID).collection("userTech")
        mStore.collection("userTech").document(userID)
                .set(TechMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(TechRegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(TechRegisterActivity.this, "สร้างบัญชีผู้ใช้ช่างสำเร็จ", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(TechRegisterActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            techUrl = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                techUrl);
                thImg.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
