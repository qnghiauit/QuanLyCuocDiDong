package com.uit.nst95.quanlycuocdidong.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.DB.DAO_Statistic;
import com.uit.nst95.quanlycuocdidong.DB.Statistic;
import com.uit.nst95.quanlycuocdidong.Manager.DateTimeManager;
import com.uit.nst95.quanlycuocdidong.R;

import org.joda.time.DateTime;

import java.text.DecimalFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment f = new HomeFragment();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        DecimalFormat formatter = new DecimalFormat("#,###,###");

        TextView textViewTongTienThang = (TextView) view.findViewById(R.id.textViewTongTienThang);
        TextView textViewTienGoiNoiMang = (TextView) view.findViewById(R.id.textViewTienGoiNoiMang);
        TextView textViewTienGoiNgoaiMang = (TextView) view.findViewById(R.id.textViewTienGoiNgoaiMang);
        TextView textViewNhanTinNoiMang = (TextView) view.findViewById(R.id.textViewTienSmSNoiMang);
        TextView textViewNhanTinNgoaiMang = (TextView) view.findViewById(R.id.textViewTienSmsNgoaiMang);

        DAO_Statistic dao_statistic = new DAO_Statistic(getContext());
        dao_statistic.Open();
        Statistic monthStatistics = dao_statistic.FindStatisticByMonthYear(DateTime.now().getMonthOfYear(),DateTime.now().getYear());
        textViewTongTienThang.setText(formatter.format(monthStatistics.get_totalCost()) + "đ");
        textViewTienGoiNoiMang.setText(formatter.format(monthStatistics.get_innerCallFee()) + "đ cho " + DateTimeManager.get_instance().convertToMinutesAndSec(monthStatistics.get_innerCallDuration(),false) + " gọi nội mạng");
        textViewTienGoiNgoaiMang.setText(formatter.format(monthStatistics.get_outerCallFee()) + "đ cho " + DateTimeManager.get_instance().convertToMinutesAndSec(monthStatistics.get_outerCallDuration(),false) + " gọi ngoại mạng");
        textViewNhanTinNoiMang.setText(formatter.format(monthStatistics.get_innerMessageFee()) + "đ cho " + monthStatistics.get_innerMessageCount() + " tin nhắn nội mạng");
        textViewNhanTinNgoaiMang.setText(formatter.format(monthStatistics.get_outerMessageFee()) + "đ cho " + monthStatistics.get_outerMessageCount() + " tin nhắn ngoại mạng");

        ImageButton buttonSetting = (ImageButton) view.findViewById(R.id.imageButtonCaiDat);
        ImageButton buttonThongKe = (ImageButton) view.findViewById(R.id.imageButtonThongKe);
        ImageButton buttonTraTheoNgay = (ImageButton) view.findViewById(R.id.imageButtonTraTheoNgay);
        ImageButton buttonNapBangCamera = (ImageButton) view.findViewById(R.id.imageButtonNapTienCamera);
        ImageButton buttonKhuyenMai = (ImageButton) view.findViewById(R.id.imageButtonKhuyenMai);
        ImageButton buttonUngTien = (ImageButton) view.findViewById(R.id.imageButtonUngTien);
        ImageButton buttonSDTHuuIch = (ImageButton) view.findViewById(R.id.imageButtonSDTHuuIch);
        ImageButton buttonDangKy3G = (ImageButton) view.findViewById(R.id.imageButtonDK3G);

        buttonSetting.setOnClickListener(new ShortcutOnClick());
        buttonThongKe.setOnClickListener(new ShortcutOnClick());
        buttonTraTheoNgay.setOnClickListener(new ShortcutOnClick());
        buttonNapBangCamera.setOnClickListener(new ShortcutOnClick());
        buttonKhuyenMai.setOnClickListener(new ShortcutOnClick());
        buttonUngTien.setOnClickListener(new ShortcutOnClick());
        buttonSDTHuuIch.setOnClickListener(new ShortcutOnClick());
        buttonDangKy3G.setOnClickListener(new ShortcutOnClick());

        return view;
    }

    private class ShortcutOnClick implements View.OnClickListener
    {
        @Override
        public void onClick(View arg0) {
            int id = 11;
            switch(arg0.getId())
            {
                case R.id.imageButtonCaiDat:
                    id = 11;
                    break;
                case R.id.imageButtonThongKe:
                    id = 2;
                    break;
                case R.id.imageButtonTraTheoNgay:
                    id = 3;
                    break;
                case R.id.imageButtonNapTienCamera:
                    id = 10;
                    break;
                case R.id.imageButtonKhuyenMai:
                    id = 4;
                    break;
                case R.id.imageButtonUngTien:
                    id = 5;
                    break;
                case R.id.imageButtonSDTHuuIch:
                    id = 8;
                    break;
                case R.id.imageButtonDK3G:
                    id = 9;
                    break;
            }
            ((MainActivity)getActivity()).ChangeFragment(id);
        }

    }

}
