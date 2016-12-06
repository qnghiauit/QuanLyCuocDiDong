package com.uit.nst95.quanlycuocdidong.tesstwo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.uit.nst95.quanlycuocdidong.customview.ImageDialog;
import com.uit.nst95.quanlycuocdidong.tools.BitmapTool;

/**
 * Created by Truong Ngoc Son on 10/25/2016.
 * A specified  implementation of {@link AsyncTask} for convert {@link android.graphics.Bitmap} to {@link String}.
 * Reference : http://www.codeproject.com/Tips/840623/Android-Character-Recognition
 */

public class TesstwoRecognizeAsync extends AsyncTask<Object, Void, String> {
    private static final String TAG = TesstwoRecognizeAsync.class.getSimpleName(); // tag for logging

    // bitmap to be converted
    private Bitmap bitmap;
    private Activity context;

    @Override
    protected String doInBackground(Object... params) {
        if (params.length < 2) {
            Log.e(TAG, "Error passing parameter to execute - missing params");
            return null;
        }

        if (!(params[0] instanceof Activity) || !(params[1] instanceof Bitmap)) {
            Log.e(TAG, "Error passing parameter to execute(context, bitmap)");
            return null;
        }

        this.context = (Activity) params[0];
        // get bitmap from parameter
        this.bitmap = (Bitmap) params[1];

        int rotate = 0;

        if (params.length == 3 && params[2] != null && params[2] instanceof Integer) {
            rotate = (Integer) params[2];
        }

        if (rotate >= -180 && rotate <= 180 && rotate != 0) {
            this.bitmap = BitmapTool.preRotateBitmap(this.bitmap, rotate);
            Log.d(TAG, "Rotated OCR bitmap " + rotate + " degrees");
        }

        this.bitmap = this.bitmap.copy(Bitmap.Config.ARGB_8888, true);
        // convert bitmap to number in String
        String result = TessTwoORCFactory.convert(this.context, this.bitmap);
        // log the result
        Log.d(TAG, "Recognized result : " + result);

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        /**
         * Show a {@link com.uit.nst95.quanlycuocdidong.customview.ImageDialog} to see the result
         */
        ImageDialog.New()
                .addBitmap(this.bitmap)
                .addTitle(s)
                .show(context.getFragmentManager(), TAG);

        super.onPostExecute(s);
    }
}
