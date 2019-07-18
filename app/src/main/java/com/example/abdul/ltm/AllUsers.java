package com.example.abdul.ltm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abdul.ltm.ViewHolder.ViewHolderActiviy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class AllUsers extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mUsersList;
    private FirebaseRecyclerAdapter<Friends, ViewHolderActiviy> adapter ;
    private FirebaseRecyclerOptions<Friends> options;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolbar = (Toolbar)findViewById(R.id.users_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUsersList = (RecyclerView)findViewById(R.id.users_list_recyclerView);


        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<Friends>()
                .setQuery(mUserDatabase, Friends.class).build();

        adapter = new FirebaseRecyclerAdapter<Friends, ViewHolderActiviy>(options) {

               protected void onBindViewHolder(@NonNull final ViewHolderActiviy holder, int position, @NonNull final Friends model) {

                   final String list_user_id = getRef(position).getKey();

                holder.displayName.setText(model.getName());
                holder.status.setText(model.getStatus());

                String image = model.getImage();
                   Picasso.with(AllUsers.this).load(image).placeholder(R.drawable.profile_picture).into(holder.displayImage, new Callback() {
                       @Override
                       public void onSuccess() {





                       }

                       @Override
                       public void onError() {

                       }
                   });
                   holder.itemView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           Intent profileIntent =  new Intent(AllUsers.this,Activity_profile.class);
                           profileIntent.putExtra("user_id", list_user_id);
                           startActivity(profileIntent);
                       }
                   });

            }

            @NonNull
            @Override
            public ViewHolderActiviy onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


                View view = LayoutInflater.from(AllUsers.this).inflate(R.layout.users_single_layout,viewGroup,false);


                return new ViewHolderActiviy(view);
            }
        };

        adapter.startListening();
        mUsersList.setAdapter(adapter);




    }
}
