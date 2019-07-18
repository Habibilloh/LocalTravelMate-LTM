package com.example.abdul.ltm;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager mViewPager;

    private SectionPagerAdapter mSectionPagerAdapter;

    private DatabaseReference mUserRef;

    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat Page");



        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mTabLayout = (TabLayout)findViewById(R.id.main_tabs);
        mViewPager = (ViewPager)findViewById(R.id.main_tabPager);
        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            IntentFuntion();
        } else {

            mUserRef.child("online").setValue(true);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            mUserRef.child("online").setValue(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     super.onCreateOptionsMenu(menu);
      getMenuInflater().inflate(R.menu.main_manu,menu);
     return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logoutBtn){

            mAuth.signOut();
            IntentFuntion();
        }
        if(item.getItemId() == R.id.accountSettings){
            Intent intent = new Intent(MainActivity.this,SettingsPage.class);
            startActivity(intent);

        }
        if(item.getItemId() == R.id.allUsers){
            Intent intent = new Intent(MainActivity.this,AllUsers.class);
            startActivity(intent);
        }
        return true;
    }
    public void IntentFuntion(){
        Intent intent = new Intent(MainActivity.this,EntryPage.class);
        startActivity(intent);
        finish();
    }
}
