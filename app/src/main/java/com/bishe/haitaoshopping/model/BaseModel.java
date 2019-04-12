package com.bishe.haitaoshopping.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhviews on 2019/4/11.
 */

public class BaseModel<T extends BaseModel> implements Serializable {

    public String status;
    public String msg;
    public List<T> infoList;

    public void parse(String json) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        status = jsonObject.optString("status");
        msg = jsonObject.optString("msg");
        JSONArray array = jsonObject.optJSONArray("infor");
        if (array != null) {
            infoList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                T model = parse(array.optJSONObject(i));
                if (model != null) {
                    infoList.add(model);
                }
            }
        }
    }

    protected T parse(JSONObject obj) {
        return null;
    }
}
