package com.uit.nst95.quanlycuocdidong.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.uit.nst95.quanlycuocdidong.R;

import static android.R.attr.fragment;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_PHONE_GROUP_REQUEST = 1; // request code for PHONE group permission

    ////save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    //network provider
    private IProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.actionbar_title);

        // Create a network provider info
        profile = new ProfileDrawerItem().withName(getString(R.string.drawer_item_profile_network_provider))
                .withEmail(getString(R.string.drawer_item_profile_package))
                .withIcon(getResources().getDrawable(R.drawable.profile));

        // Create the AccountHeader with compact template
        buildHeader(true, savedInstanceState);

        //Create the drawer
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
                        // Insert the fragment by replacing any existing fragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
                        //return true/false - remain/close drawer after select item
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Set default Fragment - Home
        HomeFragment homeFragment = HomeFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
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
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, PERMISSION_PHONE_GROUP_REQUEST);
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
            case PERMISSION_PHONE_GROUP_REQUEST:
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
