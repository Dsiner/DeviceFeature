package com.d.lib.devicefeature.texttospeech;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Locale;

/**
 * TextToSpeechCompat
 * Created by D on 2020/3/7.
 */
public class TextToSpeechCompat {
    private TextToSpeech mTextToSpeech;

    public TextToSpeechCompat() {
    }

    public void init(final Context context, final TextToSpeech.OnInitListener listener) {
        mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (listener != null) {
                    listener.onInit(status);
                    return;
                }
                if (status == TextToSpeech.SUCCESS) {
                    // Language setting
                    int result = mTextToSpeech.setLanguage(Locale.CHINA);
                    // TextToSpeech.LANG_MISSING_DATA: Data loss for languages
                    // TextToSpeech.LANG_NOT_SUPPORTED: not support
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, "Data is missing or not supported", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        // Set the pitch. The larger the value, the sharper the sound (girl).
        // The smaller the value, the male voice becomes. 1.0 is normal.
        mTextToSpeech.setPitch(1.0f);
        // Set the speaking rate
        mTextToSpeech.setSpeechRate(0.5f);
    }

    public TextToSpeech getTextToSpeech() {
        return mTextToSpeech;
    }

    /**
     * TextToSpeech's speak method has two overloads.
     * Method of reading aloud
     * speak (CharSequence text, int queueMode, Bundle params, String utteranceId);
     * <p>
     * Record the spoken voice as an audio file
     * synthesizeToFile (CharSequence text, Bundle params, File file, String utteranceId);
     * <p>
     * The second parameter queueMode is used to specify the pronunciation queue mode, two modes are selected
     * (1) TextToSpeech.QUEUE_FLUSH: In this mode, when there is a new task, the current voice task will be cleared and a new voice task will be executed.
     * (2) TextToSpeech.QUEUE_ADD: In this mode, a new voice task will be placed after the voice task, wait for the previous voice task to complete before executing a new voice task
     */
    public void speak(final String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (mTextToSpeech == null || mTextToSpeech.isSpeaking()) {
            return;
        }
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onStop() {
        // Interrupted whether or not TTS is being read
        mTextToSpeech.stop();
        // Close, release resources
        mTextToSpeech.shutdown();
    }
}
