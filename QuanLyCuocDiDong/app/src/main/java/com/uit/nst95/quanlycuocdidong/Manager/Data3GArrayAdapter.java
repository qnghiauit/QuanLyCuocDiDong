package com.uit.nst95.quanlycuocdidong.Manager;

import android.app.Activity;
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
            txtData.setText(data3g.getData_highspeed());
            txtVuotLL.setText(data3g.getCharges_arise());
            txtHSD.setText(data3g.getExpiry_date());
            txtCuPhapDK.setText(data3g.getSyntax_reg());
            //btnDK.setText(data3g.getId_3gpackage());
        }
        return convertView;
    }
}