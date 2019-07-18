package com.example.abdul.ltm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ModifyProfile extends AppCompatActivity {

    private EditText mDisplayName;
    private EditText mEmail;

    private EditText mPhoneNumber;
    private EditText mBithDate;
    private EditText mStatus;
    private EditText mCountry;
    private RadioGroup mGenderGroup;
    private Button submitChangeBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String gender = "";
    private DatabaseReference mDatabase;
    private ProgressDialog mModProgress;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);

        mAuth = FirebaseAuth.getInstance();
        mDisplayName = (EditText)findViewById(R.id.mnameField);

        mCountry = (EditText)findViewById(R.id.mcountryField);

        submitChangeBtn = (Button)findViewById(R.id.msubmitChangebtn);
        mPhoneNumber = (EditText) findViewById(R.id.mphoneField);
        mBithDate = (EditText)findViewById(R.id.mbirthField);
        mStatus = (EditText)findViewById(R.id.mstatusField);

        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Modify Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGenderGroup = (RadioGroup) findViewById(R.id.mgenderGroup);
        mModProgress = new ProgressDialog(this);

        mGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButton:
                        gender = "male";
                        break;
                    case R.id.radioButton2:
                        gender = "female";
                        break;

                }
            }
        });

        getUserInfo();


        submitChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String country =mCountry.getText().toString();
                String phone = mPhoneNumber.getText().toString();
                String birth = mBithDate.getText().toString();
                String status = mStatus.getText().toString();
                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(phone)||!TextUtils.isEmpty(birth)||!TextUtils.isEmpty(status)){
                    mModProgress.setTitle("Modifying profile...");
                    mModProgress.setMessage("Please wait ...");
                    mModProgress.setCanceledOnTouchOutside(false);
                    mModProgress.show();
                    register_user(display_name, email, phone , birth, status, country, gender);
                }

            }
        });
    }

    private void getUserInfo() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDisplayName.setText(dataSnapshot.child("name").getValue().toString());
                mEmail.setText(dataSnapshot.child("email").getValue().toString());
                mPhoneNumber.setText(dataSnapshot.child("phone").getValue().toString());
                mBithDate.setText(dataSnapshot.child("age").getValue().toString());
                mStatus.setText(dataSnapshot.child("status").getValue().toString());
                mCountry.setText(dataSnapshot.child("country").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void register_user(String display_name, String email, String phone, String birth, String status,String country, String gender) {

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", display_name);

        hashMap.put("status",status);
        hashMap.put("phone", phone);
        hashMap.put("age",birth);
        hashMap.put("gender",gender);
        hashMap.put("country",country);
        hashMap.put("image","default");
        hashMap.put("thump_image","default");
        mDatabase.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mModProgress.dismiss();

                }
            }
        });
    }
}
