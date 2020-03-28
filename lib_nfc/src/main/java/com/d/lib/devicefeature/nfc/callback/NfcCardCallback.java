package com.d.lib.devicefeature.nfc.callback;

import android.nfc.Tag;
import android.support.annotation.NonNull;

public interface NfcCardCallback {
    void onDispatchIntent(@NonNull Tag tag);

    void onTagDiscovered(@NonNull Tag tag);

    void onTagRemoved();

    void onError(Throwable e);
}
