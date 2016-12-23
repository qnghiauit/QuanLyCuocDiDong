package com.uit.nst95.quanlycuocdidong.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.uit.nst95.quanlycuocdidong.BackgroundService.CameraWidget;
import com.uit.nst95.quanlycuocdidong.FirebaseDB.*;
import com.uit.nst95.quanlycuocdidong.FirebaseDB.Data3GPackage;
import com.uit.nst95.quanlycuocdidong.Manager.DateTimeManager;
import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.Manager.PackageNetwork;
import com.uit.nst95.quanlycuocdidong.NetworkPackage.PackageFee;
import com.uit.nst95.quanlycuocdidong.R;
import com.uit.nst95.quanlycuocdidong.Manager.*;

import java.util.ArrayList;
import java.util.List;

import com.uit.nst95.quanlycuocdidong.DB.*;
import com.uit.nst95.quanlycuocdidong.NetworkPackage.*;
import com.uit.nst95.quanlycuocdidong.tools.CameraEngine;

import org.opencv.android.OpenCVLoader;

import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.*;

public class MainActivity extends AppCompatActivity {

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
    }

    private static final String TAG = MainActivity.class.getSimpleName(); // tag
    // permission request codes
    private static final int READ_PHONESTATE_PERMISSON_REQUEST_CODE = 1;
    private static final int READ_SMS_PERMISSION_REQUEST_CODE = 3;
    ////save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    //network provider
    private IProfile profile;

    //Variable
    private String _provider;
    private String _package;
    private int _id_logo_provider;
    private int _id_logo_package;
    private PackageFee _myPackageFee;
    private PhoneLogManager _logManager;
    private DAO_CallLog _callLogTableAdapter;
    private DAO_MessageLog _messageLogTableAdapter;
    private DAO_Statistic _statisticTableAdapter;
    private DateTimeManager _dateTimeManager;
    private long _lastCallUpdate;
    private long _lastMessageUpdate;
    private ProgressBar _progressBar;


    private DataManager<CuuHo> cuuHoDataManager;
    private DataManager<NganHang> nganHangDataManager;
    private DataManager<com.uit.nst95.quanlycuocdidong.FirebaseDB.Data3GPackage> data3GPackageDataManager;
    private DataManager<NhaMang> nhaMangDataManager;
    private DataManager<Taxi> taxiDataManager;
    private DataManager<TuVan> tuVanDataManager;

    public static ArrayList<CuuHo> cuuHos;
    public static ArrayList<NganHang> nganHangs;
    public static ArrayList<Data3GPackage> data3GPackages;
    public static ArrayList<NhaMang> nhaMangs;
    public static ArrayList<Taxi> taxis;
    public static ArrayList<TuVan> tuVans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cuuHoDataManager = new DataManager<>(CuuHo.class);
        //cuuHoDataManager.setOnLoadDataComplete(this);

        cuuHos = cuuHoDataManager.getDataset();

        nganHangDataManager = new DataManager<>(NganHang.class);
        nganHangs = nganHangDataManager.getDataset();

        data3GPackageDataManager = new DataManager<>(Data3GPackage.class);
        data3GPackages = data3GPackageDataManager.getDataset();

        nhaMangDataManager = new DataManager<>(NhaMang.class);
        nhaMangs = nhaMangDataManager.getDataset();

        taxiDataManager = new DataManager<>(Taxi.class);
        taxis = taxiDataManager.getDataset();

        tuVanDataManager = new DataManager<>(TuVan.class);
        tuVans = tuVanDataManager.getDataset();

