package com.example.abdul.ltm;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.abdul.ltm.ViewHolder.ViewHolderActiviy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFregment extends Fragment {

    private RecyclerView mFriendsList;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;



    private String mCurrent_user_id;
    private View mMainView;

    private FirebaseRecyclerAdapter<FriendsList, ViewHolderActiviy> adapter ;
    private FirebaseRecyclerOptions<FriendsList> options;



    public FriendsFregment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_friends_fregment, container, false);

        mFriendsList =(RecyclerView)mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        options = new FirebaseRecyclerOptions.Builder<FriendsList>()
                .setQuery(mFriendsDatabase,FriendsList.class).build();

        adapter = new FirebaseRecyclerAdapter<FriendsList, ViewHolderActiviy>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderActiviy holder, int position, @NonNull final FriendsList model) {


                final String list_user_id = getRef(position).getKey();


                assert list_user_id != null;
                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                        final String userName = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                        final String uri =  Objects.requireNonNull(dataSnapshot.child("thump_image").getValue()).toString();

                        holder.displayName.setText(userName);
                        holder.status.setText(model.getDate());


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(mMainView.getContext());

                                builder.setTitle("Select Option");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {


                                        if(i == 0){
                                            Intent profileIntent =  new Intent(getContext(),Activity_profile.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            startActivity(profileIntent);
                                        }
                                        if (i == 1 ){

                                            Intent chatIntent =  new Intent(getContext(),ChatActivity.class);
                                            chatIntent.putExtra("user_id", list_user_id);
                                            chatIntent.putExtra("user_name", userName);
                                            startActivity(chatIntent);


                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                       Picasso.with(mMainView.getContext()).load(uri).placeholder(R.drawable.profile_picture).into(holder.displayImage, new Callback() {
                            @Override
                            public void onSuccess() {

                                Boolean userOnline = (boolean) dataSnapshot.child("online").getValue();

                            }

                            @Override
                            public void onError() {
                                Picasso.with(mMainView.getContext()).load(uri).placeholder(R.drawable.profile_picture).into(holder.displayImage);
                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public ViewHolderActiviy onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                assert getParentFragment() != null;
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_single_layout,viewGroup,false);


                return new ViewHolderActiviy(view);
            }
        };

        adapter.startListening();
        mFriendsList.setAdapter(adapter);

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(mMainView.getContext(),EntryPage.class);
            startActivity(intent);
            getActivity().finish();
        }

        if(adapter!=null){
            adapter.startListening();
        }





    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!= null){
            adapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.startListening();
        }
    }
}
