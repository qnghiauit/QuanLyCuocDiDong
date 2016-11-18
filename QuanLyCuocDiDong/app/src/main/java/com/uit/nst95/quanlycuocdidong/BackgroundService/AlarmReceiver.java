package com.uit.nst95.quanlycuocdidong.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

/**
 * Created by TNS on 11/17/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static PowerManager.WakeLock wakeLock = null;
    private static final String LOCK_TAG = AlarmReceiver.class.getSimpleName();

    /**
     * Method used to share the <code>WakeLock</code> created by this
     * <code>BroadcastReceiver</code>.
     * Note: this method is <code>synchronized</code> as it lazily creates
     * the <code>WakeLock</code>
     *
     * @param ctx The <code>Context</code> object acquiring the
     *            lock.
     */
    public static synchronized void acquireLock(Context ctx) {
        if (wakeLock == null) {
            PowerManager mgr = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
            wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, LOCK_TAG);
            wakeLock.setReferenceCounted(true);
        }
        wakeLock.acquire();
    }

    /**
     * Method used to release the shared <code>WakeLock</code>.
     */
    public static synchronized void releaseLock() {
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        acquireLock(context);
        Intent cloudService = new Intent(context, FetchPromotionCloudDataService.class);
        context.startService(cloudService);
    }
}
