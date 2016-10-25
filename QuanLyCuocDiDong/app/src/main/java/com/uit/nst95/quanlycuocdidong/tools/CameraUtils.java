package com.uit.nst95.quanlycuocdidong.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by Truong Ngoc Son on 10/25/2016.
 * See Android developer site : https://developer.android.com/guide/topics/media/camera.html#camera-apps to learn how to use with predicated {@link Camera} Api
 */

public class CameraUtils {
    private static final String TAG = CameraUtils.class.getSimpleName(); // tag for logginf

    private CameraUtils() {

    }

    /**
     * Method to check if the device has camera of not
     *
     * @param context : the {@link Context} invoke this method
     * @return : {@code true} if device has camera, otherwise returns {@code false}
     */
    private static boolean isCameraAvailable(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    /**
     * Get a {@link Camera} object.
     * NOTE : this API is predicated. A new API : android.hardware.camera2 is now released.
     *
     * @return
     */
    public static Camera getCamera() {
        Camera camera
                = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "Failed to open camera object", e);
        }
        return camera;
    }

}
