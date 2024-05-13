/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author gauravdahal
 */
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "k7yHmEnlXaBJdkC5F+b1Yw=="; // Replace with your actual secret key
    private static final int IV_LENGTH = 16; // IV length for AES encryption

    public static String encrypt(String plaintext) {
        try {
            // Generate a secure random initialization vector
            byte[] iv = IVGenerator.generateIV();

            // Convert secret key and IV to SecretKeySpec and IvParameterSpec
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Initialize Cipher for encryption
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Perform encryption
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());

            // Combine IV and encrypted data into a single byte array
            byte[] combined = new byte[IV_LENGTH + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
            System.arraycopy(encryptedBytes, 0, combined, IV_LENGTH, encryptedBytes.length);

            // Base64 encode the combined byte array
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String ciphertext) {
        try {
            // Decode Base64 encoded ciphertext
            byte[] combined = Base64.getDecoder().decode(ciphertext);

            // Extract IV and encrypted data
            byte[] iv = new byte[IV_LENGTH];
            byte[] encryptedBytes = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            System.arraycopy(combined, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);

            // Convert secret key and IV to SecretKeySpec and IvParameterSpec
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Initialize Cipher for decryption
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Perform decryption
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Return decrypted plaintext
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
