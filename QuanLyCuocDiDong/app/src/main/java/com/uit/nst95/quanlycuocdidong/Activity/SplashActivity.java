package com.uit.nst95.quanlycuocdidong.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.R;

public class SplashActivity extends Activity {

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
