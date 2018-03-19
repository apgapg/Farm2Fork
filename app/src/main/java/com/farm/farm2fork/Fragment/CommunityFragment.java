package com.farm.farm2fork.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import com.farm.farm2fork.Interface.Weatherlistener;
import com.farm.farm2fork.MainNavScreen;
import com.farm.farm2fork.R;

import zh.wang.android.yweathergetter4a.WeatherInfo;

public class CommunityFragment extends Fragment {
    private static final String TAG = CommunityFragment.class.getName();
    private Activity mContext;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TextView currenttemp;
    private TextView txtforecast;
    private TextView txtwind;
    private TextView txthumidity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community, container, false);

        currenttemp = view.findViewById(R.id.currenttemp);
        txtforecast = view.findViewById(R.id.forecast);
        txthumidity = view.findViewById(R.id.humidity);
        txtwind = view.findViewById(R.id.wind);

        viewPager = view.findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(4);

        pagerAdapter = new PagerAdapter(((MainNavScreen) mContext).getSupportFragmentManager());
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);

        ((MainNavScreen) mContext).setonWeatherDataReceivedListener(new Weatherlistener() {
            @Override
            public void onWeatherDataReceived(WeatherInfo weatherInfo) {
                Log.d(TAG, "onWeatherDataReceived: " + weatherInfo.getCurrentTemp());
                currenttemp.setText(String.valueOf(weatherInfo.getCurrentTemp()) + "°c");
                txtforecast.setText(String.valueOf(weatherInfo.getCurrentText()));
                txthumidity.setText(String.valueOf(weatherInfo.getAtmosphereHumidity()));
                txtwind.setText(String.valueOf(weatherInfo.getWindSpeed()+" mph"));

                if(pagerAdapter.getRegisteredFragment(0)!=null){
                    ((WeatherFragment) pagerAdapter.getRegisteredFragment(0)).onWeatherDataReceived(weatherInfo.getForecastInfoList());
                }

            }
        });

        ((MainNavScreen) mContext).reqWeatherInfo("Roorkee");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            mContext = (Activity) context;
        else throw new IllegalArgumentException("Context should be an instance of Activity");
    }

    @Override
    public void onDestroy() {
        mContext = null;
        super.onDestroy();

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
                    return new WeatherFragment();
                case 1:
                    return new FeedsFragment();
                case 2:
                    return new PriceFragment();
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
