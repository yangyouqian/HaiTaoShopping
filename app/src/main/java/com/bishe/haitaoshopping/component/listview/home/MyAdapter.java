package com.bishe.haitaoshopping.component.listview.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.component.listview.OnUpdateItemListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhviews on 2019/4/8.
 */

public class MyAdapter<T> extends BaseAdapter implements Serializable{

    private List<T> mData = new ArrayList<>();
    private Context mContext;
    private OnUpdateItemListener listener;

    public MyAdapter(Context context) {
        this.mContext = context;
    }

    public void update(List<T> shopList) {
        mData.clear();
        mData.addAll(shopList);
        notifyDataSetChanged();
    }

    public void setListener(OnUpdateItemListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_home_shop_view, null);
            viewHolder = new ViewHolder();
            viewHolder.ivThumbnail = convertView.findViewById(R.id.iv_thumbnail);
            viewHolder.tvItemTitle = convertView.findViewById(R.id.item_tv_title);
            viewHolder.tvItemSubTitle = convertView.findViewById(R.id.item_tv_subtitle);
            viewHolder.tvCreateUser = convertView.findViewById(R.id.tv_create_user);
            viewHolder.tvCreateTime = convertView.findViewById(R.id.tv_create_time);
            viewHolder.tvLikeNum = convertView.findViewById(R.id.tv_like_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (listener != null) {
            listener.update(viewHolder, mData.get(position));
        }
        return convertView;
    }
}
