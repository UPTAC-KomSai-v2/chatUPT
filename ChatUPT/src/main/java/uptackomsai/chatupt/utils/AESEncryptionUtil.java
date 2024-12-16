/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uptackomsai.chatupt.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import io.github.cdimascio.dotenv.Dotenv;

/**
 *
 * @author origa
 */
public class AESEncryptionUtil {
    // Generate an AES key (you can use this to create a key or use an existing one)
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Use 128, 192, or 256 bits based on your requirements
        return keyGenerator.generateKey();
    }
    
    public static SecretKey getSecretKey() {
        // Load environment variables
        Dotenv dotenv = Dotenv.load();

        // Get the AES key as a string
        String key = dotenv.get("AES_SECRET_KEY");

        // Convert the key string to a SecretKey
        return new SecretKeySpec(key.getBytes(), "AES");
    }

    // Encrypt a JSON string using AES
    public static String encrypt(String json, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(json.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Convert to Base64 string
    }

    // Decrypt an AES-encrypted string
    public static String decrypt(String encryptedJson, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedJson); // Decode Base64 string
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes); // Convert to original JSON string
    }
}
    

