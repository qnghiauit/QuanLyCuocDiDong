package com.uit.nst95.quanlycuocdidong.customview;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Truong Ngoc Son on 10/22/2016.
 * Reference link : http://www.codeproject.com/Tips/840623/Android-Character-Recognition
 */

public class FocusBoxUtils {
    // constants defining the minimum and maximum pixels to support
    private static int MIN_PREVIEW_PIXELS = 470 * 320;
    private static int MAX_PREVIEW_PIXELS = 800 * 600;

    /**
     * Private constructor to prevent from being instantiated. See Factory Pattern.
     */
    private FocusBoxUtils() {

    }


    /**
     * Get the device's screen resolution
     *
     * @param context : the context that will call this method
     * @return : a {@link Point} that presents the width and  height of the screen
     */
    public static Point getScreenResolution(Context context) {

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //Display display = manager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics); // get Metrics
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        return new Point(width, height);

    }

    /**
     * Get camera resolution of the device
     *
     * @param context : context where this method is invoked
     * @param camera  : the camera object
     * @return : {@link Point} contains the related information
     */
    public static Point getCameraResolution(Context context, Camera camera) {
        return findBestPreviewSizeValue(camera.getParameters(), getScreenResolution(context));
    }

    /**
     * @param parameters
     * @param screenResolution
     * @return
     */
    private static Point findBestPreviewSizeValue(Camera.Parameters parameters,
                                                  Point screenResolution) {

        List<Camera.Size> supportedPreviewSizes =
                new ArrayList<Camera.Size>(parameters.getSupportedPreviewSizes());

        Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        Point bestSize = null;
        float screenAspectRatio = (float) screenResolution.x / (float) screenResolution.y;

        float diff = Float.POSITIVE_INFINITY;
        for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
            int realWidth = supportedPreviewSize.width;
            int realHeight = supportedPreviewSize.height;
            int pixels = realWidth * realHeight;
            if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
                continue;
            }
            boolean isCandidatePortrait = realWidth < realHeight;
            int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
            int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
            if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
                return new Point(realWidth, realHeight);
            }
            float aspectRatio = (float) maybeFlippedWidth / (float) maybeFlippedHeight;
            float newDiff = Math.abs(aspectRatio - screenAspectRatio);
            if (newDiff < diff) {
                bestSize = new Point(realWidth, realHeight);
                diff = newDiff;
            }
        }

        if (bestSize == null) {
            Camera.Size defaultSize = parameters.getPreviewSize();
            bestSize = new Point(defaultSize.width, defaultSize.height);
        }
        return bestSize;
    }
}
