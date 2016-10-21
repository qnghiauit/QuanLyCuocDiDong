package com.uit.nst95.quanlycuocdidong.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallbackFragment extends Fragment {

    private TextView textViewGT;
    private TextView textViewHD;
    private TextView textViewTTCT;
    private Button buttonExcute;
    private EditText editTextSDT;
    public CallbackFragment() {
        // Required empty public constructor
    }

    public static CallbackFragment newInstance() {
        CallbackFragment f = new CallbackFragment();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_callback, container, false);
        textViewGT = (TextView) view.findViewById(R.id.textViewGioithieu);
        textViewHD = (TextView) view.findViewById(R.id.textViewHuongDan);
        textViewTTCT = (TextView) view.findViewById(R.id.textViewTTChiTiet);
        buttonExcute = (Button) view.findViewById(R.id.buttonOk);
        editTextSDT = (EditText) view.findViewById(R.id.editTextSDT);
        SharedPreferences settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, MODE_PRIVATE);
        String mangDiDong = settings.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);
        String huongdan = null;
        String gioithieu = null;
        String urlTTCT = null;

        switch (mangDiDong)
        {
            case "Mobifone": {
                gioithieu = getString(R.string.textYCGLMobi);
                huongdan = getString(R.string.textHDMobi) +
                        getString(R.string.textYCGL_HD);
                urlTTCT = getString(R.string.urlTTCTMobi);

                buttonExcute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventClick("*105*");
                    }
                });
                break;
            }
            case "VinaPhone": {
                gioithieu = getString(R.string.textYCGLVina);
                huongdan = getString(R.string.textHDVina) +
                        getString(R.string.textYCGL_HD);
                urlTTCT = getString(R.string.urlTTCTVina);

                buttonExcute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventClick("*110*");
                    }
                });
                break;
            }
            case "Viettel": {
                gioithieu = getString(R.string.textYCGLViettel);
                huongdan = getString(R.string.textHDViettel) +
                        getString(R.string.textYCGL_HD);
                urlTTCT = getString(R.string.urlTTCTViettel);

                buttonExcute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                        smsIntent.setType("vnd.android-dir/mms-sms");
                        smsIntent.putExtra("address", "9119");
                        smsIntent.putExtra("sms_body",editTextSDT.getText().toString());
                        startActivity(smsIntent);
                    }
                });
                break;
            }
            case "GMobile": {
                gioithieu = getString(R.string.textYCGLGmobi);
                huongdan = getString(R.string.textHDGmobi) +
                        getString(R.string.textYCGL_HD);
                urlTTCT = getString(R.string.urlTTCTGmobi);

                buttonExcute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        eventClick("*9119*");
                    }
                });
                break;
            }
            case "VietNamMobile": {
                break;
            }
        }
        if (gioithieu == null)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage(R.string.textServiceNotFound);
            dialog.setPositiveButton(R.string.textOK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            dialog.create().show();
        }
        else
        {
            textViewGT.setText(gioithieu);
            textViewHD.setText(huongdan);
            textViewTTCT.setText(Html.fromHtml(getString(R.string.textTTCTpart2) + urlTTCT + getString(R.string.textTTCTpart1)));
            textViewTTCT.setMovementMethod(LinkMovementMethod.getInstance());
        }

        return view;
    }
    private boolean checkEditTextSDT()
    {
        String SDT = editTextSDT.getText().toString();
        if(SDT.matches(""))
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage(R.string.textNeedFillPhoneNumber);
            dialog.setPositiveButton(R.string.textOK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editTextSDT.requestFocus();
                }
            });
            dialog.create().show();
            return false;
        }
        return true;
    }
    private void eventClick(String string)
    {
        if(checkEditTextSDT())
        {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, MakeLoanFragment.ussdToCallableUri(string + editTextSDT.getText().toString() + "#"));
            getActivity().startActivity(callIntent);
        }
    }

}
