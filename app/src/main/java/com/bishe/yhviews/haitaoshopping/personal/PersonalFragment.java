package com.bishe.yhviews.haitaoshopping.personal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bishe.yhviews.haitaoshopping.R;

/**
 * Created by yhviews on 2019/3/1.
 */

public class PersonalFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout rlUserInfo;
    private ImageView ivUserAvatar;
    private TextView tvUserName;
    private TextView tvExitLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        rlUserInfo = view.findViewById(R.id.rl_user_info);
        ivUserAvatar = view.findViewById(R.id.iv_user_avatar);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvExitLogin = view.findViewById(R.id.tv_exit_login);

        rlUserInfo.setOnClickListener(this);
        tvExitLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_user_info) {

        } else if (id == R.id.tv_exit_login) {

        }
    }
}
