package org.octoteam.octoproject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Alexander on 28/02/16.
 */
public class HashUtils {
    public static String md5(String plaintext) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.reset();
        m.update(plaintext.getBytes());

        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashText = bigInt.toString(16);

        while(hashText.length() < 32 ){
            hashText = "0" + hashText;
        }

        return hashText;
    }
}
