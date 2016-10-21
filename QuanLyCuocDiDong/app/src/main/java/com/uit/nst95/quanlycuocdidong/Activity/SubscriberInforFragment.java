package com.uit.nst95.quanlycuocdidong.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubscriberInforFragment extends Fragment {

    private TextView textViewGT;
    private TextView textViewHD;
    private TextView textViewTTCT;
    private Button buttonExcute;
    public SubscriberInforFragment() {
        // Required empty public constructor
    }

    public static SubscriberInforFragment newInstance() {
        SubscriberInforFragment f = new SubscriberInforFragment();

        /*Bundle args = new Bundle();

        args.putString(KEY_TITLE, title);
        f.setArguments(args);*/

        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscriber_infor, container, false);

        textViewGT = (TextView) view.findViewById(R.id.textViewGioithieu);
        textViewHD = (TextView) view.findViewById(R.id.textViewHuongDan);
        textViewTTCT = (TextView) view.findViewById(R.id.textViewTTChiTiet);
        buttonExcute = (Button) view.findViewById(R.id.buttonOk);
        buttonExcute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", "1414");
                smsIntent.putExtra("sms_body","TTTB");
                startActivity(smsIntent);
            }
        });
        textViewTTCT.setText(Html.fromHtml(getString(R.string.urlTTCTTraCuuTTTB)));
        textViewTTCT.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

}
