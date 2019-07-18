package com.example.abdul.ltm;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abdul.ltm.ViewHolder.ViewHolderActiviy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFrag extends Fragment {

    private View mMainPageView;


    private RecyclerView mUsersList;
    private FirebaseRecyclerAdapter<Blogs, ViewHolderBlogActiviy> adapter ;
    private FirebaseRecyclerOptions<Blogs> options;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mBlogList;
    private FirebaseAuth mAuth;


    public PostsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainPageView = inflater.inflate(R.layout.fragment_posts, container, false);


        FloatingActionButton fab = (FloatingActionButton) mMainPageView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mMainPageView.getContext(),PostActivity.class);
                startActivity(intent);


            }
        });

        mAuth = FirebaseAuth.getInstance();
        final String mCurrentUser = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mBlogList = FirebaseDatabase.getInstance().getReference().child("Blog");

        mUsersList = (RecyclerView)mMainPageView.findViewById(R.id.postsListView);


        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(mMainPageView.getContext()));

        options = new FirebaseRecyclerOptions.Builder<Blogs>()
                .setQuery(mBlogList, Blogs.class).build();

        adapter = new FirebaseRecyclerAdapter<Blogs, ViewHolderBlogActiviy>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderBlogActiviy holder, int position, @NonNull final Blogs model) {
                holder.blogTitle.setText(model.getTitle());
                holder.blog_desc.setText(model.getDesc());
                holder.sent_date.setText( new Date(model.getDate()).toString());
                holder.blog_country.setText(model.getCountry());
                final String uri = model.getImage();
                Picasso.with(mMainPageView.getContext()).load(uri).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.blog_imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mMainPageView.getContext()).load(uri).into(holder.blog_imageView);

                    }
                });
                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = Objects.requireNonNull(dataSnapshot.child(model.getUserId()).child("name").getValue()).toString();
                        final String thum_image = Objects.requireNonNull(dataSnapshot.child(model.getUserId()).child("thump_image").getValue()).toString();
                        holder.senderName.setText(name);
                        Picasso.with(mMainPageView.getContext()).load(thum_image).networkPolicy(NetworkPolicy.OFFLINE)
                                .placeholder(R.drawable.profile_picture).into(holder.senderImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(mMainPageView.getContext()).load(thum_image).placeholder(R.drawable.profile_picture).into(holder.senderImage);

                                    }
                                });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.senderName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent =  new Intent(mMainPageView.getContext(),Activity_profile.class);
                        profileIntent.putExtra("user_id", model.getUserId());
                        startActivity(profileIntent);
                    }
                });


            }

            @NonNull
            @Override
            public ViewHolderBlogActiviy onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(mMainPageView.getContext()).inflate(R.layout.blog_row,viewGroup,false);
                return new ViewHolderBlogActiviy(view);
            }
        };

        adapter.startListening();
        mUsersList.setAdapter(adapter);

            // Inflate the layout for this fragment
        return mMainPageView;
    }

    private class ViewHolderBlogActiviy extends RecyclerView.ViewHolder {

        public TextView blog_country,blog_desc,blogTitle,senderName,sent_date;
        public ImageView blog_imageView;
        public CircleImageView senderImage;
        public ViewHolderBlogActiviy(@NonNull View itemView) {
            super(itemView);
            blog_country = (TextView)itemView.findViewById(R.id.senderCountry);
            blog_desc  = (TextView)itemView.findViewById(R.id.post_desc);
            blogTitle = (TextView)itemView.findViewById(R.id.post_title);
            senderName =(TextView)itemView.findViewById(R.id.senderName);
            sent_date =(TextView)itemView.findViewById(R.id.sentDate);
            blog_imageView = (ImageView)itemView.findViewById(R.id.post_image);
            senderImage =(CircleImageView)itemView.findViewById(R.id.senderImage);
        }
    }
}
