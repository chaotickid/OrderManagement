package com.infinity.common.utils.passwordencutils;

/**
 * @author Aditya Patil
 */
public class CoreConstants {

    private static final byte[] ENCODED_BYTES = {86, 71, 104, 112, 99, 121, 66, 112, 99, 121, 66, 109, 98, 51, 74, 108,
            99, 50, 108, 110, 97, 72, 81, 103, 90, 109, 57, 121, 73, 71, 86, 117, 89, 51, 74, 53, 99, 72, 81, 103, 89,
            87, 53, 107, 73, 71, 82, 108, 89, 51, 74, 53, 99, 72, 81, 61};

    public static byte[] getEncodedBytes() {
        return ENCODED_BYTES;
    }
}