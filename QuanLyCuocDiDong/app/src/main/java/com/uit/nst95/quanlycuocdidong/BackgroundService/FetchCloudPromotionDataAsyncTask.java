package com.uit.nst95.quanlycuocdidong.BackgroundService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;

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
 * Created by TNS on 11/17/2016.
 */

public class FetchCloudPromotionDataAsyncTask extends AsyncTask<Void, Void, PromotionInformation.Results> {

    private static final String TAG = FetchCloudPromotionDataAsyncTask.class.getSimpleName(); // tag

    // values for shared preference keys
    public static final String PROMOTION_SHARED_PREFERENCE_KEY = "promotion_shared_preference_key";
    public static final String PROMOTION_PERCENTAGE_PREFERENCE_KEY = "promotion_percentage_preference_key";
    public static final String PROMOTION_TIME_PREFERENCE_KEY = "promotion_time_preference_key";
    public static final String PROMOTION_DEFAULT_VALUE = "+ 50%"; // default percentage value

    private String providerName;
    private Context context;

    public FetchCloudPromotionDataAsyncTask(String providerName, Context context) {
        this.providerName = providerName;
        this.context = context;
    }

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
                if (detailInformation.getProviderName().equalsIgnoreCase(this.providerName)) {
                    // save this updated values
                    SharedPreferences preferences = this.context.getSharedPreferences(PROMOTION_SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor
                            = preferences.edit();
                    editor.putString(PROMOTION_PERCENTAGE_PREFERENCE_KEY, detailInformation.getPercentage());
                    editor.putString(PROMOTION_TIME_PREFERENCE_KEY, detailInformation.getTime());
                    editor.apply();
                    SystemClock.sleep(500);
                    // release lock after work is done
                    AlarmReceiver.releaseLock();
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
        String clusterPointQuery = this.context.getString(R.string.clusterpoint_cloud_query);
        String accountString = this.context.getString(R.string.authentication_string);
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

