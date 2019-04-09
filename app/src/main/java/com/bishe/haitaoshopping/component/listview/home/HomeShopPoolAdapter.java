package com.bishe.haitaoshopping.component.listview.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.model.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yhviews on 2019/4/8.
 */

public class HomeShopPoolAdapter extends BaseAdapter {

    private List<Shop> mShopList = new ArrayList<>();
    private Context mContext;

    public HomeShopPoolAdapter(Context context) {
        this.mContext = context;
    }

    public void update(List<Shop> shopList) {
        mShopList.clear();
        mShopList.addAll(shopList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mShopList.size();
    }

    @Override
    public Object getItem(int position) {
        return mShopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ShopPoolViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_shop_view, null);
            viewHolder = new ShopPoolViewHolder();
            viewHolder.ivThumbnail = convertView.findViewById(R.id.iv_thumbnail);
            viewHolder.tvItemTitle = convertView.findViewById(R.id.item_tv_title);
            viewHolder.tvItemSubTitle = convertView.findViewById(R.id.item_tv_subtitle);
            viewHolder.tvCreateUser = convertView.findViewById(R.id.tv_create_user);
            viewHolder.tvCreateTime = convertView.findViewById(R.id.tv_create_time);
            viewHolder.tvLikeNum = convertView.findViewById(R.id.tv_like_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ShopPoolViewHolder) convertView.getTag();
        }
        Shop shop = mShopList.get(position);
        viewHolder.tvItemTitle.setText(shop.getTitle());
        viewHolder.tvItemSubTitle.setText(shop.getSubTitle());
        viewHolder.tvCreateUser.setText(shop.getUserName());
        viewHolder.tvCreateTime.setText(shop.getCreateTime());
        viewHolder.tvLikeNum.setText(shop.getLikeNum());
        if (Utils.isCollectionEmpty(shop.getImageUrlList())) {
            String thumbnailUrl = (String) shop.getImageUrlList().get(0);
            AVFile file = new AVFile("thumbnail.jpg", thumbnailUrl, new HashMap<String, Object>());
            file.getThumbnailUrl(true, 80, 80);
            file.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, AVException e) {
                    if (e == null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        viewHolder.ivThumbnail.setImageBitmap(bitmap);
                    }
                }
            });
        }
        return convertView;
    }
}
