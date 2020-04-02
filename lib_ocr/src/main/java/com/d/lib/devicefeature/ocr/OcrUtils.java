package com.d.lib.devicefeature.ocr;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

public class OcrUtils {
    // 训练数据路径, 必须包含tessdata文件夹
    public static final String TESSBASE_PATH = Environment.getExternalStorageDirectory().getPath()
            + "/DeviceFeature/Ocr";
    public static final String TESSBASE_TESSDATA_NAME = "tessdata";

    private static final String DEFAULT_LANGUAGE = "eng"; // 英文数据包
    private static final String CHINESE_LANGUAGE = "chi_sim"; // 中文数据包

    public static String ocr(Bitmap bitmap, String ocr_location) {
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
        switch (ocr_location) {
            case "id_num":
                baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
                baseApi.setVariable("tessedit_char_whitelist", "0123456789Xx");
                break;
            case "sex":
                baseApi.init(TESSBASE_PATH, CHINESE_LANGUAGE);
                baseApi.setVariable("tessedit_char_whitelist", "男女");
                break;
            case "birth":
                baseApi.init(TESSBASE_PATH, DEFAULT_LANGUAGE);
                baseApi.setVariable("tessedit_char_whitelist", "0123456789");
                break;
            case "minority":
                baseApi.init(TESSBASE_PATH, CHINESE_LANGUAGE);
                baseApi.setVariable("tessedit_char_whitelist", "汉壮满回苗维吾尔土家彝蒙古藏布依侗瑶朝鲜白哈尼哈萨克黎傣畲傈僳仡佬东乡高山拉祜水佤纳西羌土仫佬锡伯柯尔克孜达斡尔景颇毛南撒拉布朗塔吉克阿昌普米鄂温克怒京基诺德昂保安俄罗斯裕固乌孜别克门巴鄂伦春独龙塔塔尔赫哲珞巴族");
                break;
            case "id_card":
                baseApi.init(TESSBASE_PATH, CHINESE_LANGUAGE);
                baseApi.setVariable("tessedit_char_blacklist", "。，、＇：∶；?‘’“”〝〞ˆˇ﹕︰﹔﹖﹑·¨….¸;！´？！～—ˉ｜‖＂〃｀@﹫¡¿﹏﹋﹌︴々﹟#﹩$﹠&﹪%*﹡﹢﹦﹤‐￣¯―﹨ˆ˜﹍﹎+=<＿_-ˇ~﹉﹊（）〈〉‹›﹛﹜『』〖〗［］《》〔〕{}「」【】︵︷︿︹︽_﹁﹃︻︶︸﹀︺︾ˉ﹂﹄︼ ");
                break;
            default:
                baseApi.init(TESSBASE_PATH, CHINESE_LANGUAGE);
                break;
        }
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        baseApi.setImage(bitmap);
        String result = baseApi.getUTF8Text();
        baseApi.clear();
        baseApi.end();
        return result;
    }

    /**
     * 灰度化处理
     */
    public static Bitmap convertGray(Bitmap bitmap) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

        Paint paint = new Paint();
        paint.setColorFilter(filter);
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        canvas.drawBitmap(bitmap, 0, 0, paint);
        return result;
    }
}
