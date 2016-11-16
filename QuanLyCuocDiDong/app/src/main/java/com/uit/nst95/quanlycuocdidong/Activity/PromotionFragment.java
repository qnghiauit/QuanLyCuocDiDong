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
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.Manager.PromotionInformation;
import com.uit.nst95.quanlycuocdidong.R;

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

    // values for shared preference keys
    public static final String PROMOTION_SHARED_PREFERENCE_KEY = "promotion_shared_preference_key";
    public static final String PROMOTION_PERCENTAGE_PREFERENCE_KEY = "promotion_percentage_preference_key";
    public static final String PROMOTION_TIME_PREFERENCE_KEY = "promotion_time_preference_key";
    public static final String PROMOTION_DEFAULT_VALUE = "promotion_default_value";

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
        SharedPreferences preferences = this.getContext().getSharedPreferences(PROMOTION_SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        String promotionPercentage = preferences.getString(PROMOTION_PERCENTAGE_PREFERENCE_KEY, PROMOTION_DEFAULT_VALUE);
        String promotionTime = preferences.getString(PROMOTION_TIME_PREFERENCE_KEY, PROMOTION_DEFAULT_VALUE);
        if (promotionPercentage.equals(PROMOTION_DEFAULT_VALUE) || promotionTime.equals(PROMOTION_DEFAULT_VALUE)) {
            // start to asynchronous
            new FetchCloudPromotionDataAsync().execute();
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


    public class FetchCloudPromotionDataAsync extends AsyncTask<Void, Void, PromotionInformation.Results> {
        @Override
        protected PromotionInformation.Results doInBackground(Void... params) {

            try {
                return this.getAllPromotionDataFromClusterPointService();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(PromotionInformation.Results results) {
            super.onPostExecute(results);
            // make we have valid result
            if (results != null) {
                // loop to find user portal service provider
                for (PromotionInformation.Result detailInformation : results.getResults()) {
                    if (detailInformation.getProviderName().equalsIgnoreCase(nhaMang)) {
                        // save this updated values
                        SharedPreferences preferences = getContext().getSharedPreferences(PROMOTION_SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor
                                = preferences.edit();
                        editor.putString(PROMOTION_PERCENTAGE_PREFERENCE_KEY, detailInformation.getPercentage());
                        editor.putString(PROMOTION_TIME_PREFERENCE_KEY, detailInformation.getTime());
                        editor.apply();
                        // update views
                        final TextView textViewNgayKM = (TextView) getView().findViewById(R.id.textViewThoiGian);
                        textViewNgayKM.setText(detailInformation.getTime());
                        final TextView textViewNgayNoiDung = (TextView) getView().findViewById(R.id.textViewNoiDung);
                        textViewNgayNoiDung.setText(detailInformation.getPercentage() + "% giá trị các thẻ nạp.");
                        // exit the function
                        return;
                    }
                }
            }
        }

        /**
         * Get promotion data from cloud server : Cluster Point
         * Visit this site : https://cloud-eu.clusterpoint.com/#/collection/promotion_information.promotion_information/run-query
         * See : R.string.accountString to see username and password for authentication
         * <p>
         * The response is Json format. So we use XmlMapper (an open source API) to parse for fast performance.
         * This is how server responses
         * {
         * "results": [
         * {
         * "providerName": "vietnamobile",
         * "time": "1/1/2016",
         * "percent": "100",
         * "_id": "cmJvLpMO"
         * },
         * {
         * "providerName": "mobifone",
         * "time": "1/1/2016",
         * "percent": "100",
         * "_id": "cP1GwHi2"
         * },
         * {
         * "providerName": "vinafone",
         * "time": "1/1/2016",
         * "percent": "100",
         * "_id": "XsG8nTd"
         * },
         * {
         * "providerName": "viettel",
         * "time": "1/1/2016",
         * "percent": "100",
         * "_id": "cP1GwCmH"
         * }
         * ],
         * "error": null,
         * "seconds": 0.331634,
         * "hits": "4",
         * "more": "=4",
         * "found": "4",
         * "from": "0",
         * "to": "4"
         * }
         *
         * @return : {@link com.uit.nst95.quanlycuocdidong.Manager.PromotionInformation.Results} contains list of promotion details
         * @throws IOException
         */
        private PromotionInformation.Results getAllPromotionDataFromClusterPointService() throws IOException, JSONException {
            String clusterPointQuery = getString(R.string.clusterpoint_cloud_query);
            String accountString = getString(R.string.authentication_string);
            String authenticationString = "Basic " + Base64.encodeToString(accountString.getBytes(),
                    0, accountString.length(), Base64.DEFAULT); // authentication string

            URL clusterPointURL = new URL(clusterPointQuery);
            HttpURLConnection httpURLConnection = (HttpURLConnection) clusterPointURL.openConnection();
            httpURLConnection.addRequestProperty("Authorization", authenticationString);
            httpURLConnection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            // write select query into output stream
            dataOutputStream.writeBytes("select * from promotion_information");
            dataOutputStream.flush();
            dataOutputStream.close();

            // Receive response
            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuilder stringBuilder = new StringBuilder();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                stringBuilder.append(charArray, 0, numCharsRead);
            }

            Log.d(TAG, "Server Cluster point responses : " + stringBuilder.toString());


            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            // parse inner values
            PromotionInformation promotionInformation = this.JsonToPromotionInformation(jsonObject);
            return promotionInformation.getResults();
        }


        /**
         * Parse Json response from server to {@link PromotionInformation} value
         *
         * @param jsonObject : The response from server
         * @return : {@link PromotionInformation} corresponding to json
         * @throws JSONException : if some errors occur when parsing
         */
        private PromotionInformation JsonToPromotionInformation(JSONObject jsonObject) throws JSONException {
            PromotionInformation promotionInformation = new PromotionInformation();
            // parse inner values
            // get all response properties from server
            promotionInformation.setFrom(jsonObject.getString("from"));
            promotionInformation.setFound(jsonObject.getString("found"));
            promotionInformation.setHits(jsonObject.getString("hits"));
            promotionInformation.setMore(jsonObject.getString("more"));
            promotionInformation.setFrom(jsonObject.getString("from"));
            promotionInformation.setTo(jsonObject.getString("to"));
            // primary data
            // get array
            JSONArray detailedInformationArray = jsonObject.getJSONArray("results");
            PromotionInformation.Result[] arrayOfResults = new PromotionInformation.Result[detailedInformationArray.length()];
            for (int index = 0; index < detailedInformationArray.length(); ++index) {
                JSONObject detailedPromotionInformationJson = detailedInformationArray.getJSONObject(index);
                arrayOfResults[index] = new PromotionInformation.Result();
                arrayOfResults[index].setId(detailedPromotionInformationJson.getString("_id"));
                arrayOfResults[index].setProviderName(detailedPromotionInformationJson.getString("providerName"));
                arrayOfResults[index].setPercentage(detailedPromotionInformationJson.getString("percent"));
                arrayOfResults[index].setTime(detailedPromotionInformationJson.getString("time"));
            }

            PromotionInformation.Results results = new PromotionInformation.Results();
            results.setResults(arrayOfResults);
            promotionInformation.setResults(results);
            // log the result
            Log.d(TAG, promotionInformation.toString());
            return promotionInformation;
        }

    }


}
