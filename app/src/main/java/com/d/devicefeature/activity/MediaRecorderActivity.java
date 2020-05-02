package com.d.devicefeature.activity;

import android.view.View;

import com.d.devicefeature.App;
import com.d.devicefeature.R;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.mediarecorder.audio.MP3Recorder;

import java.io.File;
import java.io.IOException;

public class MediaRecorderActivity extends BaseActivity<MvpBasePresenter>
        implements MvpView, View.OnClickListener {

    private MP3Recorder mMP3Recorder;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                try {
                    mMP3Recorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_stop:
                mMP3Recorder.stop();
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_media_recorder;
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
    protected void bindView() {
        super.bindView();
        ViewHelper.setOnClick(this, this, R.id.btn_start, R.id.btn_stop);
    }

    @Override
    protected void init() {
        final String path = App.PATH + "/MediaRecorder";
        final File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final File file = new File(path + "/MP3Recorder.mp3");
        mMP3Recorder = new MP3Recorder(file);
    }

    @Override
    public void finish() {
        mMP3Recorder.stop();
        super.finish();
    }
}
