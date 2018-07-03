package com.farm.farm2fork.ui.map;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.farm.farm2fork.R;
import com.farm.farm2fork.Utils.AddressHelper;
import com.farm.farm2fork.Utils.Constants;
import com.farm.farm2fork.Utils.KeyboardUtils;
import com.farm.farm2fork.models.LocationInfoModel;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private static final String TAG = MapsActivity.class.getName();
    private static final LatLng INDIA_LOWER_LEFT = new LatLng(5.445640, 67.487799);
    private static final LatLng INDIA_UPPER_RIGHT = new LatLng(37.691225, 90.413055);
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            INDIA_LOWER_LEFT, INDIA_UPPER_RIGHT);
    private static final float ZOOM_LEVEL = 16;
    private static final String LOCATION = "location";
    private GoogleMap mMap;
    private AutoCompleteTextView mAutocompleteView;
    private PlaceAutocompleteAdapter mAdapter;
    private GeoDataClient mGeoDataClient;
    private TextView text_address;
    private Subscription addressSubscription;
    private View fabDone;
    private Intent addressIntentData;
    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);
                getCompleteAddress(place.getLatLng());

                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .draggable(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        place.getLatLng(), ZOOM_LEVEL));


                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete.", e);
                return;
            }
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Construct a GeoDataClient for the Google Places API for Android.
        mGeoDataClient = Places.getGeoDataClient(this, null);


        setContentView(R.layout.activity_maps);


        fabDone = findViewById(R.id.fab_done);
        text_address = findViewById(R.id.text_address);
        mAutocompleteView = findViewById(R.id.autocomplete_places);
        mAutocompleteView.setOnItemClickListener(this);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("IN")
                .build();
        // Set up the adapter that will retrieve suggestions from the Places Geo Data Client.
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_GREATER_SYDNEY, typeFilter);
        mAutocompleteView.setAdapter(mAdapter);

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressIntentData != null) {
                    setResult(RESULT_OK, addressIntentData);
                    finish();
                }
            }
        });
      /*  // Set up the 'clear text' button that clears the text in the autocomplete view
        Button clearButton = (Button) findViewById(R.id.button_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteView.setText("");
            }
        });*/


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                getCompleteAddress(latLng);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL));
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng delhi = new LatLng(28.7041, 77.1025);
        mMap.addMarker(new MarkerOptions().position(delhi).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(delhi, 12));
        getCompleteAddress(delhi);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final AutocompletePrediction item = mAdapter.getItem(position);
        final String placeId = item.getPlaceId();
        final CharSequence primaryText = item.getPrimaryText(null);

        Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
        Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
        placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);

        KeyboardUtils.hideKeyboard(this);

        Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
    }


    private void getCompleteAddress(LatLng latLng) {
        Observable<Address> addressObservable = Observable.fromCallable(new Callable<Address>() {
            @Override
            public Address call() {
                return getCompleteAddressInString(latLng);
            }
        });

        addressSubscription = addressObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Address>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Address address) {
                        if (address != null)
                            onAddressFetch(address);
                    }
                });
    }

    private void onAddressFetch(Address address) {
        String strAddress = AddressHelper.getAddressInString(address);
        text_address.setText(strAddress);

        LocationInfoModel locationInfoModel = createLocationModel(address);
        addressIntentData = new Intent();
        addressIntentData.putExtra(Constants.LOCATION, locationInfoModel);
    }


    private LocationInfoModel createLocationModel(Address address) {
        return new LocationInfoModel(address);
    }

    private Address getCompleteAddressInString(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address returnedAddress = addresses.get(0);


            return returnedAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected void onDestroy() {
        if (addressSubscription != null && !addressSubscription.isUnsubscribed()) {
            addressSubscription.unsubscribe();
        }

        super.onDestroy();
    }
}
