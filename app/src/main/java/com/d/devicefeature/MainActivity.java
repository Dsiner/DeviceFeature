package com.d.devicefeature;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;

import com.d.devicefeature.activity.MediaRecorderActivity;
import com.d.devicefeature.activity.NfcActivity;
import com.d.devicefeature.activity.OcrActivity;
import com.d.devicefeature.activity.TextToSpeechActivity;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.view.dialog.AlertDialogFactory;
import com.d.lib.devicefeature.motiondetection.MotionDetectionActivity;
import com.d.lib.permissioncompat.Permission;
import com.d.lib.permissioncompat.PermissionCompat;
import com.d.lib.permissioncompat.PermissionSchedulers;
import com.d.lib.permissioncompat.callback.PermissionCallback;

import butterknife.OnClick;

public class MainActivity extends BaseActivity<MvpBasePresenter> implements MvpView {

    public final static String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @OnClick({R.id.btn_text_to_speech, R.id.btn_motion_detection, R.id.btn_nfc,
            R.id.btn_media_recorder, R.id.btn_ocr})
    public void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.btn_text_to_speech:
                startActivity(new Intent(MainActivity.this, TextToSpeechActivity.class));
                break;

            case R.id.btn_motion_detection:
                startActivity(new Intent(MainActivity.this, MotionDetectionActivity.class));
                break;

            case R.id.btn_nfc:
                startActivity(new Intent(MainActivity.this, NfcActivity.class));
                break;

            case R.id.btn_media_recorder:
                startActivity(new Intent(MainActivity.this, MediaRecorderActivity.class));
                break;

            case R.id.btn_ocr:
                startActivity(new Intent(MainActivity.this, OcrActivity.class));
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public MvpBasePresenter getPresenter() {
        return new MvpBasePresenter(getApplicationContext());
    }

    @Override
    protected MvpView getMvpView() {
        return this;
    }

    @Override
    protected void init() {
        selfPermissions();
    }

    /**
     * Runtime permission self-check
     */
    private void selfPermissions() {
        PermissionCompat.with(this)
                .requestEachCombined(PERMISSIONS)
                .subscribeOn(PermissionSchedulers.io())
                .observeOn(PermissionSchedulers.mainThread())
                .requestPermissions(new PermissionCallback<Permission>() {
                    @Override
                    public void onNext(Permission permission) {
                        if (!permission.granted) {
                            Dialog dlg = AlertDialogFactory.createFactory(MainActivity.this)
                                    .getAlertDialog("Tips",
                                            "The application obtains your storage permissions, etc."
                                                    + " only for providing services or improving the service experience,"
                                                    + " please enable the relevant permissions in the settings",
                                            "Understood", null,
                                            new AlertDialogFactory.OnClickListener() {
                                                @Override
                                                public void onClick(Dialog dlg, View v) {
                                                    finish();
                                                }
                                            }, null);
                            dlg.setCancelable(false);
                            dlg.setCanceledOnTouchOutside(false);
                        }
                    }
                });
    }
}