//        nganHangDataManager.setOnLoadDataComplete(new OnLoadDataComplete() {
//            @Override
//            public void onComplete() {
//                NganHang itemVuaLay = nganHangDataManager.getDataset().get(nganHangDataManager.getDataset().size()-1);
//                Toast.makeText(MainActivity.this, itemVuaLay.getNAME(), Toast.LENGTH_SHORT).show();
//            }
//        });

        _dateTimeManager = DateTimeManager.get_instance();
        this._callLogTableAdapter = new DAO_CallLog(this);
        this._statisticTableAdapter = new DAO_Statistic(this);
        this._messageLogTableAdapter = new DAO_MessageLog(this);
        this._callLogTableAdapter.Open();
        this._messageLogTableAdapter.Open();
        this._statisticTableAdapter.Open();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get information about provider and package of user
        getUserInformation();

        // Create a network provider info
        createProfile();

        // Create the AccountHeader with compact template
        buildHeader(true, savedInstanceState);

        //Create the drawer
        createDrawer(toolbar, savedInstanceState);

        //Set default Fragment - Home
        setActionBarTitle(getString(R.string.drawer_item_home));
        HomeFragment homeFragment = HomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
        //Them progress bar + cho chay Asynctask
        _progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (this._progressBar.isShown()) {
            this._progressBar.setVisibility(View.GONE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            new DatabaseExecuteTask(_lastCallUpdate, _lastMessageUpdate).execute();
        }

        /**
         * The application need to read permission in dangerous permission group PHONE.
         * So we need to check that if the current device's version is Android M or higher.
         * If so, we need to request permission at runtime
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // request read phone state
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // should we show an explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    // code to show an explanation here
                    new ReadPhoneStatePermissionConfirmDialog().show(this.getSupportFragmentManager(), TAG);
                } else {
                    // no explanation needed , we can request permission
                    ActivityCompat.requestPermissions(this, new String[]{
                                    Manifest.permission.READ_PHONE_STATE, // grant[0]
                                    Manifest.permission.READ_CALL_LOG, //  grant[1]
                                    Manifest.permission.READ_CONTACTS, //  grant[2]
                                    Manifest.permission.READ_SMS}, //  grant[3]
                            READ_PHONESTATE_PERMISSON_REQUEST_CODE);
                }

            } else { // if permission granted, update phone calls and messages data
                new DatabaseExecuteTask(_lastCallUpdate, _lastMessageUpdate).execute();
            }

        }

        // get intent from widget
        Intent intent = this.getIntent();
        // make sure we have valid intent
        if (intent != null) {
            if (intent.getAction() != null && intent.getAction().equals(CameraWidget.TAG)) {
                CameraFragment cameraFragment = (CameraFragment) CameraFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, cameraFragment).commit();
            }
        }
    }

    @Override
    protected void onDestroy() {

        saveSharedPreferences();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        saveSharedPreferences();
        super.onStop();

    }

    private void createDrawer(Toolbar toolbar, Bundle savedInstanceState) {
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIdentifier(1).withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_report).withIdentifier(2).withIcon(FontAwesome.Icon.faw_bar_chart),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_check_day).withIdentifier(3).withIcon(FontAwesome.Icon.faw_search),
                        // to launch camera activity
                        new PrimaryDrawerItem().withName(R.string.launch_camera_activity).withIdentifier(13).withIcon(FontAwesome.Icon.faw_camera),
                        new SectionDrawerItem().withName(R.string.drawer_item_utility_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_promotion).withIdentifier(4).withIcon(FontAwesome.Icon.faw_cart_plus),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_cash).withIdentifier(5).withIcon(FontAwesome.Icon.faw_money),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_callback).withIdentifier(6).withIcon(FontAwesome.Icon.faw_phone),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_info_owner).withIdentifier(7).withIcon(FontAwesome.Icon.faw_user),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_contacts).withIdentifier(8).withIcon(FontAwesome.Icon.faw_taxi),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_datamobile).withIdentifier(9).withIcon(FontAwesome.Icon.faw_exchange)
                ) // add the items we want to use with our Drawer
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_item_setting).withIdentifier(11).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about_us).withIdentifier(12).withIcon(FontAwesome.Icon.faw_info_circle)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment fragment = null;
                        Class fragmentClass = null;
                        long idDrawerItem = drawerItem.getIdentifier();
                        if (idDrawerItem == 11) {
                            fragment = SettingFragment.newInstance(_myPackageFee);
                        } else {
                            if (idDrawerItem == 1) {
                                fragmentClass = HomeFragment.class;
                            } else if (idDrawerItem == 2) {
                                fragmentClass = StatisticsFragment.class;
                            } else if (idDrawerItem == 3) {
                                fragmentClass = CheckByDayFragment.class;
                            } else if (idDrawerItem == 4) {
                                fragmentClass = PromotionFragment.class;
                            } else if (idDrawerItem == 5) {
                                fragmentClass = MakeLoanFragment.class;
                            } else if (idDrawerItem == 6) {
                                fragmentClass = CallbackFragment.class;
                            } else if (idDrawerItem == 7) {
                                fragmentClass = SubscriberInforFragment.class;
                            } else if (idDrawerItem == 8) {
                                fragmentClass = UsefulPhoneNumbersFragment.class;
                            } else if (idDrawerItem == 9) {
                                fragmentClass = DataServicesFragment.class;
                            } else if (idDrawerItem == 12) {
                                fragmentClass = AboutFragment.class;
                            } else if (idDrawerItem == 13) {
                                fragmentClass = CameraFragment.class;
                            }
                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        //set ActionBar Title
                        if (drawerItem instanceof Nameable) {
                            setActionBarTitle(((Nameable) drawerItem).getName().getText(MainActivity.this));
                        }
                        // Insert the fragment by replacing any existing fragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
                        // }
                        //return true/false - remain/close drawer after select item
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private void createProfile() {
        profile = new ProfileDrawerItem().withName(getString(R.string.drawer_item_profile_network_provider).concat(_provider))
                .withEmail(getString(R.string.drawer_item_profile_package).concat(_package))
                .withIcon(getResources().getDrawable(_id_logo_provider));
    }

    /**
     * small helper method to reuse the logic to build the AccountHeader
     * this will be used to replace the header of the drawer with a compact/normal header
     *
     * @param compact
     * @param savedInstanceState
     */
    private void buildHeader(boolean compact, Bundle savedInstanceState) {
        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withCompactStyle(compact)
                .addProfiles(
                        profile
                )
                .withSavedInstance(savedInstanceState)
                .withSelectionListEnabledForSingleProfile(false)
                .build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>" + title + "</font>"));
    }

    public void saveSharedPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DefinedConstant.KEY_PACKAGE, _package);
        editor.putString(DefinedConstant.KEY_PROVIDER, _provider);
        editor.putInt(DefinedConstant.KEY_ID_LOGO_PROVIDER, _id_logo_provider);
        editor.putInt(DefinedConstant.KEY_ID_LOGO_PACKAGE, _id_logo_package);
        editor.putLong(KEY_LAST_TIME_UPDATE_CALL, _lastCallUpdate);
        editor.putLong(KEY_LAST_TIME_UPDATE_MESSAGE, _lastMessageUpdate);
        // Commit the edits!
        editor.apply();
    }

    public void getUserInformation() {
        Intent callerIntent = getIntent();
        Bundle packegeFromCaller = callerIntent.getBundleExtra(DefinedConstant.BUNDLE_NAME);
        if (packegeFromCaller != null) {
            PackageNetwork goicuoc = (PackageNetwork) packegeFromCaller.getSerializable(DefinedConstant.KEY_PACKAGE);
            if (goicuoc != null) {
                _provider = goicuoc.getProviderName();
                _package = goicuoc.getPackageName();
                if (_provider.equals(DefinedConstant.MOBIFONE)) {
                    _id_logo_provider = R.drawable.mf;
                } else if (_provider.equals(DefinedConstant.VINAPHONE)) {
                    _id_logo_provider = R.drawable.vp;
                } else if (_provider.equals(DefinedConstant.GMOBILE)) {
                    _id_logo_provider = R.drawable.gm;
                } else if (_provider.equals(DefinedConstant.VIETTEL)) {
                    _id_logo_provider = R.drawable.vt;
                } else if (_provider.equals(DefinedConstant.VIETNAMOBILE)) {
                    _id_logo_provider = R.drawable.vmb;
                }
                _id_logo_package = goicuoc.getIdImage();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                _lastCallUpdate = settings.getLong(KEY_LAST_TIME_UPDATE_CALL, DefinedConstant.TIME_DEDAULT);
                _lastMessageUpdate = settings.getLong(KEY_LAST_TIME_UPDATE_MESSAGE, DefinedConstant.TIME_DEDAULT);
                this.InitPackage(_package);
                this._logManager = PhoneLogManager.get_instance(this, _myPackageFee);
            }
        } else {
            // Restore preferences
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            _package = settings.getString(DefinedConstant.KEY_PACKAGE, DefinedConstant.VALUE_DEFAULT);
            _provider = settings.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);
            _id_logo_provider = settings.getInt(DefinedConstant.KEY_ID_LOGO_PROVIDER, 0);
            _id_logo_package = settings.getInt(DefinedConstant.KEY_ID_LOGO_PACKAGE, 0);
            _lastCallUpdate = settings.getLong(KEY_LAST_TIME_UPDATE_CALL, DefinedConstant.TIME_DEDAULT);
            _lastMessageUpdate = settings.getLong(KEY_LAST_TIME_UPDATE_MESSAGE, DefinedConstant.TIME_DEDAULT);
            this.InitPackage(this._package);
            this._logManager = PhoneLogManager.get_instance(this, _myPackageFee);
        }
        saveSharedPreferences();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * Handle when user accept the permission you request or not
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case READ_PHONESTATE_PERMISSON_REQUEST_CODE:
                if (grantResults.length > 0) {
                    //
                    // do some thing related to read phone state
                    //
                    //  new DatabaseExecuteTask(_lastCallUpdate, _lastMessageUpdate).execute();
                    boolean isReadCallLogPermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED; // grant[1] is result for requesting read call log permission
                    boolean isReadMessagePermissionGranted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (isReadCallLogPermissionGranted && isReadMessagePermissionGranted) {
                        Log.d(TAG, "start to execute database in background");
                        new DatabaseExecuteTask(_lastCallUpdate, _lastMessageUpdate).execute();
                    }
                    _progressBar.setVisibility(View.GONE);

                } else {
                    Toast.makeText(this, R.string.permission_not_granted_message, Toast.LENGTH_LONG).show();
                }
                break;
