package com.example.abdul.ltm;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class RSAsample {

    public static byte[] encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(message.getBytes());
    }

    public static byte[] decrypt(PublicKey privateKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(encrypted);
    }
    public static String encryptAES (String secretKey, String text) throws GeneralSecurityException {
          String encrypted = AESCrypt.encrypt(secretKey, text);
        return  encrypted;
    }
    public static String decryptAES (String secretKey, String encryptedMsg) throws GeneralSecurityException {


        return  AESCrypt.decrypt(secretKey, encryptedMsg);
    }
}
