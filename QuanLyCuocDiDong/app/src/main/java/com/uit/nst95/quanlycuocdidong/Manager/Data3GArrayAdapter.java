package com.uit.nst95.quanlycuocdidong.Manager;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.R;

import java.util.ArrayList;


/**
 * Created by QNghia on 31/10/2016.
 */

public class Data3GArrayAdapter extends ArrayAdapter<Data3GPackage> {

    Activity context = null;
    ArrayList<Data3GPackage> myArrayData3G = null;
    int layoutId;
    public Data3GArrayAdapter(Activity context,
                              int layoutId,
                              ArrayList<Data3GPackage> arr) {
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArrayData3G = arr;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);
        if (myArrayData3G.size() > 0 && position >= 0) {
            final TextView txtTenGoi = (TextView)
                    convertView.findViewById(R.id.textViewTenGoi);
            final TextView txtGiaCuoc = (TextView)
                    convertView.findViewById(R.id.textViewGiaCuoc);
            final TextView txtData = (TextView)
                    convertView.findViewById(R.id.textViewDLTocDoCao);
            final TextView txtVuotLL = (TextView)
                    convertView.findViewById(R.id.textViewVuotLuuLuong);
            final TextView txtHSD = (TextView)
                    convertView.findViewById(R.id.textViewHSD);
            final TextView txtCuPhapDK = (TextView)
                    convertView.findViewById(R.id.textViewCPDK);
            final Button btnDK = (Button)
                    convertView.findViewById(R.id.buttonDK);

            final Data3GPackage data3g = myArrayData3G.get(position);
            txtTenGoi.setText(data3g.getId_3gpackage());
            txtGiaCuoc.setText(data3g.getFee());
            txtData.setText("Data tốc độ cao: " + data3g.getData_highspeed());
            txtVuotLL.setText("Cước vượt lưu lượng: " + data3g.getCharges_arise());
            txtHSD.setText("Thời hạn sử dụng: " + data3g.getExpiry_date());
            txtCuPhapDK.setText("Soạn " + data3g.getSyntax_reg() + " gửi " + data3g.getNumber_receive());
            btnDK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", data3g.getNumber_receive());
                    smsIntent.putExtra("sms_body",data3g.getSyntax_reg());
                    getContext().startActivity(smsIntent);
                }
            });
        }
        return convertView;
    }
}