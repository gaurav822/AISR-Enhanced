package Utils;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author gauravdahal
 */
import java.security.SecureRandom;
import java.util.Base64;

public class IVGenerator {

    public static void main(String[] args) {
        // Generate a secure random 16-byte IV
        byte[] iv = generateIV();
        System.out.println("Generated Initialization Vector (Base64 Encoded): " + encodeIV(iv));
    }

    public static byte[] generateIV() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16]; // 16 bytes for AES encryption
        secureRandom.nextBytes(iv);
        return iv;
    }

    public static String encodeIV(byte[] iv) {
        return Base64.getEncoder().encodeToString(iv);
    }
}

