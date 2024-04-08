package com.infinity.common.utils.passwordencutils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

/**
 * @author Aditya Patil
 */

public class AESEncoderUtils {
    static final String AES_ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static Cipher getInstance(String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(transformation);
    }

    public static byte[] encrypt(String transformation, Key secretKeySpec, AlgorithmParameterSpec algorithmParameterSpec, byte[] input) throws GeneralSecurityException {
        Cipher cipher = getInstance(transformation);
        cipher.init(1, secretKeySpec, algorithmParameterSpec);
        return cipher.doFinal(input);
    }

    public static byte[] decrypt(String transformation, Key secretKeySpec, AlgorithmParameterSpec algorithmParameterSpec, byte[] input) throws GeneralSecurityException {
        Cipher cipher = getInstance(transformation);
        cipher.init(PasswordEncoderConstant.INTEGER_VALUE_TWO, secretKeySpec, algorithmParameterSpec);
        return cipher.doFinal(input);
    }

    public String encrypt(String salt, String iv, String passphrase, String plaintext) throws DecoderException {
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] encrypted = encrypt(AES_ENCRYPTION_ALGORITHM, key, new IvParameterSpec(hex(iv)), plaintext
                    .getBytes(StandardCharsets.UTF_8));
            return base64(encrypted);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    public String decrypt(String salt, String iv, String passphrase, String ciphertext) throws DecoderException {
        try {
            SecretKey key = generateKey(salt, passphrase);
            byte[] decrypted = decrypt(AES_ENCRYPTION_ALGORITHM, key, new IvParameterSpec(hex(iv)), base64(ciphertext));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private SecretKey generateKey(String salt, String passphrase) throws DecoderException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), PasswordEncoderConstant.INTEGER_VALUE_TEN_THOUSAND, PasswordEncoderConstant.INTEGER_VALUE_ONE_TWENTY_EIGHT
            );
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        } catch (NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    public static byte[] hex(String str) throws DecoderException, org.apache.commons.codec.DecoderException {
        return Hex.decodeHex(str.toCharArray());
    }
}