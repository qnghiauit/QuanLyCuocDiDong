package com.uit.nst95.quanlycuocdidong.Manager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;

import static android.R.attr.fragment;

/**
 * Created by QNghia on 28/09/2016.
 */

public final class DefinedConstant {

    public static final String PREFS_NAME = "QLCDD_Setting";

    public static final String KEY_PACKAGE = "GoiCuoc";
    public static final String KEY_PROVIDER = "NhaMang";
    public static final String BUNDLE_NAME = "TTTB";

    public static final String KEY_IDIMAGE = "IDImage";
    public static final String KEY_ALLOWPOPUP = "AllowPopup";
    public static final String KEY_UPDATE_STATE = "UpdateState";
    public static final String KEY_LAST_TIME_UPDATE_CALL = "LastUpdateCall";
    public static final String KEY_LAST_TIME_UPDATE_MESSAGE = "LastUpdateMessage";

    public static final String VALUE_DEFAULT = "Notfound";
    public static final long TIME_DEDAULT = 0;

    //Networl Provider
    public static final String MOBIFONE = "Mobifone";
    public static final String VINAPHONE = "VinaPhone";
    public static final String VIETTEL = "Viettel";
    public static final String GMOBILE = "GMobile";
    public static final String VIETNAMOBILE = "VietNamobile";

    //PackageNetwork
    public static final String MOBICARD = "Mobicard";
    public static final String MOBIGOLD = "MobiGold";
    public static final String MOBIQ = "MobiQ";
    public static final String QSTUDENT = "Q-Student";
    public static final String QTEEN = "Q-Teen";
    public static final String QKIDS = "Q-Kids";
    public static final String VINACARD = "VinaCard";
    public static final String VINAXTRA = "VinaXtra";
    public static final String TALKTEEN = "TalkTeen";
    public static final String TALKSTUDENT = "TalkStudent";
    public static final String ECONOMY = "Economy";
    public static final String TOMATO = "Tomato";
    public static final String STUDENT = "Student";
    public static final String SEA = "Sea+";
    public static final String HISCHOOL = "Hi School";
    public static final String SEVENCOLOR = "7Colors";
    public static final String BUONLANG = "Buôn làng";
    public static final String BIGSAVE = "Big Save";
    public static final String BIGKOOL = "Big & Kool";
    public static final String TIPHU2 = "Tỉ phú 2";
    public static final String TIPHU3 ="Tỉ phú 3";
    public static final String TIPHU5 = "Tỉ phú 5";
    public static final String VMONE = "VM One";
    public static final String VMAX = "VMax";
    public static final String SV2014 = "SV 2014";

    //Mutual Func
    public static void sendSmS(Activity activity, String address, String sms_body)
    {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", address);
        smsIntent.putExtra("sms_body", sms_body);
        activity.startActivity(smsIntent);
    }
    public static void callUSSD(Activity activity, String ussd)
    {
        String uriString = "";
        if(!ussd.startsWith("tel:"))
            uriString += "tel:";
        for(char c : ussd.toCharArray()) {

            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }
        Uri uriCode = Uri.parse(uriString);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, uriCode);
        activity.startActivity(callIntent);
    }
}
