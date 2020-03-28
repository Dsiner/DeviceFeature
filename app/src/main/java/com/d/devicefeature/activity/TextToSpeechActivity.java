package com.d.devicefeature.activity;

import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.d.devicefeature.R;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.devicefeature.texttospeech.TextToSpeechCompat;

import java.util.Locale;

import butterknife.OnClick;

public class TextToSpeechActivity extends BaseActivity<MvpBasePresenter> implements MvpView {

    // TTS
    private TextToSpeechCompat mTextToSpeechCompat;

    @OnClick({R.id.btn_read_aloud})
    public void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.btn_read_aloud:
                EditText et_text = ViewHelper.findView(this, R.id.et_text);
                String text = et_text.getText().toString();
                mTextToSpeechCompat.speak(text);
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_text_to_speech;
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
        mTextToSpeechCompat = new TextToSpeechCompat();
        mTextToSpeechCompat.init(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Language setting
                    int result = mTextToSpeechCompat.getTextToSpeech().setLanguage(Locale.CHINA);
                    // TextToSpeech.LANG_MISSING_DATA: Data loss for languages
                    // TextToSpeech.LANG_NOT_SUPPORTED: not support
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "Data is missing or not supported", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
