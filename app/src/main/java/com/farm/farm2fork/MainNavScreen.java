package com.farm.farm2fork;

import android.Manifest;
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
import android.view.View;
import android.widget.Toast;

import com.farm.farm2fork.Fragment.AboutFragment;
import com.farm.farm2fork.Fragment.AddFarmFragment;
import com.farm.farm2fork.Fragment.AddFeedFragment;
import com.farm.farm2fork.Fragment.CommunityFragment;
import com.farm.farm2fork.Fragment.ContactUsFragment;
import com.farm.farm2fork.Fragment.FarmFragment;
import com.farm.farm2fork.Fragment.ProfileFragment;
import com.farm.farm2fork.Interface.ImagePathListener;
import com.farm.farm2fork.Interface.LocationSetListener;
import com.farm.farm2fork.Interface.NetRetryListener;
import com.farm.farm2fork.Interface.Weatherlistener;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.Models.LocationInfoModel;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.schibstedspain.leku.LocationPickerActivity;

import java.util.List;

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
    private ImagePicker imagePicker;
    private ImagePathListener onImagePathListener;
    private NetRetryListener networkReqRetryListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // weatherlistener= (Weatherlistener) this;

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

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


        } else if (id == R.id.nav_contact_us) {
            fragmentClass = ContactUsFragment.class;


        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;


        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl, fragment).addToBackStack(null).commit();
            isOtherScreen = true;
        } catch (Exception e) {
            e.printStackTrace();
            //throw new NullPointerException("Fragment is null");
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        fragmentManager.beginTransaction().replace(R.id.fl, fragment).addToBackStack(null).commit();

        isOtherScreen = true;

    }

    public void onCommunityButtonClick(FarmModel farmModel) {

        getSupportActionBar().setTitle("Community");

        try {
            CommunityFragment communityFragment = new CommunityFragment();
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String json = gson.toJson(farmModel);
            bundle.putString("FarmModel", json);
            communityFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl, communityFragment).addToBackStack(null).commit();
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

    public void reqWeatherInfo(String loc_lat, String loc_long) {

        YahooWeather mYahooWeather = YahooWeather.getInstance();
        mYahooWeather.queryYahooWeatherByLatLon(this, Double.valueOf(loc_lat), Double.valueOf(loc_long), this);

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

        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                                                           @Override
                                                           public void onImagesChosen(List<ChosenImage> images) {
                                                               if (images != null)
                                                                   if (images.size() > 0) {
                                                                       onImagePathListener.onImagePath(images.get(0).getQueryUri());
                                                                   } else Log.d(TAG, "onImagesChosen: empty");
                                                               else
                                                                   Log.e(TAG, "onError: onimagechoosen: ");

                                                           }

                                                           @Override
                                                           public void onError(String message) {
                                                               // Do error handling
                                                               Log.e(TAG, "onError: " + message);
                                                           }
                                                       }

                    );
                }
                imagePicker.submit(data);
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
        Snackbar.make(findViewById(R.id.cl), "Start by Adding your Farm", 4000).show();
    }

    public void chooseImage() {
        imagePicker = new ImagePicker(this);
        imagePicker.setImagePickerCallback(new ImagePickerCallback() {
                                               @Override
                                               public void onImagesChosen(List<ChosenImage> images) {
                                                   if (images != null) {
                                                       if (images.size() > 0) {
                                                           onImagePathListener.onImagePath(images.get(0).getQueryUri());
                                                       } else Log.d(TAG, "onImagesChosen: empty");
                                                   } else
                                                       Log.e(TAG, "onError: onimagechoosen: ");

                                               }

                                               @Override
                                               public void onError(String message) {
                                                   // Do error handling
                                                   Log.e(TAG, "onError: " + message);
                                               }
                                           }

        );

        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(false);
        imagePicker.pickImage();
    }

    public void setonImagePathListener(ImagePathListener onImagePathListener) {
        this.onImagePathListener = onImagePathListener;
    }


    public void checkstoragepermissionforimage() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        chooseImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainNavScreen.this, "Please allow the permission to proceed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        token.continuePermissionRequest();
                    }

                }).check();

    }

    public void showCommunityFragment(FarmModel farmModel) {
        onCommunityButtonClick(farmModel);
    }

    public void showSnackBarNetError(final String name) {
        Snackbar.make(findViewById(R.id.cl), "Couldn't fetch Data", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkReqRetryListner.onNetReqRetry(name);
            }
        }).show();

    }

    public void setnetworkReqRetryListner(NetRetryListener networkReqRetryListner) {
        this.networkReqRetryListner = networkReqRetryListner;
    }


    public void showAddFeedFragment(String crop, String city) {

        try {
            AddFeedFragment addFeedFragment = new AddFeedFragment();
            Bundle bundle = new Bundle();
            bundle.putString("crop", crop);
            bundle.putString("city", city);
            addFeedFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fl, addFeedFragment).addToBackStack(null).commit();
            isOtherScreen = true;
        } catch (Exception e) {
            throw new NullPointerException("Fragment is null");
        }

    }

    public void setToolbarTitle(String toolbarTitle) {
        getSupportActionBar().setTitle(toolbarTitle);
    }
}
