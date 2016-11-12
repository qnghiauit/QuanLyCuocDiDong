package com.uit.nst95.quanlycuocdidong.Activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Manager.AmazingAdapter;
import com.uit.nst95.quanlycuocdidong.Manager.AmazingListView;
import com.uit.nst95.quanlycuocdidong.Manager.DataMonthFee;
import com.uit.nst95.quanlycuocdidong.Manager.MonthFee;
import com.uit.nst95.quanlycuocdidong.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment {

    AmazingListView lsComposer;
    SectionMonthFeeAdapter adapter;
    private String currency = "đ";
    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance() {
        StatisticsFragment f = new StatisticsFragment();
        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        lsComposer = (AmazingListView) view.findViewById(R.id.lsComposer);
        lsComposer.setPinnedHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.custom_header_listview_monthfee, lsComposer, false));
        lsComposer.setAdapter(adapter = new SectionMonthFeeAdapter());

        return view;
    }
    class SectionMonthFeeAdapter extends AmazingAdapter {
        List<Pair<String, List<MonthFee>>> all = DataMonthFee.getAllData(getContext());

        @Override
        public int getCount() {
            int res = 0;
            for (int i = 0; i < all.size(); i++) {
                res += all.get(i).second.size();
            }
            return res;
        }

        @Override
        public MonthFee getItem(int position) {
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return all.get(i).second.get(position - c);
                }
                c += all.get(i).second.size();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        protected void onNextPageRequested(int page) {
        }

        @Override
        protected void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
            if (displaySectionHeader) {
                view.findViewById(R.id.header).setVisibility(View.VISIBLE);
                TextView lSectionTitle = (TextView) view.findViewById(R.id.header);
                lSectionTitle.setText(getSections()[getSectionForPosition(position)]);
            } else {
                view.findViewById(R.id.header).setVisibility(View.GONE);
            }
        }

        @Override
        public View getAmazingView(int position, View convertView, ViewGroup parent) {
            View res = convertView;

            DecimalFormat formatter = new DecimalFormat("#,###,###");

            if (res == null)
                res = getActivity().getLayoutInflater().inflate(R.layout.custom_item_listview_monthfee, null);

            TextView textViewTongTienGoi = (TextView) res.findViewById(R.id.textViewTongTienGoi);
            TextView textViewSoPhutGoiNoiMang = (TextView) res.findViewById(R.id.textViewSoPhutGoiNoiMang);
            TextView textViewTienGoiNoiMang = (TextView) res.findViewById(R.id.textViewTienGoiNoiMang);
            TextView textViewSoPhutGoiNgoaiMang = (TextView) res.findViewById(R.id.textViewSoPhutGoiNgoaiMang);
            TextView textViewTienGoiNgoaiMang = (TextView) res.findViewById(R.id.textViewTienGoiNgoaiMang);
            TextView textViewTongTienSmS = (TextView) res.findViewById(R.id.textViewTongTienSmS);
            TextView textViewSoSmsNoiMang = (TextView) res.findViewById(R.id.textViewSoSmsNoiMang);
            TextView textViewTienSmSNoiMang = (TextView) res.findViewById(R.id.textViewTienSmSNoiMang);
            TextView textViewSoSmsNgoaiMang = (TextView) res.findViewById(R.id.textViewSoSmsNgoaiMang);
            TextView textViewTienSmsNgoaiMang = (TextView) res.findViewById(R.id.textViewTienSmsNgoaiMang);
            TextView textViewTongTien = (TextView) res.findViewById(R.id.textViewTongTien);

            MonthFee monthFee = getItem(position);

            textViewTongTienGoi.setText(formatter.format(monthFee.getFee_innerCall() + monthFee.getFee_outerCall()) + currency);
            textViewSoPhutGoiNoiMang.setText(monthFee.getMinutes_innerCall());
            textViewTienGoiNoiMang.setText(formatter.format(monthFee.getFee_innerCall()) + currency);
            textViewSoPhutGoiNgoaiMang.setText(monthFee.getMinutes_outerCall());
            textViewTienGoiNgoaiMang.setText(formatter.format(monthFee.getFee_outerCall()) + currency);
            textViewTongTienSmS.setText(formatter.format(monthFee.getFee_innerMess() + monthFee.getFee_outerMess()) + currency);
            textViewSoSmsNoiMang.setText(String.valueOf(monthFee.getNumber_innerMess()));
            textViewTienSmSNoiMang.setText(formatter.format(monthFee.getFee_innerMess()) + currency);
            textViewSoSmsNgoaiMang.setText(String.valueOf(monthFee.getNumber_outerMess()));
            textViewTienSmsNgoaiMang.setText(formatter.format(monthFee.getFee_outerMess()) + currency);
            textViewTongTien.setText(formatter.format(monthFee.getFee_innerCall() + monthFee.getFee_outerCall() +
                    monthFee.getFee_innerMess() + monthFee.getFee_outerMess()) + currency);

            return res;
        }

        @Override
        public void configurePinnedHeader(View header, int position, int alpha) {
            TextView lSectionHeader = (TextView) header;
            lSectionHeader.setText(getSections()[getSectionForPosition(position)]);
            //lSectionHeader.setBackgroundColor(alpha << 24 | (0xbbffbb));
            //lSectionHeader.setTextColor(alpha << 24 | (0x000000));
        }

        @Override
        public int getPositionForSection(int section) {
            if (section < 0) section = 0;
            if (section >= all.size()) section = all.size() - 1;
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (section == i) {
                    return c;
                }
                c += all.get(i).second.size();
            }
            return 0;
        }

        @Override
        public int getSectionForPosition(int position) {
            int c = 0;
            for (int i = 0; i < all.size(); i++) {
                if (position >= c && position < c + all.get(i).second.size()) {
                    return i;
                }
                c += all.get(i).second.size();
            }
            return -1;
        }

        @Override
        public String[] getSections() {
            String[] res = new String[all.size()];
            for (int i = 0; i < all.size(); i++) {
                res[i] = all.get(i).first;
            }
            return res;
        }
    }
}
