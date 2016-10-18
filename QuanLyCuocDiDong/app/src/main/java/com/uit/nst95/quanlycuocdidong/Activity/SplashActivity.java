package com.uit.nst95.quanlycuocdidong.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.R;
import com.uit.nst95.quanlycuocdidong.tesstwo.TessTwoORCFactory;

import java.io.IOException;

public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName(); // tag for logging
    private String _package;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences settings = getSharedPreferences(DefinedConstant.PREFS_NAME, MODE_PRIVATE);
        _package = settings.getString(DefinedConstant.KEY_PACKAGE, DefinedConstant.VALUE_DEFAULT);
        new CountDownTimer(3000, 3000) {

            public void onTick(long millisUntilFinished) {
                //Do nothing
            }

            public void onFinish() {
                AsyncTask<Void, Void, String> copyTrainedDataFilesAsync = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        try {
                            TessTwoORCFactory.createTessTwoTrainedDataRepository(SplashActivity.this);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Failed to create trained data in device storage");
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        Toast.makeText(SplashActivity.this, "Created trained data files", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Trained data file created");
                        super.onPostExecute(s);
                    }
                };

                copyTrainedDataFilesAsync.execute(); // start the task
                Class intentClass;
                if (_package.equals(DefinedConstant.VALUE_DEFAULT))
                    intentClass = ChooseNetworkProviderActivity.class;
                else
                    intentClass = MainActivity.class;
                Intent myIntent = new Intent(SplashActivity.this, intentClass);
                startActivity(myIntent);
            }
        }.start();
    }
}
