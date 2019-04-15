package com.bishe.haitaoshopping.promotion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.banner.ViewCreator;
import com.bishe.haitaoshopping.component.listview.OnUpdateItemListener;
import com.bishe.haitaoshopping.component.listview.home.MyAdapter;
import com.bishe.haitaoshopping.component.listview.home.ViewHolder;
import com.bishe.haitaoshopping.model.BaseModel;
import com.bishe.haitaoshopping.model.PromotionInfo;
import com.bishe.haitaoshopping.model.UrlModel;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.bishe.haitaoshopping.Constant.DEFAULT_SELECT_TAB_INDEX;

/**
 * Created by yhviews on 2019/3/1.
 */

public class PromotionFragment extends Fragment implements ViewCreator {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
//    private ProgressBar progressBar;
//    private TextView loadingTextView;

    private int[] tabTitleRes = {R.string.tab_recommend, R.string.tab_make_up, R.string.tab_bag};
    private List<UrlModel> data;
    private int MSG_UPDATE = 0;

    private Map<Integer, BaseModel> promotionData = new HashMap<>();
    private Map<Integer, MyAdapter> adapterMap = new HashMap<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE) {
                BaseModel model = (BaseModel) msg.obj;
                if (Utils.isCollectionHasData(model.infoList)) {
                    adapterMap.get(msg.arg1).update(model.infoList);
                }
//                loadingTextView.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);
        Log.d("adapter", "onCreateView" + promotionData.size() + " " + adapterMap.size());
        mTabLayout = view.findViewById(R.id.promotion_tab);
        mViewPager = view.findViewById(R.id.promotion_view_pager);
        initTab();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapterMap.clear();
    }


    private void initTab() {
        setUrlData();
        ListPagerAdapter adapter = new ListPagerAdapter(getContext(), data, this);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(DEFAULT_SELECT_TAB_INDEX).select();//设置首页默认选中
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.tab_bottom_item_view, null);
            mTabLayout.getTabAt(i).setCustomView(view);
            TextView tv = view.findViewById(R.id.tv_bottom_name);
            tv.setText(tabTitleRes[i]);
            if (i == DEFAULT_SELECT_TAB_INDEX) {
                Utils.setBottomTabSelectedStyle(getContext(), tv);
            }
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tv = view.findViewById(R.id.tv_bottom_name);
                tv.setTextColor(getResources().getColor(R.color.tab_text_selected));
                Utils.doTabStateChangeAnimation(15, 17, tv);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView tv = view.findViewById(R.id.tv_bottom_name);
                Utils.setBottomTabNormalStyle(getContext(), tv);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    /**
     * 通过数据更新listview item的内容
     */
    private OnUpdateItemListener updateItemListener = new OnUpdateItemListener() {
        @Override
        public void update(ViewHolder viewHolder, Object data) {
            if (!(data instanceof PromotionInfo)) {
                return;
            }
            PromotionInfo info = (PromotionInfo) data;
            viewHolder.tvItemTitle.setText(info.title);
            viewHolder.tvItemSubTitle.setText(info.subtitle);
            viewHolder.tvItemSubTitle.setTextColor(getContext().getResources().getColor(R.color.color_d43030));
            viewHolder.tvCreateUser.setText(info.mallName);
            viewHolder.tvLikeNum.setText(info.likeNum);
            Date date = new Date(info.deadline);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            viewHolder.tvCreateTime.setText(sdf.format(date));
            String url = info.imgUrl;
            if (!TextUtils.isEmpty(url)) {
                Glide.with(getContext()).load(url).into(viewHolder.ivThumbnail);
            }
        }
    };

    /**
     * 设置获取优惠信息数据的url和参数
     */
    private void setUrlData() {
        data = new ArrayList<>();
        UrlModel model = new UrlModel();
        model.url = Constant.URL_HOME_PAGE_LIST;
        model.type = Constant.VALUE_RECOMMEND;
        data.add(model);
        UrlModel model2 = new UrlModel();
        model2.url = Constant.URL_PRODUCT_LIST;
        model2.type = Constant.VALUE_MAKE_UP;
        data.add(model2);
        UrlModel model3 = new UrlModel();
        model3.url = Constant.URL_PRODUCT_LIST;
        model3.type = Constant.VALUE_BAG;
        data.add(model3);
    }

    private void getInfoData(String url, String type, final int position) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(type)) {
            return;
        }
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        formBody.add(Constant.PARAM_PAGE, "1");//传递键值对参数
        formBody.add(Constant.PARAM_PAGE_RANGE, Constant.VALUE_PAGE_RANGE);
        formBody.add(Constant.PARAM_CAT_TYPE, type);
        Request request = new Request.Builder()//创建Request 对象。
                .url(url)
                .post(formBody.build())//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("adapter", "onResponse " + position);
                    BaseModel model = new PromotionInfo();
                    model.parse(response.body().string());
                    promotionData.put(position, model);
                    Message msg = Message.obtain();
                    msg.obj = model;
                    msg.arg1 = position;
                    msg.what = MSG_UPDATE;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public View createView(Context context, final int po) {
        ListView view = new ListView(context);
        view.setDivider(null);
        view.setVerticalScrollBarEnabled(false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseModel model = promotionData.get(po);
                PromotionInfo info = (PromotionInfo) model.infoList.get(position);
                Intent intent = new Intent(getActivity(), PromotionDetailActivity.class);
                intent.putExtra("info_id", info.id);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void updateUI(Context context, View view, int position, Object o) {
        MyAdapter mAdapter;
        Log.d("adapter", "updateUI " + position);
        if (adapterMap.get(position) != null) {
            mAdapter = adapterMap.get(position);
        } else {
            mAdapter = new MyAdapter(getContext());
            mAdapter.setListener(updateItemListener);
            adapterMap.put(position, mAdapter);
            ((ListView) view).setAdapter(mAdapter);
        }
        if (promotionData.get(position) != null) {
            mAdapter.update(promotionData.get(position).infoList);
        } else {
            UrlModel urlModel = (UrlModel) o;
            String url = urlModel.url;
            String type = urlModel.type;
            getInfoData(url, type, position);
        }
    }
}
