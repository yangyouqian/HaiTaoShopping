package com.bishe.haitaoshopping.component.dialog.choose;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.dialog.BasicBottomDialog;
import com.bishe.haitaoshopping.model.ShopInfoDialogModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhviews on 2019/3/7.
 * 选择品牌,类型,网站的弹窗
 */

public class ChooseDialog extends BasicBottomDialog implements AdapterView.OnItemClickListener {

    private TextView tvTitle;
    private LinearLayout llSelectedContainer;
    private TextView tvAdd;
    private ListView listView;
    private TextView tvConfirm;
    private LinearLayout llAdd;
    private TextView tvHint;
    private EditText etInfo;
    private Button btnAddInfo;

    private List<String> mListInfo;

    private int mSelectedCount;
    private List<String> mSelectedInfo = new ArrayList<>();

    private OnConfirmListener onConfirmListener;

    private String mDbName;

    public ChooseDialog(Context context) {
        super(context);
    }

    @Override
    protected View initViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_layout, null);
        tvTitle = view.findViewById(R.id.tv_choose_dialog_title);
        llSelectedContainer = view.findViewById(R.id.ll_selected_container);
        tvAdd = view.findViewById(R.id.tv_add);
        listView = view.findViewById(R.id.list_view);
        tvConfirm = view.findViewById(R.id.tv_confirm_add);
        llAdd = view.findViewById(R.id.ll_add);
        tvHint = view.findViewById(R.id.tv_hint);
        etInfo = view.findViewById(R.id.et_info);
        btnAddInfo = view.findViewById(R.id.btn_add_info);
        tvConfirm.setOnClickListener(this);
        btnAddInfo.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData(Object o) {
        if (o instanceof ShopInfoDialogModel) {
            ShopInfoDialogModel model = (ShopInfoDialogModel) o;
            tvTitle.setText(model.title);
            mDbName = model.getDbName();
            if (model.info != null && model.info.size() > 0) {
                mListInfo = model.info;
                ChooseListAdapter adapter = new ChooseListAdapter(mContext, model.info);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(this);
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        addItemToSelectedContainer(mListInfo.get(position));
    }

    private void addItemToSelectedContainer(String text) {
        if (mSelectedCount > 3) {
            Utils.showToast(mContext, "最多添加4个~");
            return;
        }
        if (mSelectedInfo.contains(text)) {
            Utils.showToast(mContext, "不能重复添加~");
            return;
        }
        TextView textView = buildTextView(text);
        mSelectedCount++;
        mSelectedInfo.add(text);
        llSelectedContainer.addView(textView);
    }

    private TextView buildTextView(String text) {
        TextView textView = new TextView(mContext);
        textView.setBackgroundResource(R.drawable.tv_selected_bg);
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.dip2pxInt(mContext, 23));
        layoutParams.setMargins(0, 0, 15, 0);
        textView.setPadding(Utils.dip2pxInt(mContext, 5), 0, Utils.dip2pxInt(mContext, 5), 0);
        textView.setLayoutParams(layoutParams);
        textView.setTextColor(mContext.getResources().getColor(R.color.text_normal));
        textView.setOnClickListener(this);
        textView.setTag(text);
        return textView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_confirm_add) {
            if (onConfirmListener != null) {
                onConfirmListener.onConfirm(mSelectedInfo);
            }
        } else if (v.getId() == R.id.btn_add_info) {
            String inputInfo = etInfo.getText().toString();
            if (!TextUtils.isEmpty(inputInfo)) {
                String[] temp = inputInfo.split(",");
                for (String aTemp : temp) {
                    addItemToSelectedContainer(aTemp);
                    //添加到数据库中
                    AVObject shopInfo = new AVObject(mDbName);
                    shopInfo.put("name", aTemp);
                    shopInfo.put("pinyin", Utils.getFirstSpell(aTemp));
                    shopInfo.saveInBackground();
                }
            }
        } else if (v.getId() == R.id.tv_add) {
            llAdd.setVisibility(View.VISIBLE);
            tvHint.setVisibility(View.VISIBLE);
        } else {
            llSelectedContainer.removeView(v);
            mSelectedCount--;
            mSelectedInfo.remove(v.getTag());
        }
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener {
        void onConfirm(List<String> selectedInfo);
    }
}