//            case READ_SMS_PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // run asynchronous to run database service
//
//                } else {
//                    Toast.makeText(this, R.string.permission_not_granted_message, Toast.LENGTH_LONG).show();
//                }
//                break;
            default:
                break;

        }
    }

//    @Override
//    public void onComplete() {
//        CuuHo itemVuaLoadXong = cuuHoDataManager.getDataset().get(cuuHoDataManager.getDataset().size()-1);
//        Toast.makeText(this, itemVuaLoadXong.getNAME(), Toast.LENGTH_SHORT).show();
//    }

    /**
     * Dialog for explaining read call log permission
     */
    public static class ReadPhoneStatePermissionConfirmDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.read_phone_state_request_permission)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    READ_PHONESTATE_PERMISSON_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // finish activity if user deny permission
                                    dialog.dismiss();
                                }
                            })
                    .create();
        }
    }


    public void RenewCallData(long time) {

        List<CallLog> _listCall = _logManager.LoadCallLogAfterTimeSpan(time);
        if (_listCall.isEmpty())
            return;
        for (CallLog i : _listCall) {
            if (i.get_callNumber().length() >= 10) {
                _callLogTableAdapter.CreateCallLogRow(i);
                String callDate = _dateTimeManager.convertToDMYHms(i.get_callDate());
                int callFee = i.get_callFee();
                int callDuration = i.get_callDuration();
                if (_statisticTableAdapter.FindStatisticByMonthYear(_dateTimeManager.getMonth(callDate), _dateTimeManager.getYear(callDate)) == null) {
                    _statisticTableAdapter.CreateStatisticRow(_dateTimeManager.getMonth(callDate), _dateTimeManager.getYear(callDate));

                }
                if (i.get_callType() == 0) {
                    _statisticTableAdapter.UpdateInnerCallInfo(_dateTimeManager.getMonth(callDate),
                            _dateTimeManager.getYear(callDate), callFee, callDuration);


                } else if (i.get_callType() == 1) {
                    _statisticTableAdapter.UpdateOuterCallInfo(_dateTimeManager.getMonth(callDate),
                            _dateTimeManager.getYear(callDate), callFee, callDuration);

                }
            }
        }

        _listCall.clear();
        _lastCallUpdate = _logManager.GetLastedCallTime();
        saveSharedPreferences();

    }

    public void RenewMessageData(long time) {

        List<MessageLog> _listMessage = _logManager.LoadMessageLogAfterTimeSpan(time);
        if (_listMessage.isEmpty())
            return;
        for (MessageLog i : _listMessage) {
            if (i.get_receiverNumber().length() >= 10) {
                _messageLogTableAdapter.CreateMessageLogRow(i);
                String messageDate = _dateTimeManager.convertToDMYHms(i.get_messageDate());
                int messageFee = i.get_messageFee();
                if (_statisticTableAdapter.FindStatisticByMonthYear(_dateTimeManager.getMonth(messageDate)
                        , _dateTimeManager.getYear(messageDate)) == null) {
                    _statisticTableAdapter.CreateStatisticRow(_dateTimeManager.getMonth(messageDate)
                            , _dateTimeManager.getYear(messageDate));

                }
                if (i.get_messageType() == 0) {
                    _statisticTableAdapter.UpdateInnerMessageInfo(_dateTimeManager.getMonth(messageDate)
                            , _dateTimeManager.getYear(messageDate), messageFee);
                } else if (i.get_messageType() == 1) {
                    _statisticTableAdapter.UpdateOuterMessageInfo(_dateTimeManager.getMonth(messageDate)
                            , _dateTimeManager.getYear(messageDate), messageFee);
                }
            }
        }
        _listMessage.clear();

        _lastMessageUpdate = _logManager.GetLastedMessageTime();
        saveSharedPreferences();

    }

    public void FirstInitCallLog() {

        List<CallLog> _listCall = _logManager.LoadCallLogFromPhone();
        if (_listCall.isEmpty())
            return;
        for (CallLog i : _listCall) {
            // CallLog temp = i;
            if (i.get_callNumber().length() >= 10) {
                _callLogTableAdapter.CreateCallLogRow(i);
                String callDate = _dateTimeManager.convertToDMYHms(i.get_callDate());
                int callFee = i.get_callFee();
                int callDuration = i.get_callDuration();
                if (_statisticTableAdapter.FindStatisticByMonthYear(_dateTimeManager.getMonth(callDate), _dateTimeManager.getYear(callDate)) == null) {
                    _statisticTableAdapter.CreateStatisticRow(_dateTimeManager.getMonth(callDate), _dateTimeManager.getYear(callDate));

                }
                if (i.get_callType() == 0) {
                    _statisticTableAdapter.UpdateInnerCallInfo(_dateTimeManager.getMonth(callDate),
                            _dateTimeManager.getYear(callDate), callFee, callDuration);


                } else if (i.get_callType() == 1) {
                    _statisticTableAdapter.UpdateOuterCallInfo(_dateTimeManager.getMonth(callDate),
                            _dateTimeManager.getYear(callDate), callFee, callDuration);

                }
            }
        }

        _listCall.clear();
        _lastCallUpdate = _logManager.GetLastedCallTime();
        saveSharedPreferences();
    }

    public void FirstInitMessageLog() {

        List<MessageLog> _listMessage = _logManager.LoadMessageLogFromPhone();
        if (_listMessage.isEmpty())
            return;
        for (MessageLog i : _listMessage) {
            if (i.get_receiverNumber().length() >= 10) {
                _messageLogTableAdapter.CreateMessageLogRow(i);
                String messageDate = _dateTimeManager.convertToDMYHms(i.get_messageDate());
                int messageFee = i.get_messageFee();
                if (_statisticTableAdapter.FindStatisticByMonthYear(_dateTimeManager.getMonth(messageDate), _dateTimeManager.getYear(messageDate)) == null) {
                    _statisticTableAdapter.CreateStatisticRow(_dateTimeManager.getMonth(messageDate), _dateTimeManager.getYear(messageDate));

                }
                if (i.get_messageType() == 0) {
                    _statisticTableAdapter.UpdateInnerMessageInfo(_dateTimeManager.getMonth(messageDate), _dateTimeManager.getYear(messageDate), messageFee);
                } else if (i.get_messageType() == 1) {

                    _statisticTableAdapter.UpdateOuterMessageInfo(_dateTimeManager.getMonth(messageDate), _dateTimeManager.getYear(messageDate), messageFee);
                }
            }
        }

        _listMessage.clear();
        _lastMessageUpdate = _logManager.GetLastedMessageTime();
        saveSharedPreferences();

    }

    public void InitPackage(String _package) {
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

    private class DatabaseExecuteTask extends AsyncTask<Void, Integer, Void> {
        long lastCallTime;
        long lastMessageTime;

        public DatabaseExecuteTask(long calltime, long messagetime) {
            lastCallTime = calltime;
            lastMessageTime = messagetime;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            if (_progressBar != null) {
                _progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (_lastCallUpdate == 0) {
                _callLogTableAdapter.DeleteAllData();
                _statisticTableAdapter.ResetCallData();
                FirstInitCallLog();
                saveSharedPreferences();
            } else {
                RenewCallData(_lastCallUpdate);
                saveSharedPreferences();
            }
            if (_lastMessageUpdate == 0) {
                _messageLogTableAdapter.DeleteAllData();
                _statisticTableAdapter.ResetMessageData();
                FirstInitMessageLog();
                saveSharedPreferences();
            } else {
                RenewMessageData(_lastMessageUpdate);
                saveSharedPreferences();
            }
            saveSharedPreferences();
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            _progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            _progressBar.setVisibility(View.INVISIBLE);
            _progressBar.setVisibility(View.GONE);
            saveSharedPreferences();
            // reload home fragment
            HomeFragment homeFragment = HomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
        }
    }

    public void ChangeFragment(int idDrawerItem) {
        Fragment fragment = null;
        Class fragmentClass = null;
        if (idDrawerItem == 11) {
            fragment = SettingFragment.newInstance(_myPackageFee);
            setActionBarTitle("Cài đặt");
        } else {
            if (idDrawerItem == 2) {
                fragmentClass = StatisticsFragment.class;
            } else if (idDrawerItem == 3) {
                fragmentClass = CheckByDayFragment.class;
            } else if (idDrawerItem == 4) {
                fragmentClass = PromotionFragment.class;
            } else if (idDrawerItem == 5) {
                fragmentClass = MakeLoanFragment.class;
            } else if (idDrawerItem == 8) {
                fragmentClass = UsefulPhoneNumbersFragment.class;
            } else if (idDrawerItem == 9) {
                fragmentClass = DataServicesFragment.class;
            } else if (idDrawerItem == 13) {
                fragmentClass = CameraFragment.class;
            }
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                if (result.getDrawerItem(idDrawerItem) instanceof Nameable) {
                    setActionBarTitle(((Nameable) result.getDrawerItem(idDrawerItem)).getName().getText(MainActivity.this));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    public void ResetProvider() {
        SharedPreferences settings = getSharedPreferences(DefinedConstant.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DefinedConstant.KEY_PACKAGE, "");
        editor.putString(DefinedConstant.KEY_PROVIDER, "");
        editor.apply();
        Intent myIntent = new Intent(MainActivity.this, ChooseNetworkProviderActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent);
    }
}
