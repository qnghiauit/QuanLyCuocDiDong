package com.uit.nst95.quanlycuocdidong.BackgroundService;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
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

import java.util.Date;

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

    private static CustomPhoneStateListener customPhoneStateListener;
    public PhoneStateReceiver() {
        //_isReceived = false;
        _incomingNumber = "";
        _prev_state = -1;
        _isOutGoingCallEnd = false;

    }


    public void InitPackage() {
        SharedPreferences setting = _context.getSharedPreferences("MySetting", Context.MODE_PRIVATE);
        String _package = setting.getString("GoiCuoc", "Unknown");
        _isAllowPopUp = setting.getBoolean("AllowPopup", false);
        _myPackageFee = new MobiCard(); // code for TESTING !!!
        _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone); // code for TESTING !!
//        switch (_package) {
//            case ChonGoiCuocActivity.MOBICARD: {
//                _myPackageFee = new MobiCard();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
//                break;
//            }
//            case ChonGoiCuocActivity.MOBIGOLD: {
//                _myPackageFee = new MobiGold();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
//                break;
//            }
//            case ChonGoiCuocActivity.MOBIQ: {
//                _myPackageFee = new MobiQ();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
//                break;
//            }
//            case ChonGoiCuocActivity.QSTUDENT: {
//                _myPackageFee = new QStudent();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
//                break;
//            }
//            case ChonGoiCuocActivity.QTEEN: {
//                _myPackageFee = new QTeen();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
//                break;
//            }
//            case ChonGoiCuocActivity.QKIDS: {
//                _myPackageFee = new Qkids();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.mobifone);
//                break;
//            }
//            case ChonGoiCuocActivity.VINACARD: {
//                _myPackageFee = new VinaCard();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
//                break;
//            }
//            case ChonGoiCuocActivity.VINAXTRA: {
//                _myPackageFee = new VinaXtra();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
//                break;
//            }
//            case ChonGoiCuocActivity.TALKSTUDENT: {
//                _myPackageFee = new TalkStudent();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
//                break;
//            }
//            case ChonGoiCuocActivity.TALKTEEN: {
//                _myPackageFee = new TalkTeen();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vinaphone);
//                break;
//            }
//            case ChonGoiCuocActivity.ECONOMY: {
//                _myPackageFee = new Economy();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
//                break;
//            }
//            case ChonGoiCuocActivity.TOMATO: {
//                _myPackageFee = new Tomato();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
//                break;
//            }
//            case ChonGoiCuocActivity.STUDENT: {
//                _myPackageFee = new Student();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
//                break;
//            }
//            case ChonGoiCuocActivity.SEA: {
//                _myPackageFee = new SeaPlus();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
//                break;
//            }
//            case ChonGoiCuocActivity.HISCHOOL: {
//                _myPackageFee = new HiSchool();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
//                break;
//            }
//            case ChonGoiCuocActivity.SEVENCOLOR: {
//                _myPackageFee = new SevenColor();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
//                break;
//            }
//            case ChonGoiCuocActivity.BUONLANG: {
//                _myPackageFee = new TomatoBL();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.viettel);
//                break;
//            }
//            case ChonGoiCuocActivity.BIGSAVE: {
//                _myPackageFee = new BigSave();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
//                break;
//            }
//            case ChonGoiCuocActivity.BIGKOOL: {
//                _myPackageFee = new BigKool();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
//                break;
//            }
//            case ChonGoiCuocActivity.TIPHU2: {
//                _myPackageFee = new BillionareTwo();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
//                break;
//            }
//            case ChonGoiCuocActivity.TIPHU3: {
//                _myPackageFee = new BillionareThree();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
//                break;
//            }
//            case ChonGoiCuocActivity.TIPHU5: {
//                _myPackageFee = new BillionareFive();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.Gmobile);
//                break;
//            }
//            case ChonGoiCuocActivity.VMONE: {
//                _myPackageFee = new VMOne();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
//                break;
//            }
//            case ChonGoiCuocActivity.VMAX: {
//                _myPackageFee = new VMax();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
//                break;
//            }
//            case ChonGoiCuocActivity.SV2014: {
//                _myPackageFee = new SV2014();
//                _myPackageFee.set_myNetwork(NumberHeaderManager.networkName.vietnamobile);
//                break;
//            }
//        }

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE") || intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            _context = context;
//            _callAdapter = new DAO_CallLog(context);
//            _statisticTableAdapter = new DAO_Statistic(context);
//            _callAdapter.Open();
//            _statisticTableAdapter.Open();
//            _phoneCallLog = new PhoneLogManager(_context, _myPackageFee);
            this.InitPackage();
            _isOutGoingCallEnd = false;
            //Sua loi tao ra nhieu PhoneStateListener trong chuong trinh  gay trung Notify
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(customPhoneStateListener == null) {
                customPhoneStateListener = new CustomPhoneStateListener(_context);//,_myPackageFee);
                telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }
           // Toast.makeText(this._context, "Starts Receivers", Toast.LENGTH_LONG).show();
        }

    }

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
            if (incomingNumber != null && incomingNumber.length() > 0) {
                _incomingNumber = incomingNumber;
            }
            //Toast.makeText(_context, incomingNumber, Toast.LENGTH_SHORT).show();
            _isReceivingCall = false;
           // if(incomingNumber == null || incomingNumber == "" || incomingNumber.isEmpty() )
           // {
            //    Toast.makeText(_context, "It's outgoing call!!!", Toast.LENGTH_SHORT).show();
            //}
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING: {
                    _prev_state = state;
                    _isReceivingCall = true;
                    break;
                }
                case TelephonyManager.CALL_STATE_OFFHOOK: {
                    if(incomingNumber == null || incomingNumber.isEmpty() || incomingNumber.equals(""))
                    {
                        _isOutGoing = true;
                    }
                    else
                    {
                        _isOutGoing = false;
                    }
                    _prev_state = state;
                    break;
                }
                case TelephonyManager.CALL_STATE_IDLE: {
                    if (_prev_state == TelephonyManager.CALL_STATE_OFFHOOK && _isReceivingCall == false) {
                        _prev_state = state;
                        _isOutGoingCallEnd = true;

                    }
                    if (_prev_state == TelephonyManager.CALL_STATE_RINGING) {
                        _prev_state = state;
                    }
                    break;
                }
            }
            /**
             * Khi 1 cuoi goi vua ket thua, show {@link android.app.Notification} de nguoi dung biet duoc thong tin goi goi cuoc cua cuoc goi do
             */
            if (_isOutGoingCallEnd == true && _isOutGoing == true) {
                CallLog lastCall = getNewCallLog();
                //Loi version cua log qua ky tu (Thang)
               // Log.d(TAG,"Last call");
                if (lastCall == null || lastCall.get_callDuration() ==0) {
                    //Log.e(TAG,"Last call is null");
                } else {
                    //Loi version cua log qua ky tu (Thang)
                    //Log.d(TAG,"Last call is NOT null");
                    showLastCallInformationNotification(lastCall); // start to show notification
                    _isOutGoingCallEnd = false;
                }

                /** Dong if nay KHONG hoat dong duoc !!!!!!!!!!!!!!!!!!!! */

//                if (lastCall != null && lastCall.get_callDuration() > 0 && _isOutGoing == true && _isAllowPopUp == true) {
//                    try {
//                        final AlertDialog alertDialog = new AlertDialog.Builder(_context, AlertDialog.THEME_HOLO_LIGHT).create();
//                        //final AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(_context,android.R.style.Theme_Material_Light_Dialog)).create();
//                        alertDialog.setTitle("Call Information");
//                        alertDialog.setMessage("Duration: " + lastCall.get_callDuration() + "secs" + "\nCost: " + lastCall.get_callFee() + " VND");
//
//                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                alertDialog.dismiss();
//                            }
//                        });
//                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//                        layoutParams.gravity = Gravity.TOP;
//                        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//                        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                        layoutParams.alpha = 1.0f;
//                        layoutParams.buttonBrightness = 1.0f;
//                        layoutParams.windowAnimations = android.R.style.Theme_Material_Light_Dialog_Alert;
//
//
//                        //alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                        alertDialog.getWindow().setAttributes(layoutParams);
//                        alertDialog.show();
//                        _isOutGoing = false;
//                        _isOutGoingCallEnd = false;
//                        _isReceivingCall = false;
//
//                    } catch (Exception e) {
//                        e.getLocalizedMessage();
//                    }


                /**
                 * SHOW NOTIFICATION
                 */
                //  showLastCallInformationNotification(lastCall);
            }
            _isOutGoingCallEnd = false;
            _isReceivingCall = false;
        }
    }


    /**
     * Method to get information of the last call of the device.
     * Note : when the code run android 6 and higher, we need to gain permission from runtime for all dangerous permissions which include READ_CALL_LOG.
     * See
     *
     * @return
     */
    public CallLog getNewCallLog() {
        CallLog _newCall = new CallLog();
        // check version at runtime to check whether version is 6 , higher or not
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(_context, Manifest.permission_group.PHONE) == PackageManager.PERMISSION_GRANTED)
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
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
        }
        return null; // if error occurs, return null
    }

    /**
     * Method to show notification that present the last call information of current device.
     *
     * @param lastCallLog : {@link CallLog} information of the last call
     */
    private void showLastCallInformationNotification(CallLog lastCallLog) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this._context);
        notificationBuilder.setSmallIcon(R.drawable.calllog_icon);
        notificationBuilder.setContentTitle(this._context.getString(R.string.calllog_notification_title));
        // if current android version is 4.1, we show information via InboxStyle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
            inboxStyle.setBigContentTitle("Thoại với " + lastCallLog.get_callNumber());
            String[] callLogStrings = new String[]{
                    this._context.getString(R.string.call_duration) + lastCallLog.get_callDuration() + " giây",
                    this._context.getString(R.string.call_fee) + lastCallLog.get_callFee() + " đồng",
                    this._context.getString(R.string.call_date) + lastCallLog.get_callDate(),
            };
            // add all lines into InboxStyle
            for (String line :
                    callLogStrings) {
                inboxStyle.addLine(line);
            }
            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
            notificationBuilder.setStyle(inboxStyle);

        } else { // otherwise, if the version is lower
            String content = this._context.getString(R.string.call_duration) + lastCallLog.get_callDuration() + " giây" + "\n" +
                    this._context.getString(R.string.call_fee) + lastCallLog.get_callDuration() + " đồng" + "\n"
                    + this._context.getString(R.string.call_date) + lastCallLog.get_callDuration();

            notificationBuilder.setContentText(content);
        }

        // sound for notification
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(notificationSoundUri);

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
        mNotificationManager.notify(CALL_LOG_NOTIFICATION_ID, notificationBuilder.build());
    }

}
