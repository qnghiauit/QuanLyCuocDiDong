package com.uit.nst95.quanlycuocdidong.tools;

import android.hardware.Camera;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Truong Ngoc Son on 10/25/2016.
 * This class present auto focus the use camera. This contains a static create method to create this statically
 * Reference : http://www.codeproject.com/Tips/840623/Android-Character-Recognition
 */

public class AutoFocusEngine implements Camera.AutoFocusCallback {
    private static final String TAG = AutoFocusEngine.class
            .getSimpleName();

    // 2 seconds for focusing
    private static final int FOCUS_INTERVAL_TIME = 2000;


    private Timer timer;
    private TimerTask timerTask;
    private Camera camera;
    private boolean isRuning = false;

    /**
     * Private constructor for noninstatiblity
     *
     * @param camera
     */
    private AutoFocusEngine(Camera camera) {
        this.camera = camera;
        this.timer = new Timer();
    }

    /**
     * Static creator method
     *
     * @param camera
     * @return
     */
    public static AutoFocusEngine newInstance(Camera camera) {
        return new AutoFocusEngine(camera);
    }


    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        /* instantiate a TimerTask and start to run in background  */
        this.timerTask = new TimerTask() {
            @Override
            public void run() {
                // start to focus
                startFocus();
            }
        };
        // scheduler the Timer
        this.timer.schedule(this.timerTask, FOCUS_INTERVAL_TIME);
    }


    /**
     * Start to focus
     */
    public void startFocus() {
        this.camera.autoFocus(this);
        this.isRuning = true;
    }

    /**
     * Stop focusing
     */
    public void stopFocus() {
        this.camera.cancelAutoFocus();

        // check if the timer still run in background
        if (this.timerTask != null) {
            this.timerTask.cancel();
            this.timerTask = null;
        }
        this.isRuning = false;

    }

    public boolean isRuning() {
        return isRuning;
    }
}
