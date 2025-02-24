package com.sait.tawajudpremiumplusnewfeatured.ui.landingScreens;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SlidingLandingScreens_Adapter extends FragmentStatePagerAdapter {

    public SlidingLandingScreens_Adapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LandingLocationFragment();
            case 1:
                return new LandingReportsFragment();
            case 2:
                return new LandingCalenderFragment();
            default:
                return new LandingAttendanceFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
