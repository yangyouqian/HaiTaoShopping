package com.bishe.haitaoshopping.model;

import java.util.List;

/**
 * Created by yhviews on 2019/3/11.
 */

public class ShopInfoDialogModel {
    public String title;//选择品牌,网站,类型
    public List<String> info;
    public String dbName;//数据库名称

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }
}
