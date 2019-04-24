package com.bishe.haitaoshopping.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.chatkit.LCChatKit;
import com.bishe.haitaoshopping.chatkit.activity.LCIMConversationActivity;
import com.bishe.haitaoshopping.chatkit.utils.LCIMConstants;
import com.bishe.haitaoshopping.component.banner.BannerView;
import com.bishe.haitaoshopping.component.banner.ViewCreator;
import com.bishe.haitaoshopping.component.titlebar.TitleBar;
import com.bishe.haitaoshopping.event.RefreshHomeShopListEvent;
import com.bishe.haitaoshopping.model.Shop;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


public class ShopDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvSubTitle;
    public TextView tvCreateUserName;
    public TextView tvCreateTime;
    private ImageView ivAvatar;
    private LinearLayout tagContainer;
    private TextView tvLikeNum;
    private TitleBar titleBar;
    private TextView tvDiscountTitle;
    private TextView tvDiscountContent;
    private TextView tvJoinChat;
    private TextView tvJoinShop;
    private BannerView bannerView;
    private LinearLayout showPriceContainer;
    private PopupWindow popupWindow;

    private Shop mShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        Utils.setMStatusStyle(this);
        Intent intent = getIntent();
        if (intent != null) {
            mShop = intent.getParcelableExtra("shop");
        }
        initView();
        initData();
    }

    private void initView() {
        tvTitle = findViewById(R.id.shop_detail_title);
        tvSubTitle = findViewById(R.id.shop_detail_subtitle);
        tvCreateTime = findViewById(R.id.shop_detail_create_time);
        tvCreateUserName = findViewById(R.id.shop_detail_user_name);
        ivAvatar = findViewById(R.id.shop_detail_user_avatar);
        tagContainer = findViewById(R.id.shop_detail_tags);
        tvLikeNum = findViewById(R.id.shop_detail_like_num);
        titleBar = findViewById(R.id.shop_detail_title_bar);
        bannerView = findViewById(R.id.shop_detail_banner_view);
        showPriceContainer = findViewById(R.id.shop_detail_show_price_container);
        tvDiscountContent = findViewById(R.id.shop_detail_discount_content);
        tvDiscountTitle = findViewById(R.id.shop_detail_discount_title);
        tvJoinChat = findViewById(R.id.shop_detail_join_chat);
        tvJoinShop = findViewById(R.id.shop_detail_join_shop);
    }

    private void initData() {
        if (mShop == null) {
            return;
        }
        tvTitle.setText(mShop.getTitle());
        tvSubTitle.setText(mShop.getSubTitle());
        tvCreateUserName.setText(mShop.getUserName());
        tvCreateTime.setText(mShop.getCreateTime());
        tvLikeNum.setText(mShop.getLikeNum());
        titleBar.setTitle("拼单信息详情");
        if (!TextUtils.isEmpty(mShop.getDiscount())) {
            tvDiscountContent.setVisibility(View.VISIBLE);
            tvDiscountTitle.setVisibility(View.VISIBLE);
            tvDiscountTitle.setText(mShop.getDiscount());
        } else {
            tvDiscountTitle.setVisibility(View.GONE);
            tvDiscountContent.setVisibility(View.GONE);
        }
        titleBar.setOnBackClickListener(new TitleBar.OnBackClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        if (mShop.getUserId().equals(Utils.getUserId())) {
            titleBar.showMoreIcon();
            titleBar.setMoreClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupWindow(titleBar.getMoreIcon());
                }
            });
        }
        tvJoinChat.setOnClickListener(this);
        tvJoinShop.setOnClickListener(this);
        tagContainer.addView(buildTextView("#" + mShop.getBrand()));
        tagContainer.addView(buildTextView("#" + mShop.getType()));
        tagContainer.addView(buildTextView("#" + mShop.getWebSite()));
        setBean(mShop.getImageUrlList());
        showPrice();
    }

    private void showPopupWindow(View imageButton) {
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View ppw = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView tvDelete = ppw.findViewById(R.id.tv_delete);
        TextView tvUpdate = ppw.findViewById(R.id.tv_update);
        TextView tvEnd = ppw.findViewById(R.id.tv_set_end);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                deleteShop();
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(ppw);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(imageButton);
    }

    private void deleteShop() {
        //TODO 添加删除确认弹窗
        mShop.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                EventBus.getDefault().post(new RefreshHomeShopListEvent());
                finish();
            }
        });
    }


    private void showPrice() {
        List<String> priceList = mShop.getShopPriceList();
        if (Utils.isCollectionHasData(priceList)) {
            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dip2pxInt(this, 35));
            for (String priceId : priceList) {
                AVQuery<AVObject> avQuery = new AVQuery<>("ShopPrice");
                avQuery.getInBackground(priceId, new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (e == null) {
                            String name = avObject.getString("name");
                            String num = avObject.getString("num");
                            String price = avObject.getString("price");
                            showPriceContainer.addView(buildPriceItemView(name, num, price), layoutParams);
                        }
                    }
                });
            }
        }
    }

    private View buildPriceItemView(String name, String num, String price) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_shop_detail_show_price, null);
        TextView tvShopName = view.findViewById(R.id.tv_shop_name);
        tvShopName.setText(name);
        TextView tvShopNum = view.findViewById(R.id.tv_shop_num);
        tvShopNum.setText(num);
        TextView tvShopPrice = view.findViewById(R.id.tv_shop_price);
        tvShopPrice.setText(price);
        return view;
    }

    private TextView buildTextView(String text) {
        TextView textView = new TextView(this);
        textView.setBackgroundResource(R.drawable.tv_shop_detail_tag_bg);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dip2pxInt(this, 23));
        layoutParams.setMargins(0, 0, 20, 0);
        textView.setPadding(Utils.dip2pxInt(this, 5), 0, Utils.dip2pxInt(this, 5), 0);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(this.getResources().getColor(R.color.color_d43030));
        textView.setTag(text);
        return textView;
    }

    private void setBean(final List<String> beans) {
        if (Utils.isCollectionHasData(beans)) {
            bannerView.setPages(new ViewCreator<String>() {
                @Override
                public View createView(Context context, int position) {
                    ImageView imageView = new ImageView(context);
//                imageView.setScaleType(ImageView.ScaleType.CENTER);
                    return imageView;
                }

                @Override
                public void updateUI(Context context, final View view, int position, String entity) {
                    AVFile file = new AVFile("img.jpg", entity, new HashMap<String, Object>());
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            if (e == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                ((ImageView) view).setImageBitmap(bitmap);
                            }
                        }
                    });
                }
            }, beans);
        } else {
            bannerView.setVisibility(View.GONE);
        }
    }

    private void jumpToConversationActivity() {
        Intent intent = new Intent(ShopDetailActivity.this, LCIMConversationActivity.class);
        if (TextUtils.isEmpty(mShop.getConversationId())) {
            intent.putExtra(LCIMConstants.PEER_ID, mShop.getUserName());
        } else {
            intent.putExtra(LCIMConstants.CONVERSATION_ID, mShop.getConversationId());
        }
        intent.putExtra("shop", mShop);
        intent.putExtra("cov_name", "群" + mShop.getTitle());
        startActivityForResult(intent, Constant.REQUEST_CODE_CREATE_CHAT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.REQUEST_CODE_CREATE_CHAT:
                    if (data != null && TextUtils.isEmpty(mShop.getConversationId())) {
                        String covId = data.getStringExtra("cov_id");
                        mShop.setConversationId(covId);
                        mShop.saveInBackground();
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.shop_detail_join_chat) {
            if (!Utils.checkLoginState()) {
                Utils.showToast(this, "请先登录~");
                Utils.jumpToLoginActivity(this);
                return;
            }
            if (LCChatKit.getInstance().getClient() == null) {
                LCChatKit.getInstance().open(Utils.getUserName(), new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (e == null) {
                            jumpToConversationActivity();
                        }
                    }
                });
            } else {
                jumpToConversationActivity();
            }
        } else if (id == R.id.shop_detail_join_shop) {
            if (mShop.getUserId().equals(Utils.getUserId())) {
                Utils.showToast(this, "不能参与自己发起的拼单哦~");
                return;
            }
            //TODO 参与拼单前,请先完善您的个人信息~
            Intent intent = new Intent(this, JoinShopActivity.class);
            intent.putExtra("shop", mShop);
            startActivityForResult(intent, Constant.REQUEST_CODE_JOIN_SHOP);
        }
    }
}
