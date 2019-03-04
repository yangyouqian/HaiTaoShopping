package com.bishe.haitaoshopping.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.component.banner.BannerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * @author yanghuan
 * @date 2019/2/25
 */
public class HomeFragment extends Fragment {

    private BannerView mBannerView;
    private TextView mTvSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mTvSearch = view.findViewById(R.id.tv_search);
        mBannerView = view.findViewById(R.id.banner_view);

        initData();
        return view;
    }

    private void initData() {
        ArrayList<String> images = new ArrayList<>();
        images.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3778456200,3076998411&fm=23&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3535338527,4000198595&fm=23&gp=0.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1017904219,2460650030&fm=23&gp=0.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2863927798,667335035&fm=23&gp=0.jpg");
        setBean(images);
    }

    private void setBean(final ArrayList<String> beans) {
        mBannerView.setPages(new BannerView.ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, final View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans);
        mBannerView.startTurning(5000);
    }
}
