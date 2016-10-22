package com.uit.nst95.quanlycuocdidong.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MakeLoanFragment extends Fragment {

    private TextView textViewGT;
    private TextView textViewHD;
    private TextView textViewTTCT;
    private Button buttonExcute;
    public MakeLoanFragment() {
        // Required empty public constructor
    }

    public static MakeLoanFragment newInstance() {
        MakeLoanFragment f = new MakeLoanFragment();return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_loan, container, false);

        textViewGT = (TextView) view.findViewById(R.id.textViewGioithieu);
        textViewHD = (TextView) view.findViewById(R.id.textViewHuongDan);
        textViewTTCT = (TextView) view.findViewById(R.id.textViewTTChiTiet);
        buttonExcute = (Button) view.findViewById(R.id.buttonOk);
        SharedPreferences settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, MODE_PRIVATE);
        String mangDiDong = settings.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);
        String huongdan = null;
        String gioithieu = null;
        String urlTTCT = null;
        switch (mangDiDong)
        {
            case DefinedConstant.MOBIFONE: {
                gioithieu = getString(R.string.textUngTienMobi);
                huongdan = getString(R.string.textUngTienHDMobi);
                urlTTCT = getString(R.string.urlUngTienMobi);

                buttonExcute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSmS("900", "UT");
                    }
                });
                break;
            }
            case DefinedConstant.VINAPHONE: {
                gioithieu = getString(R.string.textUngTienVina);
                huongdan = getString(R.string.textUngTienHDVina);
                urlTTCT = getString(R.string.urlUngTienVina);

                buttonExcute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSmS("1576", "Y");
                    }
                });
                break;
            }
            case DefinedConstant.VIETTEL: {
                gioithieu = getString(R.string.textUngTienViettel);
                huongdan = getString(R.string.textUngTienHDViettel);
                urlTTCT = getString(R.string.urlUngTienViettel);

                buttonExcute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callUSSD("*911#");
                    }
                });
                break;
            }
            case DefinedConstant.GMOBILE: { //khong co dich vu nay
                break;
            }
            case DefinedConstant.VIETNAMOBILE: {
                gioithieu = getString(R.string.textUngTienVNMobi);
                huongdan = getString(R.string.textUngTienHDVNMobi);
                urlTTCT = getString(R.string.urlUngTienVNMobi);

                buttonExcute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callUSSD("*911#");
                    }
                });
                break;
            }
        }
        if (gioithieu == null)
        {
            view = inflater.inflate(R.layout.fragment_not_found, container, false);
        }
        else
        {
            textViewGT.setText(gioithieu);
            textViewHD.setText(huongdan);
            textViewTTCT.setText(Html.fromHtml(getString(R.string.textTTCTpart2) + urlTTCT + getString(R.string.textTTCTpart1)));
            textViewTTCT.setMovementMethod(LinkMovementMethod.getInstance());
        }
        buttonExcute.setText("Thực hiện");
        return view;
    }
    private void sendSmS(String address, String sms_body)
    {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", address);
        smsIntent.putExtra("sms_body",sms_body);
        startActivity(smsIntent);
    }
    private void callUSSD(String code)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL, ussdToCallableUri(code));
        startActivity(callIntent);
    }
    public static Uri ussdToCallableUri(String ussd) {

        String uriString = "";

        if(!ussd.startsWith("tel:"))
            uriString += "tel:";

        for(char c : ussd.toCharArray()) {

            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }

}
