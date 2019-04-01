package com.bishe.haitaoshopping.model;

import android.text.TextUtils;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhviews on 2019/3/4.
 * 拼单信息
 */

@AVClassName("Shop")
public class Shop extends AVObject {

    public Shop() {
    }

    /**
     * 品牌
     * @param value
     */
    public void setBrand(String value) {
        put("brand", value);
    }

    public String getBrand() {
        return getString("brand");
    }

    /**
     * 类型
     * @param value
     */
    public void setType(String value) {
        put("type", value);
    }

    public String getType() {
        return getString("type");
    }

    /**
     * 网站
     * @param value
     */
    public void setWebsite(String value) {
        put("website", value);
    }

    public String getWebSite() {
        return getString("website");
    }

    /**
     * 快递信息
     * @param value
     */
    public void setExpress(String value) {
        if (TextUtils.isEmpty(value)) {
            put("express", "包国际不包国内，国内运费到货后再算");
        } else {
            put("express", value);
        }
    }

    public String getExpress() {
        return getString("express");
    }

    /**
     * 折扣信息
     * @param value
     */
    public void setDiscount(String value) {
        put("discount", value);
    }

    public String getDiscount() {
        return getString("discount");
    }

    public void setTitle(String value) {
        put("title", value);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setSubTitle(String value) {
        put("subTitle", value);
    }

    public String getSubTitle() {
        return getString("subTitle");
    }

    public String getUserId() {
        return getString("user_id");
    }

    public void setUserId(String value) {
        put("user_id", value);
    }

    public String getUserName() {
        return getString("user_name");
    }

    public void setUserName(String value) {
        put("user_name", value);
    }

    public void setImageUrlList(List<String> imageUrlList) {
        put("img_list", imageUrlList);
    }

    public List getImageUrlList() {
        return getList("img_list");
    }
}
