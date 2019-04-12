package com.bishe.haitaoshopping;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author yanghuan
 * @date 2019/1/18
 */
public class TitleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private String [] titles;

    public TitleFragmentPagerAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
        super(fm);
        this.mFragmentList = mFragmentList;
    }

    /**
     * 描述：获取索引位置的Fragment.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position < mFragmentList.size()){
            fragment = mFragmentList.get(position);
        }else{
            fragment = mFragmentList.get(0);

        }
        return fragment;
    }

    /**
     * 描述：获取数量.
     * @return
     */
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
