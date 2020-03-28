package com.d.lib.devicefeature.nfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class NfcStatusChangeBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_ADAPTER_STATE_CHANGED.equals(action)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                final int status = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, NfcAdapter.STATE_OFF);
                switch (status) {
                    case NfcAdapter.STATE_ON:
                        onNfcOn();
                        break;
                    case NfcAdapter.STATE_OFF:
                        onNfcOff();
                        break;
                    case NfcAdapter.STATE_TURNING_ON:
                        onNfcTurningOn();
                        break;
                    case NfcAdapter.STATE_TURNING_OFF:
                        onNfcTurningOff();
                        break;
                }
            }
        }
    }

    protected void onNfcOn() {

    }

    protected void onNfcOff() {

    }

    protected void onNfcTurningOn() {

    }

    protected void onNfcTurningOff() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static IntentFilter getNfcBroadcastReceiverIntentFilter() {
        return new IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
    }
}
