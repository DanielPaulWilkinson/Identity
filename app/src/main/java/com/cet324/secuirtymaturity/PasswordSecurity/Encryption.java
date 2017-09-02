package com.cet324.secuirtymaturity.PasswordSecurity;

import com.mobapphome.mahencryptorlib.MAHEncryptor;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by DanielWilkinson on 1/3/2017.
 */

public class Encryption {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
    final private static String PEPPER = "Identity";
    //class using mahENCRYPTION
    //method to encode string
    //throws multiple error exceptions
    public String encode(String encodepassword, String Key) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException,
            NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        //get a new instance of this method with the users key
        MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(Key);
        //encoding the password
        String encrypted = mahEncryptor.encode(encodepassword);
        //return encoded variable
        return encrypted;
    }
    //method to decode password throws many errors
    public String decode(String decodepassword, String Key) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException,
            NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        //get the instance for decoding
        MAHEncryptor mahEncryptor = MAHEncryptor.newInstance(Key);
        // decoding the password
        String decoded = mahEncryptor.decode(decodepassword);
        //return decoded passsword
        return decoded;
    }


    public String makeUnusable(String encodepassword, String Key, String salt) {
        //get a new instance of this method with the users key
        MAHEncryptor mahEncryptor = null;
        String encrypted="";
        try {
            mahEncryptor = MAHEncryptor.newInstance(Key);
            //encoding the password
             encrypted = mahEncryptor.encode(sha256(PEPPER+encodepassword+salt));
            //return encoded variable
            return encrypted;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return encrypted;
    }


    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public static byte[] getSalt(){
        SecureRandom sr = null;
        byte[] salt = new byte[0];
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");

            salt = new byte[16];
        sr.nextBytes(salt);
        for(int i = 0; i<16; i++) {
            System.out.print(salt[i] & 0x00FF);
            System.out.print(" ");
        }
        return salt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return salt;
    }

}
