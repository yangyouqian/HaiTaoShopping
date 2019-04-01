package com.bishe.haitaoshopping.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


public abstract class BasicBottomDialog implements View.OnClickListener {

    protected Context mContext;

    private Dialog mDialog;

    private boolean isCannotTouchClose;

    public BasicBottomDialog(Context context) {
        mContext = context;
        init();
    }

    public BasicBottomDialog(Context mContext, boolean isCannotTouchClose) {
        this.mContext = mContext;
        this.isCannotTouchClose = isCannotTouchClose;
        init();
    }

    public void show(Object o) {
        if (mDialog == null) {
            return;
        }
        initData(o);
        mDialog.show();
    }

    public void show(String title, String subTitle, String confirmButtonText, String cancelButtonText) {
        if (mDialog == null) return;
        setupUI(title, subTitle, confirmButtonText, cancelButtonText);
        mDialog.show();
    }

    public void show(String title, String subTitle, String confirmButtonText) {
        if (mDialog == null) return;
        setupUI(title, subTitle, confirmButtonText);
        mDialog.show();
    }

    public void showLoadingDialog(boolean isLoadingState) {
        if (mDialog == null) {
            return;
        }
        if (isLoadingState) {
            mDialog.show();
        }
        loadingData(isLoadingState);
    }

    protected void loadingData(boolean firstLoading) {
    }

    protected void init() {
        final View rootView = initViews();
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(1);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        mDialog.getWindow().getAttributes().windowAnimations = R.style.common_popup_anim_style;
        mDialog.setContentView(rootView);
        if (!isCannotTouchClose) {
            rootView.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    if (rootView == null) {
                        return false;
                    }
                    View firstChild = null;
                    if (rootView instanceof ViewGroup) {
                        firstChild = ((ViewGroup) rootView).getChildAt(0);
                    }
                    if (firstChild == null) {
                        return false;
                    }
                    int height = firstChild.getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            hide();
                        }
                    }
                    return true;
                }
            });
        } else {
            mDialog.setCanceledOnTouchOutside(false);
        }

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (mDialog == null) {
                    return;
                }
                Window window = mDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(0));
//                window.getAttributes().windowAnimations = R.style.common_popup_anim_style;
                DisplayMetrics dm = new DisplayMetrics();
                window.getWindowManager().getDefaultDisplay().getMetrics(dm);
                window.setLayout(dm.widthPixels, -2);
                WindowManager.LayoutParams windowParams = window.getAttributes();
                windowParams.dimAmount = 0.4F;
                windowParams.flags |= 2;
//                windowParams.gravity = 80;
                window.setAttributes(windowParams);
            }
        });

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                onDismissDialog();
            }
        });
    }

    protected void onDismissDialog() {
    }

    protected abstract View initViews();

    protected abstract void initData(Object o);

    protected void setupUI(String title, String subTitle, String confirmButtonText, String cancelButtonText) {
    }

    protected void setupUI(String title, String subTitle, String confirmButtonText) {
    }


    public void hide() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    public boolean isShown() {
        return mDialog != null && mDialog.isShowing();
    }

    @Override
    public void onClick(View v) {
    }

}