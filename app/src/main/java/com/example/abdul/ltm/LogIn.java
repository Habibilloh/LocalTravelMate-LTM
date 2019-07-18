package com.example.abdul.ltm;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.os.Build;
        import android.support.annotation.NonNull;
        import android.support.annotation.RequiresApi;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import android.support.v7.widget.Toolbar;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.iid.FirebaseInstanceId;


public class LogIn extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLoginBtn;
    private Toolbar mToolbar;

    private TextView forgotPass;
    ProgressDialog mLogProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText)findViewById(R.id.emailField);
        mPassword = (EditText)findViewById(R.id.passwordField);
        forgotPass  =(TextView)findViewById(R.id.forgotPass);

        mLoginBtn = (Button)findViewById(R.id.logInBtn);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLogProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this,ForgotPassword.class);
                startActivity(intent);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){

                    Toast.makeText(LogIn.this, "Email or password field is empty!", Toast.LENGTH_SHORT).show();
                }else {

                    mLogProgress.setTitle("Logging In");
                    mLogProgress.setMessage("Please wait...");
                    mLogProgress.setCanceledOnTouchOutside(false);
                    mLogProgress.show();


                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mLogProgress.dismiss();

                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Intent intent = new Intent(LogIn.this,MainPage.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });

                }else{
                    mLogProgress.hide();
                    Toast.makeText(LogIn.this, "There is an error while logging in", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
