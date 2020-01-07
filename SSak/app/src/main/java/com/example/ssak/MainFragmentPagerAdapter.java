package com.example.ssak;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

// Customized by SY

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    int numOfTabs;
    public MainFragmentPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MainIsNotExpiredFragment tab1 = new MainIsNotExpiredFragment();
                return tab1;
            case 1:
                MainIsExpiredFragment tab2 = new MainIsExpiredFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
