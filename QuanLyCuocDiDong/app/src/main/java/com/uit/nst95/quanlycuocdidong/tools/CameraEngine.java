package com.uit.nst95.quanlycuocdidong.tools;

import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by Truong Ngoc Son on 10/25/2016.
 * This class is reference from code project site : http://www.codeproject.com/Tips/840623/Android-Character-Recognition
 */

public class CameraEngine {
    private static final String TAG = CameraEngine.class.getSimpleName(); // tag

    private boolean isCameraOn;
    private SurfaceHolder surfaceHolder;
    private Camera camera;

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {

        }
    };

    private CameraEngine(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }

    static public CameraEngine New(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "Creating camera engine");
        return new CameraEngine(surfaceHolder);
    }

    public boolean isCameraOn() {
        return this.isCameraOn;
    }

    public void requestFocus() {
        if (camera == null)
            return;

        if (isCameraOn()) {
            camera.autoFocus(autoFocusCallback);
        }
    }

    public void start() {

        Log.d(TAG, "Entered CameraEngine - start()");
        this.camera = CameraUtils.getCamera();

        if (this.camera == null)
            return;
        Log.d(TAG, "Got camera hardware");
        try {
            this.camera.setPreviewDisplay(this.surfaceHolder);
            this.camera.setDisplayOrientation(90);
            this.camera.startPreview();
            // flag the signal
            this.isCameraOn = true;
            Log.d(TAG, "CameraEngine preview started");

        } catch (IOException e) {
            Log.e(TAG, "Error in setPreviewDisplay");
        }
    }

    public void stop() {

        if (camera != null) {
            //this.autoFocusEngine.stop();
            camera.release();
            camera = null;
        }

        this.isCameraOn = false;

        Log.d(TAG, "CameraEngine Stopped");
    }

    public void takeShot(Camera.ShutterCallback shutterCallback,
                         Camera.PictureCallback rawPictureCallback,
                         Camera.PictureCallback jpegPictureCallback) {
        if (this.isCameraOn()) {
            camera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
        }
    }
}
