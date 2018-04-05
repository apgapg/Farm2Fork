package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.mainfarmscreen.FarmScreen;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class CommunityFragment extends Fragment {
    private static final String TAG = CommunityFragment.class.getName();
    private Activity mContext;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TextView currenttemp;
    private TextView txtforecast;
    private TextView txtwind;
    private TextView txthumidity, txtcrop, txtaddress;
    private FarmModel farmModel;
    private View view;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FarmScreen) mContext).setToolbarTitle("Community");

        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_community, container, false);


        String farmModelJson = getArguments().getString("FarmModel");
        Gson gson = new Gson();

        farmModel = gson.fromJson(farmModelJson, FarmModel.class);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FarmScreen) mContext).showAddFeedFragment(farmModel.getCrop(), farmModel.getLoc_city());
            }
        });
        fab.hide();


        currenttemp = view.findViewById(R.id.currenttemp);
        txtforecast = view.findViewById(R.id.forecast);
        txthumidity = view.findViewById(R.id.humidity);
        txtwind = view.findViewById(R.id.wind);
        txtcrop = view.findViewById(R.id.crop);
        txtaddress = view.findViewById(R.id.address);

        txtaddress.setText(farmModel.getLoc_address());
        txtcrop.setText(farmModel.getCrop());

        txtaddress.setSelected(true);
        viewPager = view.findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(4);

        pagerAdapter = new PagerAdapter(((FarmScreen) mContext).getSupportFragmentManager());
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    fab.show();
                else fab.hide();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getWeatherData(farmModel.getLoc_key());

        return view;
    }

    private void getWeatherData(String loc_key) {
        AndroidNetworking.get("http://dataservice.accuweather.com/currentconditions/v1/" + loc_key + "?apikey=wPPGSAmAyTuYLJV3MJ8ZVnAxGQOFAwdE&language=en-IN&details=true")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (isAdded()) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(0);
                                txtforecast.setText(jsonObject.getString("WeatherText"));


                                JSONObject jsonObjectTemp = jsonObject.getJSONObject("Temperature");
                                JSONObject jsonObjecttempMetric = jsonObjectTemp.getJSONObject("Metric");
                                currenttemp.setText(String.valueOf(jsonObjecttempMetric.get("Value")) + "°c");
                                txthumidity.setText(jsonObject.getString("RelativeHumidity") + "%");

                                JSONObject jsonObjectwind = jsonObject.getJSONObject("Wind");
                                JSONObject jsonObjectspeed = jsonObjectwind.getJSONObject("Speed");
                                JSONObject jsonObjectwindmetric = jsonObjectspeed.getJSONObject("Metric");
                                txtwind.setText(String.valueOf(jsonObjectwindmetric.get("Value")) + " km/h");


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: " + anError.getResponse());
                    }
                });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(TAG, "onAttach: ");
        if (context instanceof Activity)
            mContext = (Activity) context;
        else throw new IllegalArgumentException("Context should be an instance of Activity");
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        private String[] tabs = getResources().getStringArray(R.array.tabs);

        private PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    WeatherFragment weatherFragment = new WeatherFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("loc_key", farmModel.getLoc_key());
                    weatherFragment.setArguments(bundle1);
                    return weatherFragment;

                case 1:
                    FeedsFragment feedsFragment = new FeedsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("crop", farmModel.getCrop());
                    bundle.putString("city", farmModel.getLoc_city());
                    feedsFragment.setArguments(bundle);
                    return feedsFragment;
                case 2:
                    PriceFragment priceFragment = new PriceFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("crop", farmModel.getCrop());
                    priceFragment.setArguments(bundle2);
                    return priceFragment;
                case 3:
                    return new AboutFragment();
                case 4:
                    return new NewsFragment();
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return tabs[0];
                case 1:
                    return tabs[1];
                case 2:
                    return tabs[2];
                case 3:
                    return tabs[3];
                case 4:
                    return tabs[4];
                default:
                    return super.getPageTitle(position);
            }
        }

        @Override
        public int getCount() {

            return tabs.length;
        }


    }

}
