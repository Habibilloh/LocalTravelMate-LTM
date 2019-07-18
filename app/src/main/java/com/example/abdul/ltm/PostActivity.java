package com.example.abdul.ltm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

public class PostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton mSelectImage;

    private EditText mPostTitle;
    private EditText mPostdesc;

    private Button mSubmitBtn;

    private Uri mImgaeUri = null;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private DatabaseReference mRootRef;

    private ProgressDialog mProgress;
    private String country;


    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mToolbar = (Toolbar)findViewById(R.id.postActivityToolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPostTitle = (EditText)findViewById(R.id.titleField);
        mPostdesc = (EditText)findViewById(R.id.descriptionField);

        mSubmitBtn = (Button)findViewById(R.id.submitPostBtn);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                startPosting();
            }
        });
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              country =  dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("country").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mSelectImage = (ImageButton)findViewById(R.id.imageSelect);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallaryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent, GALLERY_REQUEST);
            }
        });

        mProgress = new ProgressDialog(this);

    }

    private void startPosting() {
        mProgress.setTitle("Posting ...");
        mProgress.setMessage("Please wait while we are posting");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostdesc.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImgaeUri != null){



           final StorageReference filepath = mStorage.child("Blog_Image").child(mImgaeUri.getLastPathSegment());

           filepath.putFile(mImgaeUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                 filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         final Uri downloadUri = uri;


                                 DatabaseReference newPost = mDatabase.push();

                                 newPost.child("title").setValue(title_val);
                                 newPost.child("desc").setValue(desc_val);
                                 newPost.child("image").setValue(downloadUri.toString());
                                 newPost.child("userId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                 newPost.child("date").setValue(ServerValue.TIMESTAMP);
                                 newPost.child("country").setValue(country);

                                 mProgress.dismiss();
                                 mPostTitle.setText("");
                                 mPostdesc.setText("");
                                 Toast.makeText(PostActivity.this, "Post successfully uploaded!", Toast.LENGTH_SHORT).show();

                                 Intent intent = new Intent(PostActivity.this,MainPage.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 startActivity(intent);




                     }
                 });

               }
           });

        }else {
            Toast.makeText(this, "Fields are emply or no Image selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){

             Uri imageUri = data.getData();

             CropImage.activity(imageUri)
                        .setAspectRatio(174,100)
                        .start(this);

        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                mImgaeUri = result.getUri();
                mSelectImage.setImageURI(mImgaeUri);
            }
        }
    }
}
