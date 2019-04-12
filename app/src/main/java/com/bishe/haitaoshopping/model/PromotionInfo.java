package com.bishe.haitaoshopping.model;

import org.json.JSONObject;

/**
 * Created by yhviews on 2019/4/11.
 */

public class PromotionInfo extends BaseModel{
    public String id;
    public String date;
    public String imgUrl;
    public String title;
    public String subtitle;
    public String mallName;//来源
    public String likeNum;//点赞数
    public long deadline;//截止时间时间戳

    @Override
    protected BaseModel parse(JSONObject obj) {
        if (obj.has("title")) {
            return null;
        }
        PromotionInfo info = new PromotionInfo();
        info.id = obj.optString("id");
        info.date = obj.optString("post_date");
        info.imgUrl = obj.optString("smeta");
        info.title = obj.optString("post_title");
        info.subtitle = obj.optString("discount");
        info.mallName = obj.optString("mall_name");
        info.likeNum = obj.optString("post_like");
        info.deadline = obj.optLong("deadline_time");
        return info;
    }
}
