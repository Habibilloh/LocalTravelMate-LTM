package com.example.abdul.ltm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionPagePagerAdapter extends FragmentPagerAdapter {
    public SectionPagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                TouristInfoFrag touristInfoFrag = new TouristInfoFrag();
                return touristInfoFrag;

            case 1:
                 PostsFrag postsFrag = new PostsFrag();
                 return postsFrag;
            case 2:
                SelectCountryFrag selectCountryFrag = new SelectCountryFrag();
                return selectCountryFrag;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public  CharSequence getPageTitle(int positon){

        switch (positon){
            case 0:
                return "INFO";
            case 1:
                return "POSTS";
            case 2:
                return "SELECT";
            default:
                return null;
        }

    }
}
