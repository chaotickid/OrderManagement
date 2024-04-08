package com.infinity.common.utils.passwordencutils;

import org.apache.commons.codec.DecoderException;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @author Aditya Patil
 */

public class PasswordEncoderUtils {

    private static final String PASSPHRASE = getDecodedPassphrase();

    private static final String SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";

    private static final String PADDING = "F27D5C9927726BCEFE7510B1BDD3D137";

    private PasswordEncoderUtils() {
    }

    protected static String getDecodedPassphrase() {
        return new String(Base64.decodeBase64(CoreConstants.getEncodedBytes()));
    }

    public static String encrypt(String plainText) {
        AESEncoderUtils util = new AESEncoderUtils();
        try {
            return util.encrypt(SALT, PADDING, PASSPHRASE, plainText);
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String decrypt(String cipherText) {
        AESEncoderUtils util = new AESEncoderUtils();
        try {
            return util.decrypt(SALT, PADDING, PASSPHRASE, cipherText);
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }
}