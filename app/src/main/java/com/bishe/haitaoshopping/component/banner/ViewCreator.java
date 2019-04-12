package com.bishe.haitaoshopping.component.banner;

import android.content.Context;
import android.view.View;

/**
 * Created by yhviews on 2019/4/11.
 */
public interface ViewCreator<T> {
    View createView(Context context, int position);

    void updateUI(Context context, View view, int position, T t);
}
