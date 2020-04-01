package com.d.lib.devicefeature.nfc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;

import com.d.lib.devicefeature.nfc.callback.NfcCardCallback;
import com.d.lib.devicefeature.nfc.support.NfcJellyBean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class NfcCompat {
    protected final String TAG = this.getClass().getSimpleName();

    public final static int NFC_NOT_EXIT = 0;
    public final static int NFC_NOT_ENABLE = 1;

    @IntDef({NFC_NOT_EXIT, NFC_NOT_ENABLE})
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {

    }

    private static int READER_FLAG = NfcAdapter.FLAG_READER_NFC_A
            | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    protected final Activity mActivity;
    protected final NfcAdapter mDefaultAdapter;
    protected NfcCardCallback mNfcCardCallback;
    private final Bundle extra = new Bundle();

    protected NfcCompat(@NonNull final Builder builder) {
        mActivity = builder.activity;
        mDefaultAdapter = NfcAdapter.getDefaultAdapter(mActivity);
        enablePlatformSound(builder.enableSound);
        setReaderPresenceCheckDelay(builder.delay);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void enableCardReader() {
        if (mDefaultAdapter != null) {
            final NfcAdapter.ReaderCallback readerCallback = new NfcAdapter.ReaderCallback() {
                @Override
                public synchronized void onTagDiscovered(Tag tag) {
                    Log.d(TAG, "onTagDiscovered: tag discovered " + tag);
                    if (mNfcCardCallback != null) {
                        mNfcCardCallback.onTagDiscovered(tag);
                    }
                }
            };
            if (extra.getInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 0) > 0) {
                mDefaultAdapter.enableReaderMode(mActivity, readerCallback, READER_FLAG, extra);
            } else {
                mDefaultAdapter.enableReaderMode(mActivity, readerCallback, READER_FLAG, null);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void disableCardReader() {
        if (mDefaultAdapter != null) {
            mDefaultAdapter.disableReaderMode(mActivity);
        }
    }

    public void dispatchIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            Log.d(TAG, "dispatchIntent: tag discovered " + tag);
            if (mNfcCardCallback != null) {
                mNfcCardCallback.onDispatchIntent(tag);
            }
        }
    }

    protected void enablePlatformSound(boolean enableSound) {
        if (enableSound) {
            READER_FLAG = READER_FLAG & (~NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS);
        } else {
            READER_FLAG = READER_FLAG | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS;
        }
    }

    protected void setReaderPresenceCheckDelay(int delay) {
        extra.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, delay);
    }

    public void setNfcCardCallback(NfcCardCallback listener) {
        mNfcCardCallback = listener;
    }

    public void onDestroy() {
        if (mDefaultAdapter != null) {
            // NfcActivityManager.onActivityDestroyed(mActivity);
        }
    }

    public static final class Builder {
        private Activity activity;
        private boolean enableSound;
        private int delay;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder enableSound(boolean enableSound) {
            this.enableSound = enableSound;
            return this;
        }

        public Builder setReaderPresenceCheckDelay(int delay) {
            if (delay < 0) {
                delay = 0;
            }
            this.delay = delay;
            return this;
        }

        public NfcCompat build() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return new NfcCompat(this);
            } else {
                return new NfcJellyBean(this);
            }
        }
    }

    /**
     * Determine if the phone has NFC function
     *
     * @param context {@link Context}
     * @return {@code true}: Yes {@code false}: No
     */
    public static boolean hasNfcFeature(@NonNull final Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return nfcAdapter != null;
    }

    /**
     * Determine if the phone NFC is on
     *
     * @param context {@link Context}
     * @return {@code true}: Turned on {@code false}: Turned off
     */
    public static boolean isNfcEnable(@NonNull final Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return nfcAdapter != null && nfcAdapter.isEnabled();
    }

    /**
     * Determine if your phone has Android Beam
     *
     * @param context {@link Context}
     * @return {@code true}: Yes {@code false}: No
     */
    public static boolean hasNfcBeamFeature(@NonNull final Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && hasNfcFeature(context);
    }

    /**
     * Determines whether Android Beam for mobile phone NFC is enabled, only after API 16
     *
     * @param context {@link Context}
     * @return {@code true}: Turned on {@code false}: Turned off
     */
    public static boolean isNfcBeamEnable(@NonNull final Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && nfcAdapter != null && nfcAdapter.isNdefPushEnabled();
    }

    /**
     * Jump to system NFC setting interface.
     *
     * @param context {@link Context}
     * @return {@code true} Success <br> {@code false} Failed
     */
    public static boolean intentToNfcSetting(@NonNull final Context context) {
        if (hasNfcFeature(context)) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return toIntent(context, Settings.ACTION_NFC_SETTINGS);
            }
        }
        return false;
    }

    /**
     * Jump to the system NFC Android Beam setting interface, there are basically NFC switches on the same page.
     *
     * @param context {@link Context}
     * @return {@code true} Success <br> {@code false} Failed
     */
    public static boolean intentToNfcShare(@NonNull final Context context) {
        if (hasNfcBeamFeature(context)) {
            return toIntent(context, Settings.ACTION_NFCSHARING_SETTINGS);
        }
        return false;
    }

    private static boolean toIntent(@NonNull final Context context, String action) {
        try {
            Intent intent = new Intent(action);
            context.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
