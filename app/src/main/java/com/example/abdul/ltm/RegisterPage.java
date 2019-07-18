package com.example.abdul.ltm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

public class RegisterPage extends AppCompatActivity {

    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPhoneNumber;
    private EditText mBithDate;
    private EditText mStatus;
    private EditText mCountry;
    private RadioGroup mGenderGroup;
    public PublicKey pub;
    public PrivateKey pvt;

    private Button submitBtn;
    //Creating toolbar
    private Toolbar mToolbar;
    //Progress Dialog
    private ProgressDialog mRegProgress;
    //Firebase Authentication
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseUser mUser;
    private String gender = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //Firebase Initialization
        mAuth = FirebaseAuth.getInstance();

        mDisplayName = (EditText)findViewById(R.id.nameField);
        mEmail = (EditText)findViewById(R.id.emailField);
        mPassword = (EditText)findViewById(R.id.passwordField);
        submitBtn = (Button)findViewById(R.id.submitbtn);
        mPhoneNumber = (EditText) findViewById(R.id.phoneField);
        mBithDate = (EditText)findViewById(R.id.birthField);
        mStatus = (EditText)findViewById(R.id.statusField);
        mCountry = (EditText)findViewById(R.id.countryField);

        //Toolbar Initialization and giving title to toolbar and make return sign to go back to entry activity by setting a parent activity for RegisterPage activity which is EntryPage
        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Registration progress dialog initialization
        mRegProgress = new ProgressDialog(this);

        mGenderGroup = (RadioGroup) findViewById(R.id.genderGroup);

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


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getText().toString();
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String phone = mPhoneNumber.getText().toString();
                String birth = mBithDate.getText().toString();
                String status = mStatus.getText().toString();
                String country = mCountry.getText().toString();
                if(!TextUtils.isEmpty(display_name)||!TextUtils.isEmpty(email)||!TextUtils.isEmpty(password)||!TextUtils.isEmpty(phone)||!TextUtils.isEmpty(birth)||!TextUtils.isEmpty(status)||!TextUtils.isEmpty(country)){
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name, email, password, phone , birth, status, country, gender);
                }else{
                    Toast.makeText(RegisterPage.this, "Some fields are empty. Please fill them!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void register_user(final String display_name, final String UserEmail, String password, final String phone, final String birth, final String status, final String country, final String gender) {
        mAuth.createUserWithEmailAndPassword(UserEmail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                             mUser = FirebaseAuth.getInstance().getCurrentUser();
                             String device_Token = FirebaseInstanceId.getInstance().getToken();
                             String uid = mUser.getUid();
                             mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                            KeyPairGenerator kpg = null;
                            try {
                                kpg = KeyPairGenerator.getInstance("RSA");
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            kpg.initialize(2048);
                            KeyPair kp = kpg.generateKeyPair();

                            pub = kp.getPublic();
                            pvt = kp.getPrivate();


                            byte[] encodedPublicKey = pub.getEncoded();
                            String b64PublicKey = Base64.getEncoder().encodeToString(encodedPublicKey);
                            byte[] encodedPrivateKey = pub.getEncoded();
                            String b64PrivateKey = Base64.getEncoder().encodeToString(encodedPrivateKey);

                           /* byte[] keyBytes = Base64.getDecoder().decode(b64PublicKey);
                            try {
                                PublicKey kkk = (PublicKey) KeyFactory.getInstance("RSA").generatePublic(new PKCS8EncodedKeySpec(keyBytes));
                            } catch (InvalidKeySpecException e) {
                                e.printStackTrace();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }*/

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("device_token",device_Token);
                            hashMap.put("name", display_name);
                            hashMap.put("email", UserEmail);
                            hashMap.put("status",status);
                            hashMap.put("phone", phone);
                            hashMap.put("age",birth);
                            hashMap.put("public_key",b64PublicKey);
                            hashMap.put("private_key",b64PrivateKey);
                            hashMap.put("country",country);
                            hashMap.put("travel","Country");
                            hashMap.put("from", "date");
                            hashMap.put("to","date");
                            hashMap.put("gender",gender);
                            hashMap.put("image","default");
                            hashMap.put("thump_image","default");
                            mDatabase.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mRegProgress.dismiss();
                                        Intent intent = new Intent(RegisterPage.this,MainPage.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        }else {
                            mRegProgress.hide();
                            Toast.makeText(RegisterPage.this, "Please make sure and try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }
}
