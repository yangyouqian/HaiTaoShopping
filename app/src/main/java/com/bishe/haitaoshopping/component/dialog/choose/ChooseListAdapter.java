package com.bishe.haitaoshopping.component.dialog.choose;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bishe.haitaoshopping.R;

import java.util.List;

/**
 * Created by yhviews on 2019/3/11.
 */

public class ChooseListAdapter extends BaseAdapter {
    private Context context;
    private List<String> info;

    public ChooseListAdapter(Context context, List<String> info) {
        this.context = context;
        this.info = info;
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int position) {
        return info.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_list_layout, null);
        }
        String name = info.get(position);
        TextView textView = convertView.findViewById(R.id.tv_item);
        View divider = convertView.findViewById(R.id.divider_item);
        if (position == info.size() - 1) {
            divider.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
        }
        textView.setText(name);
        return convertView;
    }
}
