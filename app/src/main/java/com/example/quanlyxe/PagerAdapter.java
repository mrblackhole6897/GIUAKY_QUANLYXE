package com.example.quanlyxe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new FragmentOneV2();
                break;
            case 1:
                frag = new FragmentTwoV2();
                break;
            }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Loại xe";
                break;
            case 1:
                title = "Đơn đặt hàng";
                break;

        }
        return title;
    }
}
