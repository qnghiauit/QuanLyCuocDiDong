package com.uit.nst95.quanlycuocdidong.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.BackgroundService.FetchCloudPromotionDataAsyncTask;
import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.Manager.PromotionInformation;
import com.uit.nst95.quanlycuocdidong.R;
import com.uit.nst95.quanlycuocdidong.tools.NetworkChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class PromotionFragment extends Fragment {

    private static final String TAG = PromotionFragment.class.getSimpleName();

    /**
     * Create a custom {@link FetchCloudPromotionDataAsyncTaskCustom} extending super class {@link FetchCloudPromotionDataAsyncTask} to download cloud cluster point data
     * and save into application's {@link SharedPreferences}. This include update view after downloading data.
     */
    private class FetchCloudPromotionDataAsyncTaskCustom extends FetchCloudPromotionDataAsyncTask {
        public FetchCloudPromotionDataAsyncTaskCustom(String providerName, Context context) {
            super(providerName, context);
        }

        @Override
        protected void onPostExecute(PromotionInformation.Results results) {
            super.onPostExecute(results);
            // Once the download completes, the desired promotion data is saved into application's SharedPreferences
            // so, we get data from SharedPreferences to update the View components (concretely , TextView)
            // start to update Views
            SharedPreferences preferences = getContext().getSharedPreferences(PROMOTION_SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
            final TextView promotionTimeTextView = (TextView) getView().findViewById(R.id.textViewThoiGian);
            promotionTimeTextView.setText(preferences.getString(PROMOTION_TIME_PREFERENCE_KEY, PROMOTION_DEFAULT_VALUE));
            final TextView promotionPercentageTextView = (TextView) getView().findViewById(R.id.textViewNoiDung);
            promotionPercentageTextView.setText(preferences.getString(
                    PROMOTION_PERCENTAGE_PREFERENCE_KEY + "% giá trị các thẻ nạp"
                    , PROMOTION_DEFAULT_VALUE));


        }
    }


    private String SDT = "123";
    private String nhaMang;
    private String NoiDung = "TC";
    private int idImage;
    Switch switchNhanTB;
    SharedPreferences settings;

    public PromotionFragment() {
        // Required empty public constructor
    }

    public static PromotionFragment newInstance() {
        PromotionFragment f = new PromotionFragment();
        return (f);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, 0);
        nhaMang = settings.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);
        if (nhaMang.equals(DefinedConstant.MOBIFONE)) {
            SDT = "9241";
            idImage = R.drawable.mobifone_provider;
        } else if (nhaMang.equals(DefinedConstant.VIETTEL)) {
            SDT = "199";
            idImage = R.drawable.viettel_provider;
        } else if (nhaMang.equals(DefinedConstant.VINAPHONE)) {
            SDT = "18001091";
            idImage = R.drawable.vinaphone_provider;
        } else if (nhaMang.equals(DefinedConstant.VIETNAMOBILE)) {
            SDT = "123";
            NoiDung = "TCQC";
            idImage = R.drawable.vietnamobile_provider;
        } else if (nhaMang.equals(DefinedConstant.GMOBILE)) {
            SDT = "123";
            NoiDung = "TCQC";
            idImage = R.drawable.gmobile_provider;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);

        ImageView imageViewLogo = (ImageView) view.findViewById(R.id.imageViewLogoProvider);
        imageViewLogo.setImageResource(idImage);

        /* Get shared preference values.
         If shared preference's value is default, we start a task to manually get data from cloud server.
         Additionally, We have a service run in background to update data from server every 5 minutes.
         */
        SharedPreferences preferences = this.getContext().getSharedPreferences(
                FetchCloudPromotionDataAsyncTask.PROMOTION_SHARED_PREFERENCE_KEY
                , Context.MODE_PRIVATE);
        String promotionPercentage = preferences.getString(
                FetchCloudPromotionDataAsyncTask.PROMOTION_PERCENTAGE_PREFERENCE_KEY
                , FetchCloudPromotionDataAsyncTask.PROMOTION_DEFAULT_VALUE);
        String promotionTime = preferences.getString(
                FetchCloudPromotionDataAsyncTask.PROMOTION_TIME_PREFERENCE_KEY
                , FetchCloudPromotionDataAsyncTask.PROMOTION_DEFAULT_VALUE);
        if (promotionPercentage.equals(FetchCloudPromotionDataAsyncTask.PROMOTION_DEFAULT_VALUE)
                || promotionTime.equals(FetchCloudPromotionDataAsyncTask.PROMOTION_DEFAULT_VALUE)) {

            // at first, we need check internet connection before fetching
            if (NetworkChecker.isInternetConnectionAvailable(this.getContext())) {

                // start to asynchronous task to download data manually
                new FetchCloudPromotionDataAsyncTaskCustom(this.nhaMang, this.getContext()).execute();
            }
        } else {
            final TextView textViewNgayKM = (TextView) view.findViewById(R.id.textViewThoiGian);
            textViewNgayKM.setText(promotionTime);
            final TextView textViewNoiDung = (TextView) view.findViewById(R.id.textViewNoiDung);
            textViewNoiDung.setText(promotionPercentage + "% giá trị các thẻ nạp");
        }


        TextView textViewHD = (TextView) view.findViewById(R.id.textViewHuongDan);
        textViewHD.setText("Thuê bao " + nhaMang + " soạn tin TC gửi đến " + SDT);

        Button buttonTH = (Button) view.findViewById(R.id.buttonThucHien);
        buttonTH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", SDT);
                smsIntent.putExtra("sms_body", NoiDung);
                startActivity(smsIntent);
            }
        });

        switchNhanTB = (Switch) view.findViewById(R.id.switchNhanTB);
        switchNhanTB.setChecked(settings.getBoolean(DefinedConstant.KEY_ALLOWRECEIVE, false));
        // register checked change listener
        switchNhanTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new FetchCloudPromotionDataAsyncTaskCustom(nhaMang, getContext()).execute();
                }
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        settings = getActivity().getSharedPreferences(DefinedConstant.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        boolean AllowPopup;
        if (switchNhanTB.isChecked())
            AllowPopup = true;
        else
            AllowPopup = false;
        editor.putBoolean(DefinedConstant.KEY_ALLOWRECEIVE, AllowPopup);
        // Commit the edits!
        editor.apply();
        super.onStop();
    }


}
