package com.example.renewseviceqq;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    androidx.appcompat.widget.Toolbar toolbar;
    private Context mContext = MainActivity.this;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private BottomNavigationView mainbottomNav;
    private FloatingActionButton addPostBtn;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;

    TextView btnPostList;
    Fragment selectedFragment = null;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);*/


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new HomeFragment()).commit();
        if(mAuth.getCurrentUser() != null) {
            mainbottomNav = findViewById(R.id.mainBottomNav);
            // FRAGMENTS
            homeFragment = new HomeFragment();


            accountFragment = new AccountFragment();

            initializeFragment();


            mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                    switch (menuItem.getItemId()) {
                        case R.id.ic_home:
                            replaceFragment(homeFragment, currentFragment);
                            return true;

                        case R.id.ic_account:
                            replaceFragment(accountFragment, currentFragment);
                            return true;

                        case R.id.ic_search:
                            Intent a = new Intent(MainActivity.this, SearchActivity.class);
                            startActivity(a);
                            return true;




                        default:
                            return false;


                    }
                    /*if(selectedFragment != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,selectedFragment).commit();
                    }
                    return true;*/
                }
            });
            addPostBtn = findViewById(R.id.add_post_btn);
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                    startActivity(newPostIntent);
                }
            });



        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            sendToLogin();
        }
    }
    private void initializeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.add(R.id.main_container, accountFragment);
        fragmentTransaction.hide(accountFragment);

        fragmentTransaction.commit();
    }
    private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();


        if(fragment == homeFragment){

            fragmentTransaction.hide(accountFragment);



        }

        if(fragment == accountFragment){

            fragmentTransaction.hide(homeFragment);



        }

        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();

    }
    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }
}
