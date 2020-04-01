package com.d.devicefeature.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.d.devicefeature.R;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ToastUtils;
import com.d.lib.devicefeature.nfc.NfcCompat;
import com.d.lib.devicefeature.nfc.callback.NfcCardCallback;
import com.d.lib.devicefeature.nfc.rw.MifareUltralightUtils;
import com.d.lib.taskscheduler.TaskScheduler;
import com.d.lib.taskscheduler.callback.Observer;
import com.d.lib.taskscheduler.callback.Task;
import com.d.lib.taskscheduler.schedule.Schedulers;

public class NfcActivity extends BaseActivity<MvpBasePresenter> implements MvpView {
    private NfcCompat mNfcCompat;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_nfc;
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

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNfc();
        mNfcCompat.dispatchIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mNfcCompat.dispatchIntent(intent);
    }

    private void initNfc() {
        mNfcCompat = new NfcCompat.Builder(this)
                .enableSound(true)
                .build();
        mNfcCompat.setNfcCardCallback(new NfcCardCallback() {

            @Override
            public void onTagDiscovered(@NonNull Tag tag) {
                readTag(tag);
            }

            private void readTag(@NonNull final Tag tag) {
                TaskScheduler.create(new Task<String>() {
                    @Override
                    public String run() {
                        if (MifareUltralightUtils.isMifareUltralight(tag)) {
                            try {
                                MifareUltralight mifareUltralight = MifareUltralightUtils.getMifareUltralight(tag);
                                mifareUltralight.connect();
                                return MifareUltralightUtils.readSerialNumber(mifareUltralight);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onNext(@NonNull String result) {
                                ToastUtils.toast(getApplicationContext(), "Serial number: " + result);
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.toast(getApplicationContext(), "Read error");
                            }
                        });
            }

            @Override
            public void onTagRemoved() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NfcCompat.hasNfcFeature(this)) {
            mNfcCompat.enableCardReader();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcCompat.hasNfcFeature(this)) {
            mNfcCompat.disableCardReader();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (NfcCompat.hasNfcFeature(this)) {
            mNfcCompat.onDestroy();
        }
    }
}
