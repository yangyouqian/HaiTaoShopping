package com.bishe.haitaoshopping.home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.dialog.choose.ChooseDialog;
import com.bishe.haitaoshopping.component.recyclerview.FullyGridLayoutManager;
import com.bishe.haitaoshopping.component.recyclerview.GridImageAdapter;
import com.bishe.haitaoshopping.component.titlebar.TitleBar;
import com.bishe.haitaoshopping.model.Shop;
import com.bishe.haitaoshopping.model.ShopInfoDialogModel;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

public class CreateShopActivity extends AppCompatActivity implements View.OnClickListener {

    private TitleBar titleBar;
    private EditText etBrand;
    private EditText etWebSite;
    private EditText etType;
    private EditText etExpress;
    private EditText etDiscount;
    private EditText etTitle;
    private EditText etSubtitle;
    private ProgressBar progressBar;
    private TextView btnConfirmCreate;
    private RecyclerView addImgRecyclerView;

    private ShopInfoDialogModel model;
    private ChooseDialog mDialog;
    private int mClickId;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        Utils.setMStatusStyle(this);
        init();
        View decorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));

    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    private void init() {
        titleBar = findViewById(R.id.create_shop_title_bar);
        etBrand = findViewById(R.id.et_brand);
        etWebSite = findViewById(R.id.et_website);
        etType = findViewById(R.id.et_type);
        etExpress = findViewById(R.id.et_express);
        etDiscount = findViewById(R.id.et_discount);
        etTitle = findViewById(R.id.et_title);
        etSubtitle = findViewById(R.id.et_sub_title);
        btnConfirmCreate = findViewById(R.id.btn_confirm_create);
        progressBar = findViewById(R.id.progress_bar);
        addImgRecyclerView = findViewById(R.id.recycler_add_img);
        titleBar.setTitle("发起拼单");
        etBrand.setFocusable(false);
        etWebSite.setFocusable(false);
        etType.setFocusable(false);
        etBrand.setOnClickListener(this);
        etWebSite.setOnClickListener(this);
        etType.setOnClickListener(this);
        titleBar.setOnBackClickListener(new TitleBar.OnBackClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        btnConfirmCreate.setOnClickListener(this);

        model = new ShopInfoDialogModel();
        initRecyclerView();
    }

    private void initRecyclerView() {
        FullyGridLayoutManager manager = new FullyGridLayoutManager(CreateShopActivity.this, 4, GridLayoutManager.VERTICAL, false);
        addImgRecyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(CreateShopActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        addImgRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(CreateShopActivity.this).themeStyle(R.style.picture_white_style).openExternalPreview(position, selectList);
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(CreateShopActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(6)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.MULTIPLE )// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .selectionMedia(selectList)// 是否传入已选图片
                        .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        mClickId = v.getId();
        final List<String> info = new ArrayList<String>();
        AVQuery<AVObject> query;
        mDialog = new ChooseDialog(this);
        mDialog.setOnConfirmListener(new ChooseDialog.OnConfirmListener() {
            @Override
            public void onConfirm(List<String> selectedInfo) {
                if (selectedInfo != null && selectedInfo.size() > 0) {
                    String selected = "";
                    for (int i = 0; i < selectedInfo.size(); i++) {
                        selected = selected + (i == 0 ? "" :",") + selectedInfo.get(i);
                    }
                    if (mClickId == R.id.et_brand) {
                        etBrand.setText(selected);
                    } else if (mClickId == R.id.et_website) {
                        etWebSite.setText(selected);
                    } else if (mClickId == R.id.et_type) {
                        etType.setText(selected);
                    }
                }
                mDialog.hide();
            }
        });
        switch (mClickId) {
            case R.id.et_brand:
                model.setTitle("选择品牌");
                model.setDbName(Constant.DB_SHOP_BRAND);
                progressBar.setVisibility(View.VISIBLE);
                query = new AVQuery<>(Constant.DB_SHOP_BRAND);
                query.orderByAscending("pinyin");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            for (AVObject avObject : list) {
                                String name = avObject.getString("name");
                                info.add(name);
                            }
                            model.setInfo(info);
                            progressBar.setVisibility(View.GONE);
                            mDialog.show(model);
                        }
                    }
                });
                break;
            case R.id.et_website:
                model.setTitle("选择网站");
                model.setDbName(Constant.DB_SHOP_WEBSITE);
                progressBar.setVisibility(View.VISIBLE);
                query = new AVQuery<>(Constant.DB_SHOP_WEBSITE);
                query.orderByAscending("pinyin");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            for (AVObject avObject : list) {
                                String name = avObject.getString("name");
                                info.add(name);
                            }
                            model.setInfo(info);
                            progressBar.setVisibility(View.GONE);
                            mDialog.show(model);
                        }
                    }
                });
                break;
            case R.id.et_type:
                model.setTitle("选择类型");
                model.setDbName(Constant.DB_SHOP_TYPE);
                progressBar.setVisibility(View.VISIBLE);
                query = new AVQuery<>(Constant.DB_SHOP_TYPE);
                query.orderByAscending("pinyin");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            for (AVObject avObject : list) {
                                String name = avObject.getString("name");
                                info.add(name);
                            }
                            model.setInfo(info);
                            progressBar.setVisibility(View.GONE);
                            mDialog.show(model);
                        }
                    }
                });
                break;
            case R.id.btn_confirm_create:
                Shop shop = getShop();
                if (shop != null) {
                    shop.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                finish();
                                Utils.showToast(CreateShopActivity.this, "添加成功");
                            }
                        }
                    });
                }
                break;
        }
    }

    private Shop getShop() {
        Shop shop = new Shop();
        String brand = etBrand.getText().toString();
        String website = etWebSite.getText().toString();
        String type = etType.getText().toString();
        String title = etTitle.getText().toString();
        String subtitle = etSubtitle.getText().toString();
        String express = etExpress.getText().toString();
        String discount = etDiscount.getText().toString();
        if (TextUtils.isEmpty(brand)) {
            Utils.showToast(this, "请填写品牌信息");
            return null;
        }
        if (TextUtils.isEmpty(website)) {
            Utils.showToast(this, "请填写网站信息(在哪个网站下单)");
            return null;
        }
        if (TextUtils.isEmpty(type)) {
            Utils.showToast(this, "请填写类型信息");
            return null;
        }
        if (TextUtils.isEmpty(title)) {
            Utils.showToast(this, "请填写标题信息");
            return null;
        }
        shop.setBrand(brand);
        shop.setWebsite(website);
        shop.setType(type);
        shop.setExpress(express);
        shop.setDiscount(discount);
        shop.setTitle(title);
        shop.setSubTitle(subtitle);
        shop.setUserId(Utils.getUserId());
        shop.setUserName(Utils.getUserName());
        return shop;
    }
}
