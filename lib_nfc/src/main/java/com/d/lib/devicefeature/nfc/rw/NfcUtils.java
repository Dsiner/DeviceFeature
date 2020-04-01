package com.d.lib.devicefeature.nfc.rw;

import java.io.Closeable;

public class NfcUtils {
    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    public static byte[] hexString2Bytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static byte[] byteMerger(final byte[] byte_1, final byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * 16进制转10进制
     *
     * @param hex        16进制
     * @param startIndex 起始下标
     * @param len        长度, 按字符算
     * @return 10进制
     */
    public static int hexToInt(byte[] hex, int startIndex, int len) {
        int ret = 0;

        final int e = startIndex + len;
        for (int i = startIndex; i < e; ++i) {
            ret <<= 8;
            ret |= hex[i] & 0xFF;
        }
        return ret;
    }

    /**
     * 16进制转10进制 (小端模式)
     *
     * @param b 16进制
     * @param s 起始下标
     * @param n 长度, 按字符算
     * @return 10进制
     */
    public static int hexToIntLittleEndian(byte[] b, int s, int n) {
        int ret = 0;
        for (int i = s; (i >= 0 && n > 0); --i, --n) {
            ret <<= 8;
            ret |= b[i] & 0xFF;
        }
        return ret;
    }

    /**
     * Closes {@code closeable}, ignoring any checked exceptions. Does nothing if {@code closeable} is
     * null.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }
}
