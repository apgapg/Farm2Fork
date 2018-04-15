package com.farm.farm2fork.ui.community;

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
import android.widget.Toast;

import com.farm.farm2fork.ApplicationClass;
import com.farm.farm2fork.Fragment.AboutFragment;
import com.farm.farm2fork.Fragment.FeedsFragment;
import com.farm.farm2fork.Fragment.NewsFragment;
import com.farm.farm2fork.Fragment.PriceFragment;
import com.farm.farm2fork.Fragment.WeatherFragment;
import com.farm.farm2fork.Models.CurrentWeatherModel;
import com.farm.farm2fork.Models.FarmModel;
import com.farm.farm2fork.R;
import com.farm.farm2fork.ui.farmscreen.FarmActivity;
import com.google.gson.Gson;

public class CommunityFragment extends Fragment implements CommunityContract.CommunityFragmentView {
    private static final String TAG = CommunityFragment.class.getName();
    private Activity mContext;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TextView txtCurrentTemp;
    private TextView txtForecast;
    private TextView txtWind;
    private TextView txtHumidity, txtcrop, txtaddress;
    private FarmModel farmModel;
    private View view;
    private FloatingActionButton fab;
    private CommunityFragmentPresentor mCommunityFragmentPresentor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((FarmActivity) mContext).setToolbarTitle(getResources().getString(R.string.community));

        if (view != null) {
            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }
        view = inflater.inflate(R.layout.fragment_community, container, false);

        mCommunityFragmentPresentor = new CommunityFragmentPresentor(((ApplicationClass) mContext.getApplication()).getmAppDataManager());
        mCommunityFragmentPresentor.setView(this);

        getArgumentsData();

        initViews();

        mCommunityFragmentPresentor.getCurrentWeather(farmModel.getLoc_key());

        return view;
    }

    private void initViews() {
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FarmActivity) mContext).showAddFeedFragment(farmModel.getCrop(), farmModel.getLoc_city());
            }
        });
        fab.hide();

        txtCurrentTemp = view.findViewById(R.id.currenttemp);
        txtForecast = view.findViewById(R.id.forecast);
        txtHumidity = view.findViewById(R.id.humidity);
        txtWind = view.findViewById(R.id.wind);
        txtcrop = view.findViewById(R.id.crop);
        txtaddress = view.findViewById(R.id.address);
        viewPager = view.findViewById(R.id.viewPager);

        txtaddress.setText(farmModel.getLoc_address());
        txtaddress.setSelected(true);
        txtcrop.setText(farmModel.getCrop());

        viewPager.setOffscreenPageLimit(4);

        pagerAdapter = new PagerAdapter(((FarmActivity) mContext).getSupportFragmentManager());
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

    }

    private void getArgumentsData() {
        String farmModelJson = getArguments().getString("FarmModel");
        Gson gson = new Gson();
        farmModel = gson.fromJson(farmModelJson, FarmModel.class);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(TAG, "onAttach: ");
        if (context instanceof Activity)
            mContext = (Activity) context;
        else throw new IllegalArgumentException("Context should be an instance of Activity");
    }

    @Override
    public void onWeatherFetchSuccess(CurrentWeatherModel currentWeatherModel) {
        if (isAdded()) {
            txtForecast.setText(currentWeatherModel.getForecast());
            txtHumidity.setText(currentWeatherModel.getHumidity());
            txtWind.setText(currentWeatherModel.getWind().concat(" ").concat(getResources().getString(R.string.wind_unit)));
            txtCurrentTemp.setText(currentWeatherModel.getCurrentTemp());
        }
    }

    @Override
    public void onWeatherFetchFail() {
        Toast.makeText(mContext.getApplicationContext(), "Weather Fetch Fail", Toast.LENGTH_SHORT).show();
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
