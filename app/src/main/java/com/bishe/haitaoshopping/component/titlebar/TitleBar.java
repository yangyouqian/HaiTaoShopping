package com.bishe.haitaoshopping.component.titlebar;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;

/**
 * Created by yhviews on 2019/3/4.
 */

public class TitleBar extends LinearLayout {

    private ImageView ivBack;
    private TextView tvTitle;
    private View placeHolderStatusBar;
    private OnBackClickListener onBackClickListener;
    private Context context;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.title_bar_layout, this);
        ivBack = view.findViewById(R.id.iv_back);
        tvTitle = view.findViewById(R.id.tv_title);
        placeHolderStatusBar = view.findViewById(R.id.place_holder_statusBar);
        initPlaceHolder();
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBackClickListener != null) {
                    onBackClickListener.onClick();
                }
            }
        });
    }

    private void initPlaceHolder() {
        placeHolderStatusBar.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) placeHolderStatusBar.getLayoutParams();
        lp.height = Utils.getStatusBarHeight(context);
        placeHolderStatusBar.setLayoutParams(lp);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setOnBackClickListener(OnBackClickListener onBackClickListener) {
        this.onBackClickListener = onBackClickListener;
    }

    public interface OnBackClickListener {
        void onClick();
    }
}
