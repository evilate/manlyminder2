package eu.allan.apps.manlyminder2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class _MyStartServiceReceiver extends BroadcastReceiver {
    private static String TAG = "MyStartServiceReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Broadcast");
        _Util.scheduleJob(context);
    }
}