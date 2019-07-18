package com.example.abdul.ltm;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {

    private View mMainView;
    private RecyclerView mUsersList;
    private FirebaseRecyclerAdapter<Requests, ViewHolderActiviy> adapter ;
    private FirebaseRecyclerOptions<Requests> options;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mRequestList;
    private FirebaseAuth mAuth;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mMainView = inflater.inflate(R.layout.fragment_request, container, false);
        mAuth = FirebaseAuth.getInstance();
        String mCurrentUser = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mRequestList = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrentUser);

        mUsersList = (RecyclerView)mMainView.findViewById(R.id.request_list_view);


        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(mMainView.getContext()));

        options = new FirebaseRecyclerOptions.Builder<Requests>()
                .setQuery(mRequestList, Requests.class).build();

        adapter = new FirebaseRecyclerAdapter<Requests, ViewHolderActiviy>(options) {

            protected void onBindViewHolder(@NonNull final ViewHolderActiviy holder, int position, @NonNull final Requests model) {
                final String list_user_id = getRef(position).getKey();

                String request_type = model.getRequest_type();
                if(!request_type.equals("sent")) {

                    mUserDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            holder.displayName.setText(Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                            holder.status.setText(Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString());


                            final String image = Objects.requireNonNull(dataSnapshot.child("thump_image").getValue()).toString();
                            Picasso.with(mMainView.getContext()).load(image).placeholder(R.drawable.profile_picture).into(holder.displayImage, new Callback() {
                                @Override
                                public void onSuccess() {


                                }

                                @Override
                                public void onError() {
                                    Picasso.with(mMainView.getContext()).load(image).placeholder(R.drawable.profile_picture).into(holder.displayImage);

                                }
                            });

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent profileIntent = new Intent(mMainView.getContext(), Activity_profile.class);
                                    profileIntent.putExtra("user_id", list_user_id);
                                    startActivity(profileIntent);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else {
                   holder.itemView.setVisibility(View.GONE);

                }



            }

            @NonNull
            @Override
            public ViewHolderActiviy onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


                View view = LayoutInflater.from(mMainView.getContext()).inflate(R.layout.users_single_layout,viewGroup,false);


                return new ViewHolderActiviy(view);
            }
        };

        adapter.startListening();
        mUsersList.setAdapter(adapter);

        // Inflate the layout for this fragment
        return mMainView;
    }
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

}
