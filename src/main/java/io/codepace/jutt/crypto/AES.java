package io.codepace.jutt.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * This class handles all {@link String} encryption by means of the AES algorithm
 */
public class AES {

    /**
     * Encrypt a {@link String} with AES algorithm.
     *
     * @param data The {@link String} do encrypt
     * @param key The key to encrypt the data with
     * @return the encrypted string
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        Key keyVal = generateEncryptionKey(key.getBytes());
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keyVal);
        byte[] encryptedValue = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    /**
     * Decrypt a {@link String} with the AES encryption algorithm
     * @param data The encrypted data to decrypt
     * @param key The decryption key to use
     * @return {@link String} The
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        Key encryptionKey = generateEncryptionKey(key.getBytes());
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, encryptionKey);
        byte[] decodedValue = Base64.getDecoder().decode(data);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private static Key generateEncryptionKey(byte[] key){
        return new SecretKeySpec(key, "AES");
    }
}
