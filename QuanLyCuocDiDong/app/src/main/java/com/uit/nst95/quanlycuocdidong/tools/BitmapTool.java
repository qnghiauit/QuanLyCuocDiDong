package com.uit.nst95.quanlycuocdidong.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;

import com.uit.nst95.quanlycuocdidong.customview.FocusBoxUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Truong Ngoc Son on 10/22/2016.
 * Utility class with methods that relate to process {@link Bitmap} for best fit.
 * Such as : {@link BitmapTool#calculateSampleSize(int, int, int, int, ScalingLogic)} ...
 * Reference link : http://www.codeproject.com/Tips/840623/Android-Character-Recognition
 * Reference link android developer site : https://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 */

public class BitmapTool {
    /**
     * private constructor for enforcing non instantiability
     */
    private BitmapTool() {
    }

    /**
     * @param source
     * @param angle
     * @return
     */
    private static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap preRotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.preRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

    private enum ScalingLogic {
        CROP, FIT
    }

    /**
     * @param srcWidth
     * @param srcHeight
     * @param dstWidth
     * @param dstHeight
     * @param scalingLogic
     * @return
     */
    private static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                           ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    /**
     * @param bytes
     * @param dstWidth
     * @param dstHeight
     * @param scalingLogic
     * @return
     */
    private static Bitmap decodeByteArray(byte[] bytes, int dstWidth, int dstHeight,
                                          ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        return unscaledBitmap;
    }

    /**
     * @param srcWidth
     * @param srcHeight
     * @param dstWidth
     * @param dstHeight
     * @param scalingLogic
     * @return
     */
    private static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                         ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    /**
     * @param srcWidth
     * @param srcHeight
     * @param dstWidth
     * @param dstHeight
     * @param scalingLogic
     * @return
     */
    private static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                         ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    private static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
                                             ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    /**
     * @param context
     * @param camera
     * @param data
     * @param box
     * @return
     */
    public static Bitmap getFocusedBitmap(Context context, Camera camera, byte[] data, Rect box) {
        Point CamRes = FocusBoxUtils.getCameraResolution(context, camera);
        Point ScrRes = FocusBoxUtils.getScreenResolution(context);

        int SW = ScrRes.x;
        int SH = ScrRes.y;

        int RW = box.width();
        int RH = box.height();
        int RL = box.left;
        int RT = box.top;

        float RSW = (float) (RW * Math.pow(SW, -1));
        float RSH = (float) (RH * Math.pow(SH, -1));

        float RSL = (float) (RL * Math.pow(SW, -1));
        float RST = (float) (RT * Math.pow(SH, -1));

        float k = 0.5f; // value of zooming the bitmap

        int CW = CamRes.x;
        int CH = CamRes.y;

        int X = (int) (k * CW);
        int Y = (int) (k * CH);

        Bitmap unscaledBitmap = BitmapTool.decodeByteArray(data, X, Y, ScalingLogic.CROP);

        Bitmap bmp = BitmapTool.createScaledBitmap(unscaledBitmap, X, Y, ScalingLogic.CROP);
        bmp = removeBackground(bmp); // make background transparent
        unscaledBitmap.recycle();

        if (CW > CH)
            bmp = BitmapTool.rotateBitmap(bmp, 90);

        int BW = bmp.getWidth();
        int BH = bmp.getHeight();

        int RBL = (int) (RSL * BW);
        int RBT = (int) (RST * BH);

        int RBW = (int) (RSW * BW);
        int RBH = (int) (RSH * BH);

        Bitmap res = Bitmap.createBitmap(bmp, RBL, RBT, RBW, RBH);
        // change to white background
        bmp.recycle();

        return res;
    }


    /**
     * For the result from converting Image to text, the bitmap ground should be transparent.
     * This method is to change background of a {@link Bitmap} to transparent by using Open source OpenCV project.
     *
     * @param originalBitmap : The {@link Bitmap} to be changed background
     */
    private static Bitmap removeBackground(Bitmap originalBitmap) {
        // convert image to matrix
        Mat src = new Mat(originalBitmap.getHeight(), originalBitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(originalBitmap, src);

        // init new matrices
        Mat dst = new Mat(originalBitmap.getHeight(), originalBitmap.getWidth(), CvType.CV_8UC4);
        Mat tmp = new Mat(originalBitmap.getHeight(), originalBitmap.getWidth(), CvType.CV_8U);
        Mat alpha = new Mat(originalBitmap.getHeight(), originalBitmap.getWidth(), CvType.CV_8U);

        // convert image to grayscale
        Imgproc.cvtColor(src, tmp, Imgproc.COLOR_BGR2GRAY);

        // threshold the image to create alpha channel with complete transparency in black background region and zero transparency in foreground object region.
        Imgproc.threshold(tmp, alpha, 100, 255, Imgproc.THRESH_BINARY_INV);

        // split the original image into three single channel.
        List<Mat> bgra = new ArrayList<Mat>(4);
        Core.split(src, bgra);

        // Create the final result by merging three single channel and alpha(BGRA order)
        bgra.remove(3);
        bgra.add(alpha);
        Core.merge(bgra, dst);

        // convert matrix to output bitmap
        Bitmap output = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst, output);

        return output;
    }


}
