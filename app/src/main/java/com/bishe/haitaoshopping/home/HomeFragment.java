package com.bishe.haitaoshopping.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.banner.BannerView;
import com.bishe.haitaoshopping.component.banner.ViewCreator;
import com.bishe.haitaoshopping.component.listview.OnUpdateItemListener;
import com.bishe.haitaoshopping.component.listview.home.MyAdapter;
import com.bishe.haitaoshopping.component.listview.home.ViewHolder;
import com.bishe.haitaoshopping.model.Shop;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author yanghuan
 * @date 2019/2/25
 */
public class HomeFragment extends Fragment {

    private BannerView mBannerView;
    private TextView mTvSearch;
    private ListView shopPoolListView;
    private ProgressBar loadingBar;
    private TextView loadingText;

    private List<Shop> shopList;
    private MyAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mTvSearch = view.findViewById(R.id.tv_search);
        mBannerView = view.findViewById(R.id.banner_view);
        shopPoolListView = view.findViewById(R.id.shop_pool_list_view);
        loadingBar = view.findViewById(R.id.home_progress_bar);
        loadingText = view.findViewById(R.id.tv_loading_text);
        initBannerData();
        initListView();
        updateListViewData();
        return view;
    }

    private void initListView() {
        mAdapter = new MyAdapter(getContext());
        mAdapter.setListener(new OnUpdateItemListener() {
            @Override
            public void update(final ViewHolder viewHolder, Object item) {
                if (!(item instanceof Shop)) {
                    return;
                }
                Shop shop = (Shop) item;
                viewHolder.tvItemTitle.setText(shop.getTitle());
                viewHolder.tvItemSubTitle.setText(shop.getSubTitle());
                viewHolder.tvCreateUser.setText(shop.getUserName());
                viewHolder.tvCreateTime.setText(shop.getCreateTime());
                viewHolder.tvLikeNum.setText(shop.getLikeNum());
                if (Utils.isCollectionHasData(shop.getImageUrlList())) {
                    String thumbnailUrl = (String) shop.getImageUrlList().get(0);
                    viewHolder.ivThumbnail.setTag(thumbnailUrl);
                    AVFile file = new AVFile("thumbnail.jpg", thumbnailUrl, new HashMap<String, Object>());
                    file.getThumbnailUrl(true, 80, 80);
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            if (e == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                String tag = (String) viewHolder.ivThumbnail.getTag();
                                if (!TextUtils.isEmpty(tag)) {
                                    viewHolder.ivThumbnail.setImageBitmap(bitmap);
                                }
                            }
                        }
                    });
                } else {
                    viewHolder.ivThumbnail.setTag("");
                }
            }
        });
        shopPoolListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
                intent.putExtra("shop", shopList.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_CREATE_SHOP) {
            if (data != null && data.getBooleanExtra("save", false)) {
                //如果有保存数据则更新首页listview
                updateListViewData();
            }
        }
    }

    private void updateListViewData() {
        shopList = new ArrayList<>();
        loadingBar.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
        AVQuery<Shop> shopQuery = new AVQuery<>("Shop");
        shopQuery.limit(20);
        shopQuery.orderByDescending("createdAt");//根据时间降序
        shopQuery.findInBackground(new FindCallback<Shop>() {
            @Override
            public void done(List<Shop> list, AVException e) {
                loadingBar.setVisibility(View.GONE);
                loadingText.setVisibility(View.GONE);
                if (e == null) {
                    shopList.addAll(list);
                    mAdapter.update(shopList);
                    shopPoolListView.setVisibility(View.VISIBLE);
                    shopPoolListView.setAdapter(mAdapter);
                }
            }
        });
    }

    private void initBannerData() {
        ArrayList<String> images = new ArrayList<>();
        images.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3778456200,3076998411&fm=23&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3535338527,4000198595&fm=23&gp=0.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1017904219,2460650030&fm=23&gp=0.jpg");
        images.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2863927798,667335035&fm=23&gp=0.jpg");
        setBean(images);
    }

    private void setBean(final ArrayList<String> beans) {
        mBannerView.setPages(new ViewCreator<String>() {
            @Override
            public View createView(Context context, int position) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }

            @Override
            public void updateUI(Context context, final View view, int position, String entity) {
                Glide.with(context).load(entity).into((ImageView) view);
            }
        }, beans);
        mBannerView.startTurning(5000);
    }
}
