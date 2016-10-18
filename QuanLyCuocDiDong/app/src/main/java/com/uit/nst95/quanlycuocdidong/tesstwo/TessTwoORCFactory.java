package com.uit.nst95.quanlycuocdidong.tesstwo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/***
 * A Factory contains all utility methods that we need to process image to text, especially convert from {@link Bitmap} to number.
 */
public class TessTwoORCFactory {
    private static final String TAG = TessTwoORCFactory.class.getSimpleName(); // tag

    private TessTwoORCFactory() {
        // prevent from being instantiated
    }

    /**
     * Convert {@link Bitmap} to {@link String}
     * Note: make sure that we had trained data file (in this case eng.traninedata) already in device's storage (actually , in assets file of your project)
     *
     * @param bitmap : the {@link Bitmap} to be converted
     * @return
     */
    public static String convert(Context context, Bitmap bitmap) {
        // first check that if the tessdata directory existed or not
        File tessdataDirectory = new File(context.getExternalFilesDir(Environment.MEDIA_MOUNTED) + "/tessdata");
        if (!tessdataDirectory.exists()) {
            Log.e(TAG, "tessdata does not exist , start to create !!");
            try {
                createTessTwoTrainedDataRepository(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TessBaseAPI tessBaseAPI = new TessBaseAPI(); // instantiate a tess base API object
        // we save eng.traindata file in assets android
        // resource directory,
        tessBaseAPI.init(context.getExternalFilesDir(Environment.MEDIA_MOUNTED).toString(), "eng");
        tessBaseAPI.setImage(bitmap);
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890"); // we simply need to convert to number
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
                "YTREWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?"); // the rest of characters we don't bother anymore
        tessBaseAPI.setPageSegMode(TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED); // set mode for the process
        String recognizedText = tessBaseAPI.getUTF8Text();
        // end the tess api
        tessBaseAPI.end();
        return recognizedText;
    }

    /**
     * At the beginning time of first use, we need to copy and move the trained data file from assets directory to external storage of device
     *
     * @param context
     * @return true if the creation is successful, otherwise return false (error occurs or the file existed already)
     * @throws IOException
     */
    public static boolean createTessTwoTrainedDataRepository(Context context) throws IOException {
        File file = new File(context.getExternalFilesDir(Environment.MEDIA_MOUNTED) + "/tessdata"); // check existence of the directory in device storage
        if (!file.mkdir()) {
            Log.e(TAG, "Can not make tessdata directory");
            return false;
        }
        // start to copy eng.traindedata file into this subdirectory
        File englishTrainedDataFile = new File(context.getExternalFilesDir(Environment.MEDIA_MOUNTED) + "/tessdata/eng.traineddata");
        if (!englishTrainedDataFile.exists()) { // check existence of trained data file in device storage
            if (!englishTrainedDataFile.createNewFile()) {
                Log.e(TAG, "Can not make tess-two data directory");
                return false;
            }
            AssetManager assetManager = context.getAssets(); // get asset manager
            InputStream inputStream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outputStream = new FileOutputStream(englishTrainedDataFile);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, length);
            }
            //close all streams
            inputStream.close();
            outputStream.close();
            return true;
        }
        return false; // the file existed already
    }

}
