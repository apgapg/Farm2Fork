package com.farm.farm2fork;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.farm.farm2fork.Fragment.AboutFragment;
import com.farm.farm2fork.Fragment.AddFarmFragment;
import com.farm.farm2fork.Fragment.CommunityFragment;
import com.farm.farm2fork.Fragment.ContactUsFragment;
import com.farm.farm2fork.Fragment.FarmFragment;
import com.farm.farm2fork.Fragment.ProfileFragment;
import com.farm.farm2fork.Interface.LocationSetListener;
import com.farm.farm2fork.Interface.Weatherlistener;
import com.farm.farm2fork.Models.LocationInfoModel;
import com.schibstedspain.leku.LocationPickerActivity;

import zh.wang.android.yweathergetter4a.WeatherInfo;
import zh.wang.android.yweathergetter4a.YahooWeather;
import zh.wang.android.yweathergetter4a.YahooWeatherInfoListener;

public class MainNavScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, YahooWeatherInfoListener {

    private static final String TAG = MainNavScreen.class.getName();
    private RecyclerView recyclerView;
    private boolean isOtherScreen = false;
    private Weatherlistener onWeatherDataReceivedListener;
    private LocationSetListener locationSetByUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // weatherlistener= (Weatherlistener) this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Welcome Ayush");

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = FarmFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl, fragment).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isOtherScreen) {
                showMainScreen();
                isOtherScreen = false;
            } else
                super.onBackPressed();
        }
    }

    public void showMainScreen() {
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = FarmFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl, fragment).commit();
        getSupportActionBar().setTitle("Welcome");
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
            getSupportActionBar().setTitle("Profile");


        } else if (id == R.id.nav_contact_us) {
            fragmentClass = ContactUsFragment.class;
            getSupportActionBar().setTitle("Contact Us");


        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
            getSupportActionBar().setTitle("About");


        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl, fragment).commit();
            isOtherScreen = true;
        } catch (Exception e) {
            e.printStackTrace();
            //throw new NullPointerException("Fragment is null");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void onAddFarmButtonClick() {
        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = AddFarmFragment.class;
        getSupportActionBar().setTitle("Add Farm");

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            throw new NullPointerException("Fragment is null");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fl, fragment).commit();

        isOtherScreen = true;

    }

    public void onCommunityButtonClick() {

        getSupportActionBar().setTitle("Community");

        try {
            CommunityFragment communityFragment = new CommunityFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl, communityFragment).commit();
            isOtherScreen = true;
        } catch (Exception e) {
            throw new NullPointerException("Fragment is null");
        }


    }

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
        if (weatherInfo != null) {

            Log.d(TAG, "gotWeatherInfo: " + weatherInfo.getLocationRegion() + "  ** " + weatherInfo);
            onWeatherDataReceivedListener.onWeatherDataReceived(weatherInfo);


        } else Log.d(TAG, "gotWeatherInfo: error:  " + errorType);
    }

    public void reqWeatherInfo(String roorkee) {

        YahooWeather mYahooWeather = YahooWeather.getInstance();
        mYahooWeather.queryYahooWeatherByLatLon(this, 30.2603, 78.4981, this);
        // mYahooWeather.queryYahooWeatherByPlaceName(this, roorkee, this);

    }

    public void setonWeatherDataReceivedListener(Weatherlistener onWeatherDataReceivedListener) {
        this.onWeatherDataReceivedListener = onWeatherDataReceivedListener;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // checkPermissionOnActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                LocationInfoModel locationInfoModel = parseLocationData(data);


                locationSetByUser.onLocationSetByUser(locationInfoModel);

            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
                Log.d(TAG, "onActivityResult: no result");

            }
        }
    }

    private LocationInfoModel parseLocationData(Intent data) {
        double latitude = data.getDoubleExtra(LocationPickerActivity.LATITUDE, 0);
        Log.d("LATITUDE****", String.valueOf(latitude));
        double longitude = data.getDoubleExtra(LocationPickerActivity.LONGITUDE, 0);
        Log.d("LONGITUDE****", String.valueOf(longitude));
        String address = data.getStringExtra(LocationPickerActivity.LOCATION_ADDRESS);
        Log.d("ADDRESS****", String.valueOf(address));
        String postalcode = data.getStringExtra(LocationPickerActivity.ZIPCODE);
        Log.d("POSTALCODE****", String.valueOf(postalcode));
        String city = data.getStringExtra(LocationPickerActivity.LOCATION_CITY);
        Log.d("CITY****", String.valueOf(city));

        LocationInfoModel locationInfoModel = new LocationInfoModel();
        locationInfoModel.setLongitude(String.valueOf(longitude));
        locationInfoModel.setLatitude(String.valueOf(latitude));
        locationInfoModel.setAddress(address);
        locationInfoModel.setPostalzipcode(postalcode);
        locationInfoModel.setCity(city);

        return locationInfoModel;
    }

    public void setonLocationSetByUser(LocationSetListener onLocationSetByUser) {
        this.locationSetByUser = onLocationSetByUser;
    }

    public void showSnackBar() {
        Snackbar.make(findViewById(R.id.cl), "Start by Adding your Farm", 5000).show();
    }
}
