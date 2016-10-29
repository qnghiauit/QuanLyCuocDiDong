package com.uit.nst95.quanlycuocdidong.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uit.nst95.quanlycuocdidong.R;
import com.uit.nst95.quanlycuocdidong.customview.FocusBoxView;
import com.uit.nst95.quanlycuocdidong.tesstwo.TesstwoRecognizeAsync;
import com.uit.nst95.quanlycuocdidong.tools.BitmapTool;
import com.uit.nst95.quanlycuocdidong.tools.CameraEngine;

/**
 * Created by Truong Ngoc Son on 10/29/2016.
 */

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.ShutterCallback {
    private static final String TAG = CameraFragment.class.getSimpleName(); // tag
    private static final int CAMERA_PERMISSION_REQUEST = 1;

    Button shutterButton;
    Button focusButton;
    FocusBoxView focusBox;
    SurfaceView cameraFrame;
    CameraEngine cameraEngine;
    LayoutInflater inflater;
    ViewGroup viewGroup;
    Bundle savedInstanceState;


    /**
     * utility creator method
     *
     * @return : {@link CameraFragment}
     */
    public static Fragment newInstance() {
        return new CameraFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // references
        this.inflater = inflater;
        this.viewGroup = viewGroup;
        // before using camera, we have to request permission if device's android version is M or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check permission request
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    // show the explanation dialog
                    new ConfirmationDialog().show(this.getFragmentManager(), TAG);
                } else {
                    this.requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                }
            }
        }
        return inflater.inflate(R.layout.camera_activity, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        cameraFrame = (SurfaceView) view.findViewById(R.id.camera_frame);
        shutterButton = (Button) view.findViewById(R.id.shutter_button);
        focusBox = (FocusBoxView) view.findViewById(R.id.focus_box);
        focusButton = (Button) view.findViewById(R.id.focus_button);

        shutterButton.setOnClickListener(this);
        focusButton.setOnClickListener(this);

        SurfaceHolder surfaceHolder = cameraFrame.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraFrame.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST:
                // if user denine permission, finish this activity
                if ((grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    this.onCreateView(this.inflater, this.viewGroup, this.savedInstanceState);
                } else {
                    Activity activity = getActivity();
                    activity.finish();
                }
        }
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
    public void onPause() {
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
        Bitmap bmp = BitmapTool.getFocusedBitmap(this.getContext(), camera, data, focusBox.getBox());

        Log.d(TAG, "Got bitmap");

        Log.d(TAG, "Initialization of TessBaseApi");

        // new task to recognize bitmap running in background
        // after the recognition completes, a simple dialog will show the result
        TesstwoRecognizeAsync tesstwoRecognizeAsync = new TesstwoRecognizeAsync();
        tesstwoRecognizeAsync.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, this.getActivity(), bmp);
    }

    @Override
    public void onShutter() {

    }


    /**
     * Inflate view from layout Resource.
     * This method will be invoke in {@link CameraFragment#onRequestPermissionsResult(int, String[], int[])} when user grant camera permission
     */
    private void inflateView() {
        // make sure the inflater in valid (not null)
        if (this.inflater != null) {
            this.inflater.inflate(R.layout.camera_activity, this.viewGroup, false);
        }
    }

    /**
     * Shows OK/Cancel confirmation dialog about camera permission.
     */
    public static class ConfirmationDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.camera_request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_REQUEST);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // finish activity if user deny permission
                                    Activity activity = parent.getActivity();
                                    if (activity != null) {
                                        activity.finish();
                                    }
                                }
                            })
                    .create();
        }
    }
}
