package com.example.abdul.ltm;

import android.content.Intent;
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

public class MainPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager mViewPager;

    private SectionPagePagerAdapter mSectionPagePagerAdapter;

    private DatabaseReference mUserRef;

    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        mAuth = FirebaseAuth.getInstance();
        mToolbar = (Toolbar)findViewById(R.id.mainPage_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Main Page");

        mTabLayout = (TabLayout)findViewById(R.id.mainPage_tabs);
        mViewPager = (ViewPager)findViewById(R.id.mainPage_tabPager);
        mSectionPagePagerAdapter = new SectionPagePagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagePagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_page_menu,menu);
        return true;
    }

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            IntentFuntion();
        }


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.mainPage_logoutBtn){

            mAuth.signOut();
            IntentFuntion();
        }
        if(item.getItemId() == R.id.goto_ChatPageBtn){
            Intent intent = new Intent(MainPage.this,MainActivity.class);
            startActivity(intent);

        }
      /*if(item.getItemId() == R.id.allUsers){
            Intent intent = new Intent(MainActivity.this,AllUsers.class);
            startActivity(intent);
        }*/
        return true;
    }

    public void IntentFuntion(){
        Intent intent = new Intent(MainPage.this,EntryPage.class);
        startActivity(intent);
        finish();
    }
}
