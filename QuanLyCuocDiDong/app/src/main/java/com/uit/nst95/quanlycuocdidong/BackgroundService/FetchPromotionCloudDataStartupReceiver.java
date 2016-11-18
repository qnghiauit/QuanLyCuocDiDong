package com.uit.nst95.quanlycuocdidong.BackgroundService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * To ensure that our code is executed at the desired time, we can’t rely on the {@link android.app.Service}
 * because the OS could kill the Service to free up memory. We must use the OS to schedule the execution,
 * and to do this we must use the android.app.{@link android.app.AlarmManager} class.
 * This system service is like the layout inflator or notification manager services.
 *
 * @author TNS
 */

public class FetchPromotionCloudDataStartupReceiver extends BroadcastReceiver {
    private static final String TAG = FetchPromotionCloudDataStartupReceiver.class.getSimpleName(); // TAG
    private static final int INTERVAL = 5 * 60 * 1000; // service will run in every 5 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Device booted ");
//        By specifying the type of alarm as RTC_WAKEUP, we’re instructing the OS to
//        execute this alarm even if the device has been put to sleep (that’s what the wakeup suffix
//                represents; the RTC part says we’re measuring start time in absolute system time).
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0,
                alarmReceiverIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL, sender);

    }
}

