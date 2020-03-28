package com.d.lib.devicefeature.nfc.rw;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * MifareUltralightUtils
 * Created by D on 2020/3/28.
 */
public class MifareUltralightUtils {

    private MifareUltralightUtils() {
    }

    public static boolean isMifareUltralight(final Tag tag) {
        return Arrays.toString(tag.getTechList()).contains(MifareUltralight.class.getName());
    }

    public static String readSerialNumber(Tag tag) {
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            byte[] data = ultralight.readPages(0);
            byte[] serialNumber = new byte[7];
            serialNumber[0] = data[0];
            serialNumber[1] = data[1];
            serialNumber[2] = data[2];
            serialNumber[3] = data[4];
            serialNumber[4] = data[5];
            serialNumber[5] = data[6];
            serialNumber[6] = data[7];
            return Util.bytes2HexString(serialNumber);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(ultralight);
        }
        return null;
    }

    public static void write(final int pageOffset, final Tag tag, final String text) {
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            ultralight.writePage(pageOffset, text.getBytes(Charset.forName("GB2312")));
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(ultralight);
        }
    }
}
