package com.uit.nst95.quanlycuocdidong.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.R;

public class ChooseNetworkProviderActivity extends Activity {

    private ImageButton imageBtnMobi;
    private ImageButton imageBtnVina;
    private ImageButton imageBtnViettel;
    private ImageButton imageBtnGMobi;
    private ImageButton imageBtnVNMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_network_provider);
        editActionBar();
        getControl();
        addEvents();
    }
    private void getControl() {

        imageBtnMobi = (ImageButton) findViewById(R.id.imageButtonMobifone);
        imageBtnVina = (ImageButton) findViewById(R.id.imageButtonVinaPhone);
        imageBtnViettel = (ImageButton) findViewById(R.id.imageButtonViettel);
        imageBtnGMobi = (ImageButton) findViewById(R.id.imageButtonGMobile);
        imageBtnVNMobile = (ImageButton) findViewById(R.id.imageButtonVietnamMobile);
    }
    private void addEvents()
    {
        imageBtnMobi.setOnClickListener(new ProcessMyEvent());
        imageBtnVina.setOnClickListener(new ProcessMyEvent());
        imageBtnViettel.setOnClickListener(new ProcessMyEvent());
        imageBtnGMobi.setOnClickListener(new ProcessMyEvent());
        imageBtnVNMobile.setOnClickListener(new ProcessMyEvent());
    }
    private class ProcessMyEvent implements View.OnClickListener
    {
        @Override
        public void onClick(View arg0) {
            String provider = "";
            switch(arg0.getId())
            {
                case R.id.imageButtonMobifone:
                    provider = DefinedConstant.MOBIFONE;
                    break;
                case R.id.imageButtonVinaPhone:
                    provider = DefinedConstant.VINAPHONE;
                    break;
                case R.id.imageButtonViettel:
                    provider = DefinedConstant.VIETTEL;
                    break;
                case R.id.imageButtonGMobile:
                    provider = DefinedConstant.GMOBILE;
                    break;
                case R.id.imageButtonVietnamMobile:
                    provider = DefinedConstant.VIETNAMOBILE;
                    break;
            }
            Bundle myBundle = new Bundle();
            myBundle.putString(DefinedConstant.KEY_PROVIDER, provider);
            Intent myIntent = new Intent(ChooseNetworkProviderActivity.this,ChoosePackageActivity.class);
            myIntent.putExtra(DefinedConstant.BUNDLE_NAME,myBundle);
            startActivity(myIntent);
        }

    }
    private void editActionBar()
    {
        ActionBar bar = getActionBar();
        if (bar != null) {
            bar.setDisplayShowHomeEnabled(false);
            bar.setDisplayShowCustomEnabled(false);
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));
            bar.setTitle(Html.fromHtml("<font color='#FFFFFF'>" +
                                        getString(R.string.activity_choosenetworkprovider_titlebar)
                                        + "</font>"));
        }
    }
}
