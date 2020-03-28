package com.d.lib.devicefeature.nfc.support;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcF;
import android.support.annotation.NonNull;

import com.d.lib.devicefeature.nfc.NfcCompat;

public class NfcJellyBean extends NfcCompat {
    private final PendingIntent mPendingIntent;

    public NfcJellyBean(@NonNull final Builder builder) {
        super(builder);
        mPendingIntent = PendingIntent.getActivity(mActivity, 0,
                new Intent(mActivity, mActivity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                0);
    }

    @Override
    public void enableCardReader() {
        if (mDefaultAdapter != null) {
            String[][] techListsArray = new String[][]{new String[]{NfcF.class.getName()},
                    new String[]{IsoDep.class.getName()}};
            mDefaultAdapter.enableForegroundDispatch(mActivity, mPendingIntent, null, techListsArray);
        }
    }

    @Override
    public void disableCardReader() {
        if (mDefaultAdapter != null) {
            mDefaultAdapter.disableForegroundDispatch(mActivity);
        }
    }
}
