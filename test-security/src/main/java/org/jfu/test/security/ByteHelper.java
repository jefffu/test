package org.jfu.test.security;

import java.math.BigInteger;
import java.util.Formatter;

public class ByteHelper {

    public static String toHex(byte[] bytes) {
        Formatter formatter = null;
        try {
            formatter = new Formatter();
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        } finally {
            formatter.close();
        }
    }

    public static byte[] toByteArray(String hex) {
        return new BigInteger(hex, 16).toByteArray();
    }

}
