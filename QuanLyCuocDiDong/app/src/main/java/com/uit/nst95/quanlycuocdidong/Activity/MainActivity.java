package com.uit.nst95.quanlycuocdidong.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
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
import com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant;
import com.uit.nst95.quanlycuocdidong.Manager.PackageNetwork;
import com.uit.nst95.quanlycuocdidong.R;

import static com.uit.nst95.quanlycuocdidong.Manager.DefinedConstant.PERMISSION_PHONE_GROUP_REQUEST;

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get information about provider and package of user
        getUserInformation();

        //Write your code here ahihi :))





        // Create a network provider info
        createProfile();

        // Create the AccountHeader with compact template
        buildHeader(true, savedInstanceState);

        //Create the drawer
        createDrawer(toolbar,savedInstanceState);

        //Set default Fragment - Home
        setActionBarTitle(getString(R.string.drawer_item_home));
        HomeFragment homeFragment = HomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
    }

    private void createDrawer(Toolbar toolbar, Bundle savedInstanceState){
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIdentifier(1).withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_report).withIdentifier(2).withIcon(FontAwesome.Icon.faw_bar_chart),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_check_day).withIdentifier(3).withIcon(FontAwesome.Icon.faw_search),
                        new SectionDrawerItem().withName(R.string.drawer_item_utility_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_promotion).withIdentifier(4).withIcon(FontAwesome.Icon.faw_cart_plus),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_cash).withIdentifier(5).withIcon(FontAwesome.Icon.faw_money),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_callback).withIdentifier(6).withIcon(FontAwesome.Icon.faw_phone),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_info_owner).withIdentifier(7).withIcon(FontAwesome.Icon.faw_user),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_contacts).withIdentifier(8).withIcon(FontAwesome.Icon.faw_taxi),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_datamobile).withIdentifier(9).withIcon(FontAwesome.Icon.faw_exchange),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_add_credit).withIdentifier(10).withIcon(FontAwesome.Icon.faw_camera)
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
                        } else if (idDrawerItem == 10) {
                            fragmentClass = AddCreditFragment.class;
                        } else if (idDrawerItem == 11) {
                            fragmentClass = SettingFragment.class;
                        } else if (idDrawerItem == 12) {
                            fragmentClass = AboutFragment.class;
                        }
                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //set ActionBar Title
                        if (drawerItem instanceof Nameable) {
                            setActionBarTitle(((Nameable) drawerItem).getName().getText(MainActivity.this));
                        }
                        // Insert the fragment by replacing any existing fragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
                        //return true/false - remain/close drawer after select item
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private void createProfile(){
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

    private void setActionBarTitle(String title){
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>"+ title +"</font>"));
    }
    public void saveSharedPreferences() {
        SharedPreferences settings = getSharedPreferences(DefinedConstant.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DefinedConstant.KEY_PACKAGE, _package);
        editor.putString(DefinedConstant.KEY_PROVIDER, _provider);
        editor.putInt(DefinedConstant.KEY_ID_LOGO_PROVIDER,_id_logo_provider);
        editor.putInt(DefinedConstant.KEY_ID_LOGO_PACKAGE, _id_logo_package);
        // Commit the edits!
        editor.commit();
    }
    public void getUserInformation() {
        Intent callerIntent = getIntent();
        Bundle packegeFromCaller = callerIntent.getBundleExtra(DefinedConstant.BUNDLE_NAME);
        if (packegeFromCaller != null) {
            PackageNetwork goicuoc = (PackageNetwork) packegeFromCaller.getSerializable(DefinedConstant.KEY_PACKAGE);
            if (goicuoc != null) {
                _provider = goicuoc.getProviderName();
                _package = goicuoc.getPackageName();
                if(_provider.equals(DefinedConstant.MOBIFONE)){
                    _id_logo_provider = R.drawable.mf;
                } else if (_provider.equals(DefinedConstant.VINAPHONE)){
                    _id_logo_provider = R.drawable.vp;
                } else if(_provider.equals(DefinedConstant.GMOBILE)){
                    _id_logo_provider = R.drawable.gm;
                } else if (_provider.equals(DefinedConstant.VIETTEL)){
                    _id_logo_provider = R.drawable.vt;
                } else if (_provider.equals(DefinedConstant.VIETNAMOBILE)){
                    _id_logo_provider = R.drawable.vmb;
                }
                _id_logo_package = goicuoc.getIdImage();
            }
        } else {
            // Restore preferences
            SharedPreferences settings = getSharedPreferences(DefinedConstant.PREFS_NAME, MODE_PRIVATE);
            _package = settings.getString(DefinedConstant.KEY_PACKAGE, DefinedConstant.VALUE_DEFAULT);
            _provider = settings.getString(DefinedConstant.KEY_PROVIDER, DefinedConstant.VALUE_DEFAULT);
            _id_logo_provider =  settings.getInt(DefinedConstant.KEY_ID_LOGO_PROVIDER,0);
            _id_logo_package = settings.getInt(DefinedConstant.KEY_ID_LOGO_PACKAGE, 0);
        }
        saveSharedPreferences();
    }
    @Override
    protected void onStart() {
        super.onStart();
        /**
         * The application need to read permission in dangerous permission group PHONE.
         * So we need to check that if the current device's version is Android M or higher. If so, we need to request permission at runtime
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // should we show an explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)) {
                    // code to show an explanation here
                    //
                    // to code later
                    //

                } else {
                    // no explanation needed , we can request permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, DefinedConstant.PERMISSION_PHONE_GROUP_REQUEST);
                }

            }
        }
    }

    /**
     * Handle when user accept the pemission you request or not
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case DefinedConstant.PERMISSION_PHONE_GROUP_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                    // do some thing related to read phone state
                    //
                } else {
                    Toast.makeText(this, R.string.permission_not_granted_message, Toast.LENGTH_LONG).show();
                }
        }
    }
}
