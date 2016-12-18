package com.uit.nst95.quanlycuocdidong.BackgroundService;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.uit.nst95.quanlycuocdidong.Activity.MainActivity;
import com.uit.nst95.quanlycuocdidong.DB.CallLog;
import com.uit.nst95.quanlycuocdidong.DB.DAO_CallLog;
import com.uit.nst95.quanlycuocdidong.DB.DAO_Statistic;
import com.uit.nst95.quanlycuocdidong.Manager.DateTimeManager;
import com.uit.nst95.quanlycuocdidong.NetworkPackage.MobiCard;
import com.uit.nst95.quanlycuocdidong.NetworkPackage.NumberHeaderManager;
import com.uit.nst95.quanlycuocdidong.NetworkPackage.PackageFee;
import com.uit.nst95.quanlycuocdidong.R;
import com.uit.nst95.quanlycuocdidong.NetworkPackage.*;

import java.util.Date;

import com.uit.nst95.quanlycuocdidong.Manager.*;

/**
 * Created by QNghia on 9/26/2016.
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneStateReceiver";
    private static final int CALL_LOG_NOTIFICATION_ID = 1; // id of the notification

    Context _context;
    String _incomingNumber;
    private int _prev_state;
    private static boolean _isOutGoingCallEnd = false;
    private boolean _isReceived;
    private static boolean _isOutGoing = false;
    // private PhoneLogManager _phoneCallLog;
    DAO_CallLog _callAdapter;
    DAO_Statistic _statisticTableAdapter;
    private PackageFee _myPackageFee;
    private boolean _isAllowPopUp;
    private static TelephonyManager telephonyManager;
    private static CustomPhoneStateListener customPhoneStateListener;

    public PhoneStateReceiver() {
        //_isReceived = false;
        _incomingNumber = "";
        _prev_state = -1;
        _isOutGoingCallEnd = false;

    }


    public void InitPackage() {
        SharedPreferences setting = _context.getSharedPreferences(DefinedConstant.PREFS_NAME, Context.MODE_PRIVATE);
        String _package = setting.getString(DefinedConstant.KEY_PACKAGE, "Unknown");
        _isAllowPopUp = setting.getBoolean(DefinedConstant.KEY_ALLOWPOPUP, false);

        switch (_package) {
            case DefinedConstant.MOBICARD: {
                _myPackageFee = new MobiCard();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case DefinedConstant.MOBIGOLD: {
                _myPackageFee = new MobiGold();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case DefinedConstant.MOBIQ: {
                _myPackageFee = new MobiQ();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case DefinedConstant.QSTUDENT: {
                _myPackageFee = new QStudent();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case DefinedConstant.QTEEN: {
                _myPackageFee = new QTeen();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case DefinedConstant.QKIDS: {
                _myPackageFee = new Qkids();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
                break;
            }
            case DefinedConstant.VINACARD: {
                _myPackageFee = new VinaCard();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
                break;
            }
            case DefinedConstant.VINAXTRA: {
                _myPackageFee = new VinaXtra();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
                break;
            }
            case DefinedConstant.TALKSTUDENT: {
                _myPackageFee = new TalkStudent();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
                break;
            }
            case DefinedConstant.TALKTEEN: {
                _myPackageFee = new TalkTeen();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
                break;
            }
            case DefinedConstant.ECONOMY: {
                _myPackageFee = new Economy();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case DefinedConstant.TOMATO: {
                _myPackageFee = new Tomato();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case DefinedConstant.STUDENT: {
                _myPackageFee = new Student();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case DefinedConstant.SEA: {
                _myPackageFee = new SeaPlus();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case DefinedConstant.HISCHOOL: {
                _myPackageFee = new HiSchool();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case DefinedConstant.SEVENCOLOR: {
                _myPackageFee = new SevenColor();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case DefinedConstant.BUONLANG: {
                _myPackageFee = new TomatoBL();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
                break;
            }
            case DefinedConstant.BIGSAVE: {
                _myPackageFee = new BigSave();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case DefinedConstant.BIGKOOL: {
                _myPackageFee = new BigKool();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case DefinedConstant.TIPHU2: {
                _myPackageFee = new BillionareTwo();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case DefinedConstant.TIPHU3: {
                _myPackageFee = new BillionareThree();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case DefinedConstant.TIPHU5: {
                _myPackageFee = new BillionareFive();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
                break;
            }
            case DefinedConstant.VMONE: {
                _myPackageFee = new VMOne();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
                break;
            }
            case DefinedConstant.VMAX: {
                _myPackageFee = new VMax();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
                break;
            }
            case DefinedConstant.SV2014: {
                _myPackageFee = new SV2014();
                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
                break;
            }
        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Out  !!");
        this._context = context;
//            _callAdapter = new DAO_CallLog(context);
//            _statisticTableAdapter = new DAO_Statistic(context);
//            _callAdapter.Open();
//            _statisticTableAdapter.Open();
//            _phoneCallLog = new PhoneLogManager(_context, _myPackageFee);
        this.InitPackage();
        _isOutGoingCallEnd = false;
        //Sua loi tao ra nhieu PhoneStateListener trong chuong trinh  gay trung Notify
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (customPhoneStateListener == null) {
            customPhoneStateListener = new CustomPhoneStateListener(_context);//,_myPackageFee);
        }
        telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        // Toast.makeText(this._context, "Starts Receivers", Toast.LENGTH_LONG).show()

    }

    public void onDestroy() {
        telephonyManager.listen(null, PhoneStateListener.LISTEN_NONE);
    }

    /**
     *
     */
    public class CustomPhoneStateListener extends PhoneStateListener {
        boolean _isOutGoing = false;

        private static final String TAG = "CustomPhoneStateListener";
        //  public static final String TAG_DURATION = "CallDuration";
        //  public static final String TAG_FEE = "CallFee";
        Context _context;
        //PackageFee _package;
        //Intent _listenerIntent;
        boolean _isReceivingCall;

        public CustomPhoneStateListener(Context context) {//, PackageFee packageFee) {
            super();
            this._context = context;
            //this._package = packageFee;

            // _listenerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //  _listenerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            _isReceivingCall = false;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.d(TAG, "onCallStateChanged");
            if (incomingNumber != null && incomingNumber.length() > 0) {
                _incomingNumber = incomingNumber;
            }
            _isReceivingCall = false;
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: {
                    Log.d(TAG, "User ends call CALL_STATE_RINGING");
                    _prev_state = state;
                    _isReceivingCall = true;
                    break;
                }
                case TelephonyManager.CALL_STATE_OFFHOOK: {
                    Log.d(TAG, "User ends call CALL_STATE_OFFHOOK");
//                    if (incomingNumber == null || incomingNumber.isEmpty() || incomingNumber.equals("")) {
//                        _isOutGoing = true;
//                    } else {
//                        _isOutGoing = false;
//                    }
                    _isOutGoing = true;
                    _prev_state = state;
                    break;
                }
                case TelephonyManager.CALL_STATE_IDLE: {
                    Log.d(TAG, "User ends call CALL_STATE_IDLE");
                    // _isOutGoingCallEnd = true;
                    if (_prev_state == TelephonyManager.CALL_STATE_IDLE) {
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        CallLog lastCall = getNewCallLog(); // lay thong tin cua cuoc goi gan nhat trong database
                        if (lastCall == null || lastCall.get_callDuration() == 0) {
                            Log.e(TAG, "Last call is null");
                        } else {
                            showLastCallInformationNotification(lastCall); // start to show notification
                        }

                        /**
                         * SHOW NOTIFICATION
                         */
                        //  showLastCallInformationNotification(lastCall);
                    }
                    _isOutGoing = false;
                    //_isOutGoingCallEnd = false;
                    // _isReceivingCall = false;
                }
                if (_prev_state == TelephonyManager.CALL_STATE_RINGING) {
                    _prev_state = state;
                }
                _prev_state = state;
                break;
            }

            /**
             * Khi 1 cuoi goi vua ket thua, show {@link android.app.Notification} de nguoi dung biet duoc thong tin goi goi cuoc cua cuoc goi do
             */
//            if (_isOutGoingCallEnd) {
//                CallLog lastCall = getNewCallLog(); // lay thong tin cua cuoc goi gan nhat trong database
//                if (lastCall == null || lastCall.get_callDuration() == 0) {
//                    Log.e(TAG, "Last call is null");
//                } else {
//                    showLastCallInformationNotification(lastCall); // start to show notification
//                    _isOutGoingCallEnd = false;
//                }
//
//                /**
//                 * SHOW NOTIFICATION
//                 */
//                //  showLastCallInformationNotification(lastCall);
//            }
            //_isOutGoingCallEnd = false;
            // _isReceivingCall = false;

        }
    }


    /**
     * Method to get information of the last call of the device.
     * Note : when the code run android 6 and higher, we need to gain permission from runtime
     * for all dangerous permissions which include READ_CALL_LOG.
     *
     * @return : {@link CallLog} of the last call
     */
    public CallLog getNewCallLog() {
        CallLog _newCall = new CallLog();
        // check version at runtime to check READ_CALL_LOG permission whether version is 6 , higher or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this._context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission Read call log from user");
                // user deny permission for reading call log, return null to disable notification after a call
                return null;
            }
        }
        Cursor cursor = this._context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, null, null,
                null, android.provider.CallLog.Calls.DATE + " DESC");
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                // get all column index
                // int callIdIndex = cursor.getColumnIndex(android.provider.CallLog.Calls._ID);
                int number = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
                int callType = cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
                int date = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
                int duration = cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION);

                String _callType = cursor.getString(callType);
                int dircode = Integer.parseInt(_callType);
                // we only care about out going call type
                if (dircode == android.provider.CallLog.Calls.OUTGOING_TYPE) {
                    _isOutGoingCallEnd = true;
                    String _number = cursor.getString(number);
                    if (_number.length() < 10)
                        return null;
                    String _callDate = cursor.getString(date);
                    Date _callDayTime = new Date(Long.valueOf(_callDate));
                    int _callDuration = cursor.getInt(duration);


                    _myPackageFee.set_callDuration(_callDuration);
                    _myPackageFee.set_callTime(DateTimeManager.get_instance().convertToHm(_callDayTime.toString()));
                    _myPackageFee.set_outGoingPhoneNumber(_number);
                    int fee = _myPackageFee.CalculateCallFee();
                    _newCall.set_callDuration(_callDuration);
                    _newCall.set_callFee(fee);
                    return _newCall;
                }
            }
        }
        // close the cursor after using
        if (cursor != null) {
            cursor.close();
        }
        return null; // if error occurs, return null
    }

    /**
     * Method to show notification that present the last call information of current device.
     * See : See : https://developer.android.com/guide/topics/ui/notifiers/notifications.html#CreateNotification for Android notification guide
     *
     * @param lastCallLog : {@link CallLog} information of the last call
     */
    private void showLastCallInformationNotification(CallLog lastCallLog) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this._context);
        notificationBuilder.setSmallIcon(R.drawable.ic_call_blue_24dp);
        notificationBuilder.setContentTitle(this._context.getString(R.string.calllog_notification_title));
        // if current android version is 4.1, we show information via InboxStyle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            String[] callLogStrings = new String[]{
                    this._context.getString(R.string.call_duration) + lastCallLog.get_callDuration() + " giây",
                    this._context.getString(R.string.call_fee) + lastCallLog.get_callFee() + " đồng",
            };
            // add all lines into InboxStyle
            for (String line :
                    callLogStrings) {
                inboxStyle.addLine(line);
            }
            notificationBuilder.setStyle(inboxStyle); // set style

        } else { // otherwise, if the version is lower
            String content = this._context.getString(R.string.call_duration) + lastCallLog.get_callDuration() + " giây" + "\n" +
                    this._context.getString(R.string.call_fee) + lastCallLog.get_callDuration() + " đồng" + "\n";

            notificationBuilder.setContentText(content);
        }
        notificationBuilder.setPriority(Notification.PRIORITY_MAX); // priority for notification
        // sound for notification
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(notificationSoundUri);
        // vibrate when notify
        long[] vibrateSteps = {0, 500, 100, 200, 100, 200};
        notificationBuilder.setVibrate(vibrateSteps);

        // create dismiss the notification to auto
        notificationBuilder.setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this._context, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this._context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) this._context.getSystemService(Context.NOTIFICATION_SERVICE);
        // CALL_LOG_NOTIFICAIION_ID allows you to update the notification later on.
        mNotificationManager.notify(0, notificationBuilder.build());
    }

}
