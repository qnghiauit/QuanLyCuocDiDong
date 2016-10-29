package com.uit.nst95.quanlycuocdidong.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.uit.nst95.quanlycuocdidong.R;
import com.uit.nst95.quanlycuocdidong.customview.FocusBoxView;
import com.uit.nst95.quanlycuocdidong.tesstwo.TesstwoRecognizeAsync;
import com.uit.nst95.quanlycuocdidong.tools.BitmapTool;
import com.uit.nst95.quanlycuocdidong.tools.CameraEngine;

/**
 * Created by Truong Ngoc Son on 10/25/2016.
 */

public class CameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.ShutterCallback {
    static final String TAG = "DBG_" + MainActivity.class.getName();

    Button shutterButton;
    Button focusButton;
    FocusBoxView focusBox;
    SurfaceView cameraFrame;
    CameraEngine cameraEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        this.findViewById(R.id.camera_controller_container).getBackground().setAlpha(75); // set opacity 50%
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Log.d(TAG, "Surface Created - starting camera");

        if (cameraEngine != null && !cameraEngine.isCameraOn()) {
            cameraEngine.start();
        }

        if (cameraEngine != null && cameraEngine.isCameraOn()) {
            Log.d(TAG, "Camera engine already on");
            return;
        }

        cameraEngine = CameraEngine.New(holder);
        cameraEngine.start();

        Log.d(TAG, "Camera engine started");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraFrame = (SurfaceView) findViewById(R.id.camera_frame);
        shutterButton = (Button) findViewById(R.id.shutter_button);
        focusBox = (FocusBoxView) findViewById(R.id.focus_box);
        focusButton = (Button) findViewById(R.id.focus_button);

        shutterButton.setOnClickListener(this);
        focusButton.setOnClickListener(this);

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraFrame.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (cameraEngine != null && cameraEngine.isCameraOn()) {
            cameraEngine.stop();
        }

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.removeCallback(this);
    }

    @Override
    public void onClick(View v) {
        if (v == shutterButton) {
            if (cameraEngine != null && cameraEngine.isCameraOn()) {
                cameraEngine.takeShot(this, this, this);
            }
        }

        if (v == focusButton) {
            if (cameraEngine != null && cameraEngine.isCameraOn()) {
                cameraEngine.requestFocus();
            }
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        Log.d(TAG, "Picture taken");

        if (data == null) {
            Log.d(TAG, "Got null data");
            return;
        }

        /**
         * Get {@link Bitmap} in {@link FocusBoxView} area
         */
        Bitmap bmp = BitmapTool.getFocusedBitmap(this, camera, data, focusBox.getBox());

        Log.d(TAG, "Got bitmap");

        Log.d(TAG, "Initialization of TessBaseApi");

        // new task to recognize bitmap running in background
        // after the recognition completes, a simple dialog will show the result
        TesstwoRecognizeAsync tesstwoRecognizeAsync = new TesstwoRecognizeAsync();
        tesstwoRecognizeAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, this, bmp);
    }

    @Override
    public void onShutter() {

    }

}
