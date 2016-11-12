package com.uit.nst95.quanlycuocdidong.Manager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Activity.CheckByDayFragment;
import com.uit.nst95.quanlycuocdidong.DB.CallLog;
import com.uit.nst95.quanlycuocdidong.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by QNghia on 07/11/2016.
 */

public class CalllogArrayAdapter extends ArrayAdapter<CallLog>
{
    Activity context=null;
    List<CallLog> myArrayCalllog =null;
    int layoutId;

    public CalllogArrayAdapter(Activity context,
                               int layoutId,
                               List<CallLog> arr){
        super(context, layoutId, arr);
        this.context=context;
        this.layoutId=layoutId;
        this.myArrayCalllog =arr;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater=
                context.getLayoutInflater();
        convertView=inflater.inflate(layoutId, null);
        if(myArrayCalllog.size()>0 && position>=0)
        {
            final TextView textViewPhoneNumber=(TextView)
                    convertView.findViewById(R.id.textViewPhoneNumber);
            final TextView textViewTime=(TextView)
                    convertView.findViewById(R.id.textViewTime);
            final TextView textViewfee=(TextView)
                    convertView.findViewById(R.id.textViewfee);
            final TextView textViewDuration=(TextView)
                    convertView.findViewById(R.id.textViewDuration);
            final ImageView imageViewTypeLog = (ImageView)
                    convertView.findViewById(R.id.imageViewTypeLog);

            final CallLog call = myArrayCalllog.get(position);
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            imageViewTypeLog.setImageResource(R.drawable.ic_action_communication_call);
            textViewPhoneNumber.setText(CheckByDayFragment.getContactName(getContext(), call.get_callNumber()));
            textViewTime.setText(DateTimeManager.get_instance().convertToDMYHms(call.get_callDate()).substring(11, 16));
            textViewfee.setText(formatter.format(call.get_callFee()) + CheckByDayFragment.currency);
            textViewDuration.setText(DateTimeManager.get_instance().convertToMinutesAndSec(call.get_callDuration(),true));

        }
        return convertView;
    }
}
