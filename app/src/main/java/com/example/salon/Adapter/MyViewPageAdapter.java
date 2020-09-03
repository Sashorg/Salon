package com.example.salon.Adapter;

import com.example.salon.Fragment.BookingFragment1;
import com.example.salon.Fragment.BookingFragment2;
import com.example.salon.Fragment.BookingFragment3;
import com.example.salon.Fragment.BookingFragment4;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyViewPageAdapter extends FragmentPagerAdapter {
    public MyViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return BookingFragment1.getInstance();
            case 1:
                return BookingFragment2.getInstance();
            case 2:
                return BookingFragment3.getInstance();
            case 3:
                return BookingFragment4.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
