package com.bishe.haitaoshopping;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
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
            return currentUser.getUsername();
        }
        return "";
    }

}
