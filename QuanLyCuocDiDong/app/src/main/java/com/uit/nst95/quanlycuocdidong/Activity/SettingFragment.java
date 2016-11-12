package com.uit.nst95.quanlycuocdidong.Activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.NetworkPackage.PackageFee;
import com.uit.nst95.quanlycuocdidong.R;

import java.text.DecimalFormat;

import static android.R.attr.fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    private String goiCuoc;
    private String nhaMang;
    private int idImage;
    private PackageFee _myPackageFee;

    Switch switchPopup;
    SharedPreferences settings;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance(PackageFee myPackageFee) {
        SettingFragment f = new SettingFragment();
        Bundle args = new Bundle();
        args.putSerializable(DefinedConstant.BUNDLE_NAME,myPackageFee);
        f.setArguments(args);
        return (f);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            _myPackageFee = (PackageFee)getArguments().getSerializable(DefinedConstant.BUNDLE_NAME);
        }
        settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, 0);
        goiCuoc = settings.getString(DefinedConstant.KEY_PACKAGE, DefinedConstant.VALUE_DEFAULT);
        nhaMang = settings.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        TextView textViewGoiCuoc = (TextView) view.findViewById(R.id.textViewTenGoiCuoc);
        textViewGoiCuoc.setText(goiCuoc);
        TextView textViewTenNhaMang = (TextView) view.findViewById(R.id.textViewTenNhaMang);
        textViewTenNhaMang.setText(nhaMang);
        TextView textViewPhiGoiNoiMang = (TextView) view.findViewById(R.id.textViewPhiGoiNoiMang);
        textViewPhiGoiNoiMang.setText(formatter.format(_myPackageFee.get_internalCallFee()));
        TextView textViewPhiGoiNgoaiMang = (TextView) view.findViewById(R.id.textViewPhiGoiNgoaiMang);
        textViewPhiGoiNgoaiMang.setText(formatter.format(_myPackageFee.get_outerCallFee()));
        TextView textViewPhiNhanTinNoiMang = (TextView) view.findViewById(R.id.textViewPhiNhanTinNoiMang);
        textViewPhiNhanTinNoiMang.setText(formatter.format(_myPackageFee.get_internalMessageFee()));
        TextView textViewPhiNhanTinNgoaiMang = (TextView) view.findViewById(R.id.textViewPhiNhanTinNgoaiMang);
        textViewPhiNhanTinNgoaiMang.setText(formatter.format(_myPackageFee.get_outerMessageFee()));

        Button buttonChangeMobileNW = (Button) view.findViewById(R.id.buttonClear);
        buttonChangeMobileNW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).ResetProvider();
            }
        });

        switchPopup = (Switch) view.findViewById(R.id.switchPopUp);
        switchPopup .setChecked(settings.getBoolean(DefinedConstant.KEY_ALLOWPOPUP, false));
        return view;
    }
    @Override
    public void onStop()
    {
        settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        boolean AllowPopup;
        if (switchPopup.isChecked())
            AllowPopup = true;
        else
            AllowPopup = false;
        editor.putBoolean(DefinedConstant.KEY_ALLOWPOPUP,AllowPopup);
        // Commit the edits!
        editor.commit();
        super.onStop();
    }

}
