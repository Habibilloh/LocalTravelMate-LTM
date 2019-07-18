package com.example.abdul.ltm;


import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class TurnChecker {


    private static DatabaseReference mRootRef;
    private static String Idbig;



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String GetSecretKey(final String CurrentUserId, final String UserId){
        mRootRef = FirebaseDatabase.getInstance().getReference();
        final String[] keyDecripted = new String[1];
        final String keyBig;




        mRootRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String prKey;
                final String secKey;

                String Turn =dataSnapshot.child("Chat").child(CurrentUserId).child(UserId).child("turn").getValue().toString();
                if(Turn.equals("1")){
                    secKey = dataSnapshot.child("Chat").child(CurrentUserId).child(UserId).child("secret_key").getValue().toString();
                    prKey = dataSnapshot.child("Users").child(UserId).child("private_key").getValue().toString();
                    Idbig = dataSnapshot.child("Chat").child(UserId).child(CurrentUserId).child("id").getValue().toString();
                }else{
                    secKey = dataSnapshot.child("Chat").child(UserId).child(CurrentUserId).child("secret_key").getValue().toString();
                    prKey = dataSnapshot.child("Users").child(CurrentUserId).child("private_key").getValue().toString();
                    Idbig = dataSnapshot.child("Chat").child(CurrentUserId).child(UserId).child("id").getValue().toString();
                }
                System.out.println(Idbig);







                byte[] encryptedKey = Base64.getDecoder().decode(secKey);


                Key UserPrivateKey = null;
                try {
                    UserPrivateKey = loadPublicKey(prKey);
                } catch (GeneralSecurityException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                byte[] key = new byte[0];
                try {
                    key = RSAsample.decrypt((PublicKey) UserPrivateKey, encryptedKey);
                } catch (Exception e) {
                    e.printStackTrace();
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return Idbig;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Key loadPublicKey(String stored) throws GeneralSecurityException, IOException
    {
        byte[] data = Base64.getDecoder().decode((stored.getBytes()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);

    }



}
