package com.d.lib.devicefeature.nfc.rw;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * NfcAUtils
 * Created by D on 2020/3/28.
 */
public class IsoDepUtils {

    private IsoDepUtils() {
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public static boolean isIsoDep(final Tag tag) {
        return Arrays.toString(tag.getTechList()).contains(IsoDep.class.getName());
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    public static String readNfcA(final Tag tag) {
        NfcA nfca = NfcA.get(tag);
        try {
            nfca.connect();
            if (nfca.isConnected()) {
                // NTAG216的芯片
                byte[] SELECT = {
                        (byte) 0x30,
                        (byte) 5 & 0x0ff, // 0x05
                };
                byte[] response = nfca.transceive(SELECT);
                nfca.close();
                if (response != null) {
                    return new String(response, Charset.forName("US-ASCII"));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }
}
