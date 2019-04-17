package com.bishe.haitaoshopping;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.bishe.haitaoshopping.personal.LoginActivity;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by yhviews on 2019/3/4.
 */

public class Utils {

    private static Float sDensity = null;

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    public static void setMStatusStyle(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    private static float getDensity(Context ctx) {
        if (sDensity == null) {
            sDensity = Float.valueOf(ctx.getResources().getDisplayMetrics().density);
        }

        return sDensity.floatValue();
    }

    public static int dip2pxInt(Context ctx, float dip) {
        return (int) ((double) (dip * getDensity(ctx)) + 0.5D);
    }

    //文字转拼音,字母转大小写
    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(String.valueOf(arr[i]).toUpperCase());
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    public static boolean checkLoginState() {
        AVUser currentUser = AVUser.getCurrentUser();
        return currentUser != null;
    }

    public static void jumpToLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static String getUserId() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getObjectId();
        }
        return "";
    }

    public static String getUserName() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            //获取用户昵称
            return (String) currentUser.get("name");
        }
        return "";
    }

    public static boolean isCollectionHasData(Collection collection) {
        return collection != null && collection.size() > 0;
    }

    /**
     * 返回
     * @param date
     * @return
     */
    public static String formatDate(Date date) {

        return "";
    }

    public static void doTabStateChangeAnimation(float scale1, float scale2, final TextView tv) {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(scale1, scale2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) valueAnimator.getAnimatedValue();
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, scale);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(180);
        valueAnimator.start();
    }

    public static void setBottomTabNormalStyle(Context context, TextView tv) {
        tv.setTextColor(context.getResources().getColor(R.color.tab_text_normal));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
    }

    public static void setBottomTabSelectedStyle(Context context, TextView tv) {
        tv.setTextColor(context.getResources().getColor(R.color.tab_text_selected));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
    }
}
