package com.bishe.haitaoshopping.promotion;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.titlebar.TitleBar;
import com.bishe.haitaoshopping.model.BaseModel;
import com.bishe.haitaoshopping.model.PromotionDetailModel;
import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PromotionDetailActivity extends AppCompatActivity {

    private TitleBar titleBar;
    private ProgressBar progressBar;
    private TextView loadingText;
    private WebView webView;
    private ImageView topImg;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvMallName;
    private TextView tvCreateTime;
    private View divider;

    private int MSG_INIT = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_INIT) {
                BaseModel model = (BaseModel) msg.obj;
                initData(model.infoList.get(0));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_detail);
        Utils.setMStatusStyle(this);
        initView();
        if (getIntent() != null) {
            String id = getIntent().getStringExtra("info_id");
            getInfoData(id);
        }
    }

    private void initView() {
        titleBar = findViewById(R.id.promotion_detail_title_bar);
        progressBar = findViewById(R.id.promotion_detail_progress_bar);
        loadingText = findViewById(R.id.promotion_detail_loading_text);
        topImg = findViewById(R.id.promotion_detail_top_img);
        tvTitle = findViewById(R.id.promotion_detail_title);
        tvSubTitle = findViewById(R.id.promotion_detail_subtitle);
        tvMallName = findViewById(R.id.promotion_detail_user_name);
        tvCreateTime = findViewById(R.id.promotion_detail_create_time);
        divider = findViewById(R.id.promotion_detail_divider);
        webView = findViewById(R.id.web_view);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        titleBar.setTitle("优惠信息详情");
        titleBar.setOnBackClickListener(new TitleBar.OnBackClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void initData(Object model) {
        if (model instanceof PromotionDetailModel) {
            progressBar.setVisibility(View.GONE);
            loadingText.setVisibility(View.GONE);
            PromotionDetailModel detail = (PromotionDetailModel) model;
            tvTitle.setText(detail.title);
            tvSubTitle.setText(detail.subTitle);
            Glide.with(this).load(detail.topImgUrl).into(topImg);
            tvMallName.setText(detail.mallName);
            tvCreateTime.setText(detail.date);
            divider.setVisibility(View.VISIBLE);
            String replace = detail.htmlText.replaceAll("<img", "<img style=\\\"max-width:100%;height:auto\\\"");
            webView.loadData(replace, "text/html","UTF-8");
        }
    }

    private void getInfoData(String id) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBody = new FormBody.Builder();
        formBody.add(Constant.PARAM_PRODUCT_ID, id);
        Request request = new Request.Builder()
                .url(Constant.URL_PRODUCT_DETAIL)
                .post(formBody.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    BaseModel model = new PromotionDetailModel();
                    model.parse(response.body().string());
                    Message message = Message.obtain();
                    message.obj = model;
                    message.what = MSG_INIT;
                    handler.sendMessage(message);
                }
            }
        });
    }

}
