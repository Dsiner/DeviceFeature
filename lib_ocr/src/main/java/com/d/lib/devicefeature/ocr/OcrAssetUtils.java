package com.d.lib.devicefeature.ocr;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OcrAssetUtils {
    private static final String TESSDATA_PATH = OcrUtils.TESSBASE_PATH
            + "/" + OcrUtils.TESSBASE_TESSDATA_NAME;

    private static final String ASSETS_TESSDATA_PATH = "tessdata";

    public static void copy(Context context) {
        isFolderExists(TESSDATA_PATH);
        try {
            String[] list = context.getAssets().list(ASSETS_TESSDATA_PATH);
            for (int i = 0; i < list.length; i++) {
                assetsDataToSD(context, list[i], TESSDATA_PATH + "/" + list[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFolderExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private static void assetsDataToSD(Context context, String assetName, String fileName) throws IOException {
        InputStream input;
        OutputStream output = new FileOutputStream(fileName);
        input = context.getAssets().open(ASSETS_TESSDATA_PATH + "/" + assetName);
        byte[] buffer = new byte[1024];
        int length = input.read(buffer);
        while (length > 0) {
            output.write(buffer, 0, length);
            length = input.read(buffer);
        }
        output.flush();
        input.close();
        output.close();
    }
}
