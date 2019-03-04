package com.bishe.haitaoshopping.component.autoedittext;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bishe.haitaoshopping.R;

/**
 * Created by yhviews on 2018/4/2.
 */

public class EmailAutoTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {
    private static final String TAG = "EmailAutoTextView";
    private String[] emailSufixs = new String[]{"@qq.com", "@163.com", "@126.com", "@gmail.com", "@sina.com", "@hotmail.com",
            "@yahoo.cn", "@sohu.com", "@foxmail.com", "@139.com", "@yeah.net", "@vip.qq.com", "@vip.sina.com"};
    private Button cleanButton;

    public EmailAutoTextView(Context context) {
        super(context);
        init(context);
    }

    public EmailAutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmailAutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCleanButton(Button cleanButton) {
        this.cleanButton = cleanButton;
    }

    private void init(final Context context){

        this.setAdapter(new EmailAutoCompleteAdapter(context, R.layout.auto_complete_item, emailSufixs));
        this.setThreshold(1);
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (cleanButton == null) {
                    return;
                }
                if (hasFocus) {
                    String text = EmailAutoTextView.this.getText().toString();
                    if (!"".equals(text)){
                        performFiltering(text, 0);
                    }
                    Log.i(TAG, hasFocus + "focus");
                    if (text.length() > 0)
                        cleanButton.setVisibility(VISIBLE);
                } else {
                    cleanButton.setVisibility(GONE);
                }
            }
        });
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cleanButton == null) {
                    return;
                }
                String text = EmailAutoTextView.this.getText().toString();
                if (text.length() > 0)
                    cleanButton.setVisibility(VISIBLE);
                if (text.length() == 0)
                    cleanButton.setVisibility(GONE);
            }
        });
    }

    @Override
    protected void replaceText(CharSequence text) {
        String t = this.getText().toString();
        int index = t.indexOf("@");
        if(index != -1)
            t = t.substring(0, index);//@符号前的字符串
        super.replaceText(t + text);//
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        //好像是很深奥的Android的过滤机制
        String t = text.toString();
        int index = t.indexOf("@");
        if (index == -1){
            if (t.matches("^[a-zA-Z0-9_]+$")){
                super.performFiltering("@", keyCode);
            }
        }else {
            super.performFiltering(t.substring(index), keyCode);
        }
    }

    private class EmailAutoCompleteAdapter extends ArrayAdapter<String> {
        //当用户输入数据后，AutoCompleteTextView就会将用户输入的数据与他自己的adapter中的数据对比，
        // 如果用户数据与adapter中的某条数据的开始部分完全匹配，
        // 那么adapter中的这条数据就会出现在下拉提示框中。
        public EmailAutoCompleteAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v;
            ViewHolder holder;
            if (convertView == null){
                v = LayoutInflater.from(getContext()).inflate(R.layout.auto_complete_item, null);
                holder = new ViewHolder();
                holder.email = v.findViewById(R.id.tv);
                v.setTag(holder);
            } else {
                v = convertView;
                holder = (ViewHolder) v.getTag();
            }
            String t = EmailAutoTextView.this.getText().toString();
            int index = t.indexOf("@");
            Log.i(TAG, "index:"+index);
            if (index != -1){//如果用户已经输入了@ 符号
                t = t.substring(0, index);
            }
            holder.email.setText(t + getItem(position));
//            Log.i(TAG, holder.email.getText().toString());
            return v;
        }
    }
    class ViewHolder {
        TextView email;
    }
}
