package com.bishe.haitaoshopping.personal;

import android.content.Intent;
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

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.chatkit.LCChatKit;
import com.bishe.haitaoshopping.chatkit.event.LoginOutEvent;

import de.greenrobot.event.EventBus;

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
        setUserName();
        return view;
    }

    private void setUserName() {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            tvUserName.setText((CharSequence) currentUser.get(Constant.USER_NAME));
            tvExitLogin.setVisibility(View.VISIBLE);
        }
    }

    private void hideUserName() {
        tvUserName.setText(R.string.please_login);
        tvExitLogin.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_LOGIN_BACK) {
            setUserName();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_user_info) {
            if (!Utils.checkLoginState()) {
                Intent intent = new Intent(this.getActivity(), LoginActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE_LOGIN_BACK);
            }
        } else if (id == R.id.tv_exit_login) {
            AVUser.logOut();
            LCChatKit.getInstance().close(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    Utils.showToast(getContext(), "退出登录成功");
                    EventBus.getDefault().post(new LoginOutEvent());
                }
            });
            hideUserName();
        }
    }
}
