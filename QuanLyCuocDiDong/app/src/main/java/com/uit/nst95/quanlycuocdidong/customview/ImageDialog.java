package com.uit.nst95.quanlycuocdidong.customview;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.R;

/**
 * Created by Truong Ngoc Son on 10/25/2016.
 * This will show result recognized by {@link com.uit.nst95.quanlycuocdidong.tesstwo.TessTwoORCFactory} simply.
 * This class is used for TESTING intention.
 */

public class ImageDialog extends DialogFragment {
    private static final String TAG = "ImageDialog"; // TAG
    private Bitmap bmp;
    private String recognizedText;

    public ImageDialog() {
    }

    public static ImageDialog New() {
        return new ImageDialog();
    }

    public ImageDialog addBitmap(Bitmap bmp) {
        if (bmp != null)
            this.bmp = bmp;
        return this;
    }

    public ImageDialog addTitle(String title) {
        if (title != null)
            this.recognizedText = title;
        return this;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity()); // create builder
        LayoutInflater layoutInflater = this.getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.image_dialog, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.image_dialog_imageView);
        imageView.setImageBitmap(this.bmp);
        final TextView textView = (TextView) view.findViewById(R.id.image_dialog_textView);
        textView.setText(this.recognizedText);
        // create dialog with custom view with 2 action buttons
        builder.setView(view).setPositiveButton(R.string.accept_recharge, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // start intent to make a call
                recharge(recognizedText);
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss the dialog
                dialogInterface.dismiss();
            }
        });
        // set icon and title for dialog
        builder.setIcon(R.drawable.ic_phone_24dp);
        builder.setTitle(R.string.dialog_number_recognition_title);

        // return the dialog
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        bmp.recycle();
        bmp = null;
        System.gc();
        super.onDismiss(dialog);
    }


    /**
     * Start a intent with action is {@link Intent#ACTION_CALL} to make a call with number is *101#voucherCode
     *
     * @param voucherCode : code of the voucher to be recharged
     */
    private void recharge(String voucherCode) {
        Intent rechargeIntent = new Intent(Intent.ACTION_DIAL);
        rechargeIntent.setData(Uri.parse("tel:" + Uri.encode("*101#" + voucherCode)));
        // start
        this.startActivity(rechargeIntent);

    }
}
