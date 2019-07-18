package com.example.abdul.ltm;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class TouristInfoFrag extends Fragment {

    private View mMainCountryView;


    private RecyclerView mUsersList;
    private FirebaseRecyclerAdapter<Countries, ViewHolderCountryActiviy> adapter ;
    private FirebaseRecyclerOptions<Countries> options;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mCountryList;
    private FirebaseAuth mAuth;


    public TouristInfoFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainCountryView = inflater.inflate(R.layout.fragment_tourist_info, container, false);

        mAuth = FirebaseAuth.getInstance();
        final String mCurrentUser = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mCountryList = FirebaseDatabase.getInstance().getReference().child("CountryInfo");

        mUsersList = (RecyclerView)mMainCountryView.findViewById(R.id.countryListView);


        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(new LinearLayoutManager(mMainCountryView.getContext()));

        options = new FirebaseRecyclerOptions.Builder<Countries>()
                .setQuery(mCountryList, Countries.class).build();

        adapter = new FirebaseRecyclerAdapter<Countries, ViewHolderCountryActiviy>(options) {



            @Override
            protected void onBindViewHolder(@NonNull final ViewHolderCountryActiviy holder, int position, @NonNull Countries model) {

                holder.countryName.setText(model.getNameCountry());
                final String countryImageUrl = model.getImageCountry();
                final String countrySiteUrl = model.getUrlCountry();
                Picasso.with(mMainCountryView.getContext()).load(countryImageUrl).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.countryImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(mMainCountryView.getContext()).load(countryImageUrl).into(holder.countryImage);


                    }

                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(countrySiteUrl));
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolderCountryActiviy onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(mMainCountryView.getContext()).inflate(R.layout.country_rows,viewGroup,false);
                return new ViewHolderCountryActiviy(view);
            }
        };

        adapter.startListening();
        mUsersList.setAdapter(adapter);
        // Inflate the layout for this fragment
        return mMainCountryView;
    }

    class ViewHolderCountryActiviy extends RecyclerView.ViewHolder {
        public ImageView countryImage;
        public TextView countryName;
        public ViewHolderCountryActiviy(@NonNull View itemView) {
            super(itemView);
            countryImage = (ImageView)itemView.findViewById(R.id.countryImageView);
            countryName = (TextView)itemView.findViewById(R.id.countryNameView);
        }
    }
}

