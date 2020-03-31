package com.example.renewseviceqq;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class UpdatePostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    private static final String TAG = "NewPostActivity";
    private FirebaseAuth mAuth;
    private String userID;
    private FirebaseFirestore mStore;
    StorageReference mStorageRef;
    FirebaseStorage storage;

    private static final int PICK_IMAGE_REQUEST = 22 ;
    private Context mContext = UpdatePostActivity.this;
    private EditText pxpPostTitle,pxpPostDesc,pxpAddress,pxpBudget,pxpPhone;
    private ImageView findImg;
    private Uri imgUrl;
    private boolean isChanged = false;
    private GoogleMap mMap;
    private Spinner spinner;
    private Button btnPost;
    private EditText newTopic,newDesception,newBudget,newAddress,newPhone;
    private ProgressDialog progressDialog;
    private String downloadImageUrl;
    private TextView dateText,goMaps;
    Location currentLocation;
    private static final int LOCATION_REQUEST_CODE = 100;
    FusedLocationProviderClient locClient;
    double CurrentLat = 0.0;
    double CurrentLng = 0.0;

    LatLng CurrentPosition;
    String strPosName;
    String postId, postTitle, postDesc, postAddress, postBudget,postImagePost,postCategory,postDate,postPhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        spinner = findViewById(R.id.spinner1);
        findImg = findViewById(R.id.input_img);
        btnPost = findViewById(R.id.btnPost);

       newTopic = findViewById(R.id.input_topic);
        newDesception = findViewById(R.id.input_et);
        newBudget = findViewById(R.id.input_budget);
        newAddress = findViewById(R.id.input_jobad);
        newPhone = findViewById(R.id.input_phone);


        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getInstance().getReference();
        final CollectionReference query = mStore.collection("Post");

        pxpPostTitle = findViewById(R.id.input_topic);
        pxpPostDesc = findViewById(R.id.input_et);
        pxpAddress = findViewById(R.id.input_jobad);
        pxpBudget = findViewById(R.id.input_budget);
        pxpPhone = findViewById(R.id.input_phone);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = getIntent().getExtras();
                if(bundle1 != null){
                    String id = postId;
                    String category = spinner.getSelectedItem().toString();
                    String topic = pxpPostTitle.getText().toString().trim();
                    String desc = pxpPostDesc.getText().toString().trim();
                     String budget = pxpBudget.getText().toString().trim();
                    String phone = pxpPhone.getText().toString().trim();
                    String address = pxpAddress.getText().toString().trim();
                    updatePost(id,phone,address,desc,topic,budget,category);
                }
            }
        });
        ///------------------------------------MAPS-------------------------------------------\\\
        initMap();
        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(UpdatePostActivity.this);
        if (status == ConnectionResult.SUCCESS) {
            if (ActivityCompat.checkSelfPermission(UpdatePostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                getCurrentPosition();
                ActivityCompat.requestPermissions(UpdatePostActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
        ///------------------------------------UPLOAD_IMG--------------------------------------\\\
        /*findImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseImage();

            }
        });*/

        //setup the backarrow for navigating back to "MainActivity"------------------------------
        ImageView BackArrowfind = (ImageView) findViewById(R.id.backArrowfind);
        BackArrowfind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating back to 'AccountFragment'");
                finish();
            }
        });

        ///-------------- Spinner ------------------\\\\
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        ///-------------- Spinner ------------------\\\\

        ///-------------- Calender ------------------\\\



        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            postId = bundle.getString("pId");

            postTitle = bundle.getString("pTitle");
            pxpPostTitle.setText(postTitle);

            postDesc = bundle.getString("pDes");
            pxpPostDesc.setText(postDesc);

            postImagePost = bundle.getString("pImage");
            Glide.with(this).load(postImagePost).into(findImg);

            postBudget = bundle.getString("pBudget");
            pxpBudget.setText(postBudget);

            postPhone = bundle.getString("pPhone");
            pxpPhone.setText(postPhone);

            postAddress = bundle.getString("pAddress");
            pxpAddress.setText(postAddress);


        }
    }

    private void updatePost(final String id, final String topic, final String desc, final String budget, final String address, final String category
    ,final  String phone) {

                                UpdateProductInfoToDatabase(id, address,budget,category,desc,phone,topic);

    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

   private void UpdateProductInfoToDatabase(
                                            String id, String topic, String desc, String budget, String address, String category ,String phone){
        //final String postid = UUID.randomUUID().toString();
       //        //final String key = storage.getKey();
       // Map<String, Object> postMap = new HashMap<>();
        /*postMap.put("postid", id);
        postMap.put("category", category);
        postMap.put("topic", topic);
        postMap.put("desc", desc);
        postMap.put("budget", budget);
        postMap.put("phone", phone);
        postMap.put("address", address);
        postMap.put("user_id",userID );
        postMap.put("timestamp", FieldValue.serverTimestamp());*/
        //postMap.put("image", downloadImageUrl);
        mStore.collection("Posts").document(id).update("topic",topic,"category",category,
        "desc",desc,"budget",budget,"phone",phone,"address",address/*,"image",downloadImageUrl*/).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(UpdatePostActivity.this, PostListViewActivity.class);
                    startActivity(intent);
                    Toast.makeText(UpdatePostActivity.this, "แก้ไขโพสสำเร็จ", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(UpdatePostActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

  /*  private void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }*/
    private void reqAccessLocation() {
        ActivityCompat.requestPermissions(UpdatePostActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
    }
    private void getCurrentPosition() {
        locClient = LocationServices.getFusedLocationProviderClient(UpdatePostActivity.this);
        int locPermission = ContextCompat.checkSelfPermission(UpdatePostActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (locPermission == PackageManager.PERMISSION_GRANTED) {
            locClient.getLastLocation().addOnSuccessListener(UpdatePostActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!= null) {
                        CurrentLat = location.getLatitude();
                        CurrentLng = location.getLongitude();
                    }
                    CurrentPosition = new LatLng(CurrentLat, CurrentLng);
                    strPosName = "Lat : " + CurrentLat + " Lng : " + CurrentLng;

                    mMap.addMarker(new MarkerOptions().position(CurrentPosition).title(strPosName));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentPosition));
                }
            });
        } else {
            reqAccessLocation();
        }
    }
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    /*@Override
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
            imgUrl = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                imgUrl);
                findImg.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(UpdatePostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UpdatePostActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                getCurrentPosition();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "ไม่สามารถใช้งานแผนที่ได้", Toast.LENGTH_LONG).show();
                } else {
                    getCurrentPosition();
                }
            }
    /* // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        }
    }
    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;


    }


}
