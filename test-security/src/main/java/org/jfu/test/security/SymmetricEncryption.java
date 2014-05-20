package org.jfu.test.security;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricEncryption {

    public static byte[] encrypt(String key, byte[] payload) {
        try {
            SecretKeySpec ks = new SecretKeySpec(md5Hash(key), "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, ks);
            return c.doFinal(payload);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Encrypt exception", e);
        }
    }

    public static byte[] decrypt(String key, byte[] secret) {
        try {
            SecretKeySpec ks = new SecretKeySpec(md5Hash(key), "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, ks);
            return c.doFinal(secret);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Encrypt exception", e);
        }
    }

    private static byte[] md5Hash(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.reset();
        digest.update(str.getBytes());
        return digest.digest();
    }

}
