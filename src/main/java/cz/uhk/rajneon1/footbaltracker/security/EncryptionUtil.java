package cz.uhk.rajneon1.footbaltracker.security;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class EncryptionUtil {

    private Environment environment;

    public EncryptionUtil(Environment environment) {
        this.environment = environment;
    }

    /**
     * Encrypt with AES algorithm and encode with Base64
     */
    public String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String key = environment.getProperty("security.refreshtoken.encryptkey");
        Key encKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, encKey);
        byte[] encVal = c.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    /**
     * Decode Base64 and decrypt AES
     */
    public String decrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        String key = environment.getProperty("security.refreshtoken.encryptkey");
        Key decKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, decKey);
        byte[] decodedValue = Base64.getDecoder().decode(value);
        byte[] decValue = cipher.doFinal(decodedValue);
        return new String(decValue);
    }

}
