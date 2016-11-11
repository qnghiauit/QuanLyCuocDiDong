package com.uit.nst95.quanlycuocdidong.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromotionFragment extends Fragment {

    private String SDT = "123";
    private String nhaMang;
    private String NoiDung = "TC";
    private int idImage;
    Switch switchNhanTB;
    SharedPreferences settings;
    public PromotionFragment() {
        // Required empty public constructor
    }

    public static PromotionFragment newInstance() {
        PromotionFragment f = new PromotionFragment();
        return (f);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, 0);
        nhaMang = settings.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);
        if(nhaMang.equals(DefinedConstant.MOBIFONE)){
            SDT = "9241";
            idImage = R.drawable.mobifone;
        }else if (nhaMang.equals(DefinedConstant.VIETTEL)){
            SDT = "199";
            idImage = R.drawable.vietel;
        }else if(nhaMang.equals(DefinedConstant.VINAPHONE)){
            SDT = "18001091";
            idImage = R.drawable.vinaphonne;
        }else if (nhaMang.equals(DefinedConstant.VIETNAMOBILE)){
            SDT = "123";
            NoiDung = "TCQC";
            idImage = R.drawable.vietnamobile;
        }else if (nhaMang.equals(DefinedConstant.GMOBILE)){
            SDT = "123";
            NoiDung = "TCQC";
            idImage = R.drawable.gmobile;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        ImageView imageViewLogo = (ImageView) view.findViewById(R.id.imageViewLogoProvider);
        imageViewLogo.setImageResource(idImage);

        TextView textViewNgayKM = (TextView) view.findViewById(R.id.textViewPromotionDay);
        //write code in here




        TextView textViewHD = (TextView) view.findViewById(R.id.textViewHuongDan);
        textViewHD.setText("Thuê bao " + nhaMang + " soạn tin TC gửi đến " + SDT);

        Button buttonTH = (Button) view.findViewById(R.id.buttonThucHien);
        buttonTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", SDT);
                    smsIntent.putExtra("sms_body",NoiDung);
                    startActivity(smsIntent);
            }
        });

        switchNhanTB = (Switch) view.findViewById(R.id.switchNhanTB);
        switchNhanTB.setChecked(settings.getBoolean(DefinedConstant.KEY_ALLOWRECEIVE, false));

        return view;
    }
    @Override
    public void onStop()
    {
        settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        boolean AllowPopup;
        if (switchNhanTB.isChecked())
            AllowPopup = true;
        else
            AllowPopup = false;
        editor.putBoolean(DefinedConstant.KEY_ALLOWRECEIVE,AllowPopup);
        // Commit the edits!
        editor.commit();
        super.onStop();
    }

}
