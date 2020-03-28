package com.d.devicefeature;

import android.app.Application;

import com.d.lib.aster.Aster;
import com.d.lib.aster.base.Config;
import com.d.lib.aster.utils.SSLUtil;
import com.d.lib.common.util.log.ULog;
import com.d.lib.permissioncompat.support.PermissionSupport;

/**
 * Application
 * Created by D on 2018/3/3.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ULog.setDebug(BuildConfig.DEBUG, "ULog");
        // Runtime permission
        PermissionSupport.setLevel(PermissionSupport.SUPPORT_LEVEL_M_XIAOMI);
    }
}
