package com.bishe.haitaoshopping.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.titlebar.TitleBar;
import com.bishe.haitaoshopping.model.Shop;
import com.bishe.haitaoshopping.model.ShopPrice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JoinShopActivity extends AppCompatActivity implements View.OnClickListener {

    private TitleBar titleBar;
    private Shop mShop;
    private LinearLayout priceContainer;
    private EditText etName;
    private EditText etNum;
    private TextView tvConfirm;
    private List<ShopPrice> chooseShopPriceList;
    private List<String> buyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_shop);
        Utils.setMStatusStyle(this);
        initView();
        initData();
    }

    private void initView() {
        titleBar = findViewById(R.id.join_shop_title_bar);
        priceContainer = findViewById(R.id.join_shop_show_price_container);
        etName = findViewById(R.id.et_name);
        etNum = findViewById(R.id.et_num);
        tvConfirm = findViewById(R.id.btn_confirm_join);
        titleBar.setTitle("参与拼单");
        tvConfirm.setOnClickListener(this);
    }

    private void initData() {
        chooseShopPriceList = new ArrayList<>();
        buyList = new ArrayList<>();
        if (getIntent() != null) {
            mShop = getIntent().getParcelableExtra("shop");
            buildPriceContainer();
        }
    }

    private void buildPriceContainer() {
        List<String> priceList = mShop.getShopPriceList();
        if (Utils.isCollectionHasData(priceList)) {
            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Utils.dip2pxInt(this, 35));
            for (String priceId : priceList) {
                AVQuery<AVObject> avQuery = new AVQuery<>("ShopPrice");
                avQuery.getInBackground(priceId, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null) {
                            String name = avObject.getString("name");
                            String num = avObject.getString("num");
                            String price = avObject.getString("price");
                            ShopPrice priceObj = new ShopPrice();
                            priceObj.setChooseNum(0);
                            priceObj.setName(name);
                            priceObj.setPrice(price);
                            priceObj.setNum(num);
                            priceContainer.addView(buildPriceItem(name, price, num, priceObj), layoutParams);
                            chooseShopPriceList.add(priceObj);
                        }
                    }
                });
            }
        }
    }

    private View buildPriceItem(final String name, String price, String num, final ShopPrice priceObj) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_join_shop_layout, null);
        ImageView ivAdd = view.findViewById(R.id.item_join_iv_add);
        ImageView ivLess = view.findViewById(R.id.item_join_iv_less);
        final TextView tvShopChooseNum = view.findViewById(R.id.item_join_tv_choose_num);
        TextView tvShopName = view.findViewById(R.id.item_join_tv_shop_name);
        TextView tvShopPrice = view.findViewById(R.id.item_join_tv_price);
        TextView tvShopNum = view.findViewById(R.id.item_join_tv_num);
        tvShopName.setText(name);
        tvShopPrice.setText(price);
        tvShopNum.setText(num);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = priceObj.getChooseNum();
                int nowCount = count + 1;
                tvShopChooseNum.setText(String.valueOf(nowCount));
                priceObj.setChooseNum(nowCount);
            }
        });
        ivLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = priceObj.getChooseNum();
                if (count == 0) {
                    Utils.showToast(JoinShopActivity.this, "不能再减了~~");
                    return;
                }
                int nowCount = count - 1;
                tvShopChooseNum.setText(String.valueOf(nowCount));
                priceObj.setChooseNum(nowCount);
            }
        });
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm_join) {
            for (ShopPrice obj : chooseShopPriceList) {
                if (obj.getChooseNum() == 0) {
                    chooseShopPriceList.remove(obj);
                }
            }
            for (int i = 0; i < chooseShopPriceList.size(); i++) {
                final ShopPrice priceObj = chooseShopPriceList.get(i);
                final int finalI = i;
                priceObj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            buyList.add(priceObj.getObjectId());
                        }
                        if (finalI == chooseShopPriceList.size() - 1) {
                            saveParticipationInBackground();
                        }
                    }
                });
            }
        }
    }

    private void saveParticipationInBackground() {
        AVObject object = new AVObject(Constant.DB_JOIN_SHOP);
        object.put("buy_list", buyList);
        object.put("status", 0);
        object.put("join_user_id", Utils.getUserId());
        object.put("create_user_id", mShop.getUserId());
        object.put("shop_id", mShop.getObjectId());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                sendMessageToShopCreateUser();
                Utils.showToast(JoinShopActivity.this, "已提交参与申请~等待发起者同意");
                finish();
            }
        });
    }

    private void sendMessageToShopCreateUser() {
        OkHttpClient client = new OkHttpClient();
        JSONObject object = new JSONObject();
        try {
            object.put(Constant.PARAM_FROM_CLIENT, Constant.VALUE_FROM_CLIENT);
            object.put(Constant.PARAM_MESSAGE, "您的拼单被人参与啦~快去【个人-发起的拼单】进行确认吧~");
            JSONArray array = new JSONArray();
            array.put(mShop.getUserName());
            object.put(Constant.PARAM_TO_CLIENT, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(Constant.JSON, object.toString());
        Request request = new Request.Builder()
                .addHeader(Constant.HEADER_PARAM_ID, Constant.HEADER_VALUE_ID)
                .addHeader(Constant.HEADER_PARAM_KEY, Constant.HEADER_VALUE_KEY_MASTER)
                .addHeader("Content-Type", "application/json")
                .url(Constant.URL_SEND_MESSAGE)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("yhviews", response.body().string());
            }
        });
    }

}
