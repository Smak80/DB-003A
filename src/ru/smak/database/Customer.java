package ru.smak.database;

import com.lambdaworks.crypto.SCryptUtil;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class Customer {
    private String phone;
    private String login;
    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password, boolean hash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        /*var iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        this.password = iterations + ":" + toHex(salt) + ":" + toHex(hash);*/
        this.password = (hash) ? SCryptUtil.scrypt(password, 16, 16, 16) : password;

    }

    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public boolean verifyPassword(String originalPassword, String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
//        String[] parts = storedPassword.split(":");
//        int iterations = Integer.parseInt(parts[0]);
//
//        byte[] salt = fromHex(parts[1]);
//        byte[] hash = fromHex(parts[2]);
//
//        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
//                salt, iterations, hash.length * 8);
//        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//        byte[] testHash = skf.generateSecret(spec).getEncoded();
//
//        int diff = hash.length ^ testHash.length;
//        for(int i = 0; i < hash.length && i < testHash.length; i++)
//        {
//            diff |= hash[i] ^ testHash[i];
//        }
//        return diff == 0;
        return SCryptUtil.check(originalPassword, storedPassword);
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public Customer(String phone, String login, String password){
        setPhone(phone);
        setLogin(login);
        try {
            setPassword(password, true);
        } catch (Exception e){}
    }

    public Customer(){}

    @Override
    public String toString(){
        return phone + ": " + login + " (*****)";
    }
}
