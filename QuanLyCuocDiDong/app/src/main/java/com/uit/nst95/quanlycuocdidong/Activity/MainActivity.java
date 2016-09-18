package com.uit.nst95.quanlycuocdidong.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.uit.nst95.quanlycuocdidong.R;

public class MainActivity extends AppCompatActivity {

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
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_report).withDescription("This is a description").withIcon(FontAwesome.Icon.faw_bar_chart),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_check_day).withIcon(FontAwesome.Icon.faw_search),
                        new SectionDrawerItem().withName(R.string.drawer_item_utility_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_promotion).withIcon(FontAwesome.Icon.faw_cart_plus),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_cash).withIcon(FontAwesome.Icon.faw_money),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_callback).withIcon(FontAwesome.Icon.faw_phone),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_info_owner).withIcon(FontAwesome.Icon.faw_user),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_utility_contacts).withIcon(FontAwesome.Icon.faw_taxi)
                ) // add the items we want to use with our Drawer
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_item_setting).withIcon(FontAwesome.Icon.faw_cog),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about_us).withIcon(FontAwesome.Icon.faw_info_circle)
                )
                .withSavedInstance(savedInstanceState)
                .build();


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

}
