package com.bishe.haitaoshopping.home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
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

import java.io.FileNotFoundException;
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
    private EditText etNote;
    private ProgressBar progressBar;
    private TextView btnConfirmCreate;
    private RecyclerView addImgRecyclerView;
    private LinearLayout addPriceContainer;
    private TextView addPriceBtn;
    private RelativeLayout addPriceTitle;
    private View addPriceLine;
    private TimePicker timePicker;
    private CheckBox ckAutoEnd;
    private TextView tv_add_img;

    private ShopInfoDialogModel model;
    private ChooseDialog mDialog;
    private int mClickId;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<String> imgUrlList = new ArrayList<>();
    private boolean isAdding;//正在输入价格
    private View currentItemView;
    private List<String> priceIds;
    private Shop mShop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        Utils.setMStatusStyle(this);
        init();
        View decorView = getWindow().getDecorView();
        View contentView = findViewById(Window.ID_ANDROID_CONTENT);
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(getGlobalLayoutListener(decorView, contentView));

        if (getIntent() != null) {
            mShop = getIntent().getParcelableExtra("shop");
            if (mShop != null) {
                updateData(mShop);
            }
        }
    }

    private void updateData(Shop shop) {
        etBrand.setText(shop.getBrand());
        etExpress.setText(shop.getExpress());
        etType.setText(shop.getType());
        etWebSite.setText(shop.getWebSite());
        etExpress.setText(shop.getExpress());
        etDiscount.setText(shop.getDiscount());
        etTitle.setText(shop.getTitle());
        etSubtitle.setText(shop.getSubTitle());
        addPriceBtn.setVisibility(View.GONE);
        addImgRecyclerView.setVisibility(View.GONE);
        tv_add_img.setVisibility(View.GONE);
        btnConfirmCreate.setText("确认修改");
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
        addPriceContainer = findViewById(R.id.add_price_container);
        addPriceBtn = findViewById(R.id.add_price_btn);
        addPriceTitle = findViewById(R.id.add_price_title);
        addPriceLine = findViewById(R.id.add_price_divider);
        ckAutoEnd = findViewById(R.id.ck_auto_end);
        timePicker = findViewById(R.id.timePicker);
        etNote = findViewById(R.id.et_note);
        tv_add_img = findViewById(R.id.tv_add_img);
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
        addPriceBtn.setOnClickListener(this);

        model = new ShopInfoDialogModel();
        priceIds = new ArrayList<>();
        initRecyclerView();
        //TODO 没时间了 先不搞这个
        timePicker.setIs24HourView(true);
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
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
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
                        selected = selected + (i == 0 ? "" : ",") + selectedInfo.get(i);
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
                if (mShop == null) {
                    if (checkInput(false)) {
                        addShopInfoToDB();
                    }
                } else {
                    if (checkInput(true)) {
                        saveShopInBackground(true);
                    }
                }
                break;
            case R.id.add_price_btn:
                addPrice();
                break;
        }
    }

    private void addPrice() {
        if (isAdding) {
            EditText etShopName = currentItemView.findViewById(R.id.et_shop_name);
            EditText etShopPrice = currentItemView.findViewById(R.id.et_shop_price);
            EditText etShopNum = currentItemView.findViewById(R.id.et_shop_num);
            String shopName = etShopName.getText().toString();
            String shopPrice = etShopPrice.getText().toString();
            String shopNum = etShopNum.getText().toString();
            if (!TextUtils.isEmpty(shopName) && !TextUtils.isEmpty(shopPrice)) {
                final AVObject object = new AVObject(Constant.DB_SHOP_PRICE);
                object.put("name", shopName);
                object.put("price", shopPrice);
                object.put("num", TextUtils.isEmpty(shopNum) ? "无限制" : shopNum);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            priceIds.add(object.getObjectId());
                        }
                    }
                });
                isAdding = false;
                addPriceBtn.setText(R.string.add_price);
                etShopName.setEnabled(false);
                etShopPrice.setEnabled(false);
                etShopNum.setEnabled(false);
            } else {
                Utils.showToast(this, "请输入商品名称和价格~");
            }
        } else {
            addPriceLine.setVisibility(View.VISIBLE);
            addPriceTitle.setVisibility(View.VISIBLE);
            currentItemView = LayoutInflater.from(this).inflate(R.layout.item_create_shop_add_price, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dip2pxInt(this, 35));
            addPriceContainer.addView(currentItemView, layoutParams);
            addPriceBtn.setText(R.string.confirm_add_price);
            isAdding = true;
        }
    }

    private void addShopInfoToDB() {
        //先上传图片
        if (selectList.size() > 0) {
            progressBar.setVisibility(View.VISIBLE);
            imgUrlList.clear();
            for (int i = 0; i < selectList.size(); i++) {
                try {
                    String path = selectList.get(i).getPath();
                    String[] arr = path.split("/");
                    final AVFile imgFile = AVFile.withAbsoluteLocalPath(arr[arr.length - 1], selectList.get(i).getPath());
                    final int finalI = i;
                    imgFile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                imgUrlList.add(imgFile.getUrl());
                            }
                            //最后一张图片上传完成,添加商品到数据库中
                            if (finalI == selectList.size() - 1) {
                                saveShopInBackground(false);
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            saveShopInBackground(false);
        }
    }

    private void saveShopInBackground(final boolean update) {
        Shop shop = getShop(update);
        mShop = shop;
        shop.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    Intent intent = new Intent();
                    if (!update) {
                        intent.putExtra("save", true);
                        setResult(RESULT_OK, intent);
                    } else {
                        intent.putExtra("shop", mShop);
                        setResult(RESULT_OK, intent);
                    }
                    Utils.showToast(CreateShopActivity.this, update ? "修改成功" : "添加成功");
                    finish();
                }
            }
        });
    }

    private boolean checkInput(boolean update) {
        String brand = etBrand.getText().toString();
        String website = etWebSite.getText().toString();
        String type = etType.getText().toString();
        String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(brand)) {
            Utils.showToast(this, "请填写品牌信息");
            return false;
        }
        if (TextUtils.isEmpty(website)) {
            Utils.showToast(this, "请填写网站信息(在哪个网站下单)");
            return false;
        }
        if (TextUtils.isEmpty(type)) {
            Utils.showToast(this, "请填写类型信息");
            return false;
        }
        if (TextUtils.isEmpty(title)) {
            Utils.showToast(this, "请填写标题信息");
            return false;
        }
        if (!update && !Utils.isCollectionHasData(priceIds)) {
            Utils.showToast(this, "请添加价格信息");
            return false;
        }
        return true;
    }

    private Shop getShop(boolean update) {
        if (mShop == null) {
            mShop = new Shop();
        }
        String brand = etBrand.getText().toString();
        String website = etWebSite.getText().toString();
        String type = etType.getText().toString();
        String title = etTitle.getText().toString();
        String subtitle = etSubtitle.getText().toString();
        String express = etExpress.getText().toString();
        String discount = etDiscount.getText().toString();
        mShop.setBrand(brand);
        mShop.setWebsite(website);
        mShop.setType(type);
        mShop.setExpress(express);
        mShop.setDiscount(discount);
        mShop.setTitle(title);
        mShop.setSubTitle(subtitle);
        if (!update) {
            mShop.setUserId(Utils.getUserId());
            mShop.setUserName(Utils.getUserName());
            mShop.setImageUrlList(imgUrlList);
            mShop.setShopPriceList(priceIds);
        }
        return mShop;
    }
}
