package com.example.abdul.ltm.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.abdul.ltm.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewHolderActiviy extends RecyclerView.ViewHolder {

   public   TextView displayName, status;
   public  CircleImageView displayImage;

    public ViewHolderActiviy(@NonNull View itemView) {
        super(itemView);

        displayImage = (CircleImageView)itemView.findViewById(R.id.user_single_image);
        displayName = (TextView)itemView.findViewById(R.id.user_single_name);
        status = (TextView)itemView.findViewById(R.id.user_single_status);


    }

}
