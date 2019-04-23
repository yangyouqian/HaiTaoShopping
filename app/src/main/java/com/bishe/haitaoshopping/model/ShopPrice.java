package com.bishe.haitaoshopping.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by yhviews on 2019/4/22.
 */
@AVClassName("ShopPrice")
public class ShopPrice extends AVObject {
    public ShopPrice() {
    }

    //库存
    public void setNum(String value) {
        put("num", value);
    }

    public String getNum() {
        return getString("num");
    }

    public void setName(String value) {
        put("name", value);
    }

    public String getName() {
        return getString("name");
    }

    public void setChooseNum(int value) {
        put("choose_num", value);
    }

    public int getChooseNum() {
        return getInt("choose_num");
    }

    public void setPrice(String value) {
        put("price", value);
    }

    public String getPrice() {
        return getString("price");
    }
}
