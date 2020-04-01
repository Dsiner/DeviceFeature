package com.d.lib.devicefeature.nfc.rw;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;

import java.io.IOException;
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

    public static MifareUltralight getMifareUltralight(final Tag tag) {
        return MifareUltralight.get(tag);
    }

    public static String readSerialNumber(final MifareUltralight ultralight) throws IOException {
        try {
            byte[] data = ultralight.readPages(0);
            byte[] serialNumber = new byte[7];
            serialNumber[0] = data[0];
            serialNumber[1] = data[1];
            serialNumber[2] = data[2];
            serialNumber[3] = data[4];
            serialNumber[4] = data[5];
            serialNumber[5] = data[6];
            serialNumber[6] = data[7];
            return NfcUtils.bytes2HexString(serialNumber);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String readContent(final MifareUltralight ultralight) throws IOException {
        try {
            byte[] page0 = ultralight.readPages(0);
            byte[] page1 = ultralight.readPages(4);
            byte[] page2 = ultralight.readPages(8);
            byte[] page3 = ultralight.readPages(12);
            byte[] content = NfcUtils.byteMerger(NfcUtils.byteMerger(page0, page1),
                    NfcUtils.byteMerger(page2, page3));
            return NfcUtils.bytes2HexString(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void write(final MifareUltralight ultralight, final int pageOffset, final String text)
            throws IOException {
        try {
            ultralight.writePage(pageOffset, text.getBytes(Charset.forName("GB2312")));
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
}
