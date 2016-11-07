package com.uit.nst95.quanlycuocdidong.Manager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Activity.CheckByDayFragment;
import com.uit.nst95.quanlycuocdidong.DB.MessageLog;
import com.uit.nst95.quanlycuocdidong.R;

import java.util.List;

/**
 * Created by QNghia on 07/11/2016.
 */

public class MessLogArrayAdapter extends ArrayAdapter<MessageLog>
{
    Activity context=null;
    List<MessageLog> myArrayMessLog =null;
    int layoutId;

    public MessLogArrayAdapter(Activity context,
                               int layoutId,
                               List<MessageLog> arr){
        super(context, layoutId, arr);
        this.context=context;
        this.layoutId=layoutId;
        this.myArrayMessLog =arr;
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {
        LayoutInflater inflater=
                context.getLayoutInflater();
        convertView=inflater.inflate(layoutId, null);
        if(myArrayMessLog.size()>0 && position>=0)
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

            final MessageLog mess = myArrayMessLog.get(position);

            imageViewTypeLog.setImageResource(R.drawable.ic_action_communication_message);
            textViewPhoneNumber.setText(CheckByDayFragment.getContactName(getContext(),mess.get_receiverNumber()));
            textViewTime.setText(DateTimeManager.get_instance().convertToDMYHms(mess.get_messageDate()).substring(11, 16));
            textViewfee.setText(String.valueOf(mess.get_messageFee()) + CheckByDayFragment.currency);
            textViewDuration.setText("");

        }
        return convertView;
    }
}
