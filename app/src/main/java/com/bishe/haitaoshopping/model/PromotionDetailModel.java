package com.bishe.haitaoshopping.model;

import org.json.JSONObject;

/**
 * Created by yhviews on 2019/4/12.
 */

public class PromotionDetailModel extends BaseModel {
    public String topImgUrl;
    public String title;
    public String subTitle;
    public String htmlText;
    public String mallName;//来源
    public String date;//已格式化的时间

    @Override
    protected BaseModel parse(JSONObject obj) {
        PromotionDetailModel model = new PromotionDetailModel();
        model.topImgUrl = obj.optString("smeta");
        model.title = obj.optString("post_title");
        model.subTitle = obj.optString("discount");
        model.htmlText = obj.optString("quote");
        model.mallName = obj.optString("mall_name");
        model.date = obj.optString("post_date");
        return model;
    }
}
