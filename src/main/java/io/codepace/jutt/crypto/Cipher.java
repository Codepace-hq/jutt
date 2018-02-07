package io.codepace.jutt.crypto;

import jdk.internal.jline.internal.Nullable;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * This class handles all basic hashing of strings and files.
 */
public class Cipher {

    public static final String SHA256 = "SHA-256";
    public static final String SHA1   = "SHA-1";

    /**
     *  Hashes the given string with the SHA-256 algorithm
     * @param data The {@link String} to hash
     * @param salt The {@link String} to salt the hash with
     * @return The SHA-256 hash of the data
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String SHA256Encrypt(String data, @Nullable String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest digest = MessageDigest.getInstance(SHA256);
        return new String(digest.digest((data+salt).getBytes("UTF-8")));
    }

    /**
     * Calculates the SHA-256 hash of a given file.
     * @param file The file to hash
     * @return {@link String} The hash of the file
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String SHA256Checksum(File file) throws NoSuchAlgorithmException, IOException {
        byte[] buf = new byte[4092]; // Maybe play with the size of this buffer
        int count;
        MessageDigest digest = MessageDigest.getInstance(SHA256);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        while ((count = in.read(buf)) > 0){
            digest.update(buf, 0, count);
        }
        return Base64.getEncoder().encodeToString(digest.digest());
    }

    /**
     * Hashes the given string with the SHA-1 algorithm
     * @deprecated Since there have been hash collisions with SHA-1, SHA-256 is the preferred hashing method
     * @param data The {@link String} to hash
     * @param salt The {@link String} to salt the hash with
     * @return The SHA-1 hash of the data
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    @Deprecated
    public static String SHA1Encrypt(String data, @Nullable String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest digest = MessageDigest.getInstance(SHA1);
        return new String(digest.digest((data+salt).getBytes("UTF-8")));
    }

    /**
     * Calculates the SHA-1 hash of a given file.
     * @deprecated Since there have been hash collisions with SHA-1, SHA-256 is the preferred hashing method
     * @param file The file to hash
     * @return {@link String} The hash of the file
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    @Deprecated
    public static String SHA1Checksum(File file) throws NoSuchAlgorithmException, IOException {
        byte[] buf = new byte[4092]; // Maybe play with the size of this buffer
        int count;
        MessageDigest digest = MessageDigest.getInstance(SHA1);
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        while ((count = in.read(buf)) > 0){
            digest.update(buf, 0, count);
        }
        return Base64.getEncoder().encodeToString(digest.digest());
    }
}
