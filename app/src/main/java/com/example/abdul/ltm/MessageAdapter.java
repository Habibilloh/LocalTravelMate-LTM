package com.example.abdul.ltm;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.abdul.ltm.ChatActivity.applikey;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mRootRef;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout ,parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName;
        public TextView displayTime;


        public MessageViewHolder(View view) {
            super(view);


            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            displayTime = (TextView)view.findViewById(R.id.time_text_layout);

        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();
        long message_time = c.getTime();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Chat");
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                final String image = Objects.requireNonNull(dataSnapshot.child("thump_image").getValue()).toString();

                viewHolder.displayName.setText(name);

                Picasso.with(viewHolder.profileImage.getContext()).load(image).placeholder(R.drawable.profile_picture).into(viewHolder.profileImage, new Callback() {
                    @Override
                    public void onSuccess() {


                    }

                    @Override
                    public void onError() {
                        Picasso.with(viewHolder.profileImage.getContext()).load(image).placeholder(R.drawable.profile_picture).into(viewHolder.profileImage);

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")) {

            String cypher = c.getMessage();
            String text = null;
            try {
                 text = RSAsample.decryptAES(applikey, cypher);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

            viewHolder.messageText.setText(text);



        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);


        }



        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(message_time);

        viewHolder.displayTime.setText(resultdate.toString());


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }






}
