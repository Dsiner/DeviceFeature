package com.d.lib.devicefeature.nfc;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.annotation.NonNull;

import com.d.lib.devicefeature.nfc.callback.NfcCardCallback;

public abstract class CardReader {
    protected final String TAG = this.getClass().getSimpleName();

    protected final Activity mActivity;
    protected final NfcAdapter mDefaultAdapter;
    protected NfcCardCallback mNfcCardCallback;

    public CardReader(@NonNull final Activity activity) {
        mActivity = activity;
        mDefaultAdapter = NfcAdapter.getDefaultAdapter(activity);
    }

    public boolean isCardConnected() {
        return false;
    }

    protected void enablePlatformSound(boolean enableSound) {
    }

    protected void setReaderPresenceCheckDelay(int delay) {
    }

    protected abstract void enableCardReader();

    protected abstract void disableCardReader();

    protected abstract void dispatchTag(Tag tag);
}
