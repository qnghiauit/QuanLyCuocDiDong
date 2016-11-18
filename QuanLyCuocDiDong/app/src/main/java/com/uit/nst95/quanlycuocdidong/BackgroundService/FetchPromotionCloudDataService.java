package com.uit.nst95.quanlycuocdidong.BackgroundService;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;

import java.io.IOException;

/**
 * Created by TNS on 11/17/2016.
 */

public class FetchPromotionCloudDataService extends Service {

    // service tag
    public static final String FETCH_PROMOTION_DATA_SERVICE_NAME = FetchPromotionCloudDataService.class.getSimpleName();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(FETCH_PROMOTION_DATA_SERVICE_NAME, "Service started ");
        SharedPreferences preferences = this.getSharedPreferences(DefinedConstant.PREFS_NAME, MODE_PRIVATE);
        String provideName = preferences.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);
        new FetchCloudPromotionDataAsyncTask(provideName, this).execute();
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
