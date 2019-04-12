package com.bishe.haitaoshopping.component.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

/**
 * Created by yhviews on 2019/2/28.
 */

public class BannerPagerAdapter<T> extends PagerAdapter {

    private Context mContext;
    private List<T> mData;
    private ViewCreator<T> mViewCreator;

    private BannerView.OnPageClickListener<T> mOnPageClickListener;
    private SparseArray<View> views = new SparseArray<>();


    public BannerPagerAdapter(Context mContext, List<T> mData, ViewCreator mViewCreator) {
        this.mContext = mContext;
        this.mData = mData;
        this.mViewCreator = mViewCreator;
    }

    public void setOnPageClickListener(BannerView.OnPageClickListener<T> mOnPageClickListener) {
        this.mOnPageClickListener = mOnPageClickListener;
    }

    //返回当前有效视图的数量,在实际数据的前后+1,实现无限循环
    @Override
    public int getCount() {
        return (mData == null || mData.isEmpty()) ? 0 : mData.size() + 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        //这里传入的position的范围是0~getCount-1
        final int item = getActualPosition(position);//拿到实际数据的索引
        View view = views.get(position);
        if (view == null) {
            view = mViewCreator.createView(mContext, position);
            views.put(position, view);
        }

        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }

        final T t = mData.get(item);//拿到实际的数据
        mViewCreator.updateUI(mContext, view, item, t);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnPageClickListener != null) {
                    mOnPageClickListener.onPageClick(item, t);
                }
            }
        });
        container.addView(view);
        return view;
    }

    private int getActualPosition(int index) {
        if (index == 0) {
            return mData.size() - 1;
        } else if (index == getCount() - 1) {
            return 0;
        }
        return index - 1;
    }
}
