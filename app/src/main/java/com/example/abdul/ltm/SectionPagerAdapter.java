package com.example.abdul.ltm;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionPagerAdapter extends FragmentPagerAdapter {



    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: RequestFragment requestFragment = new RequestFragment();
            return requestFragment;
            case 1: ChatsFregment chatsFregment = new ChatsFregment();
            return chatsFregment;
            case 2: FriendsFregment friendsFregment = new FriendsFregment();
            return friendsFregment;
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0: return "REQUESTS";
            case 1: return "CHATS";
            case 2: return "FRIENDS";
            default: return null;
        }
    }
}
