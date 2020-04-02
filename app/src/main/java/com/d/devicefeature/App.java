package com.d.devicefeature;

import android.app.Application;
import android.os.Environment;

import com.d.lib.common.util.log.ULog;
import com.d.lib.permissioncompat.support.PermissionSupport;

/**
 * Application
 * Created by D on 2018/3/3.
 */
public class App extends Application {

    public final static String PATH = Environment.getExternalStorageDirectory().getPath()
            + "/DeviceFeature";

    @Override
    public void onCreate() {
        super.onCreate();
        ULog.setDebug(BuildConfig.DEBUG, "ULog");
        // Runtime permission
        PermissionSupport.setLevel(PermissionSupport.SUPPORT_LEVEL_M_XIAOMI);
    }
}
