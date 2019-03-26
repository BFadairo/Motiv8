package com.onramp.android.takehome.view;

import com.onramp.android.takehome.view.Fragments.TaskFragment;
import com.onramp.android.takehome.view.Fragments.TimerFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPageAdapter extends FragmentPagerAdapter {

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0: return TimerFragment.newInstance();
            case 1: return TaskFragment.newInstance(1);
            default: return TimerFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }
}
