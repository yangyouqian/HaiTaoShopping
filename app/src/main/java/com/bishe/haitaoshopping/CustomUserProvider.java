package com.bishe.haitaoshopping;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfileProvider;
import cn.leancloud.chatkit.LCChatProfilesCallBack;

/**
 * Created by yhviews on 2019/4/15.
 */

public class CustomUserProvider implements LCChatProfileProvider{
    private static CustomUserProvider customUserProvider;

    public synchronized static CustomUserProvider getInstance() {
        if (null == customUserProvider) {
            customUserProvider = new CustomUserProvider();
        }
        return customUserProvider;
    }

    private CustomUserProvider() {
    }

//    private static List<LCChatKitUser> partUsers = new ArrayList<LCChatKitUser>();
    private static Map<String, LCChatKitUser> partUsers = new HashMap<>();

    // 此数据均为模拟数据，仅供参考
//    static {
//        partUsers.add(new LCChatKitUser("Tom", "Tom", "http://www.avatarsdb.com/avatars/tom_and_jerry2.jpg"));
//        partUsers.add(new LCChatKitUser("Jerry", "Jerry", "http://www.avatarsdb.com/avatars/jerry.jpg"));
//        partUsers.add(new LCChatKitUser("Harry", "Harry", "http://www.avatarsdb.com/avatars/young_harry.jpg"));
//        partUsers.add(new LCChatKitUser("William", "William", "http://www.avatarsdb.com/avatars/william_shakespeare.jpg"));
//        partUsers.add(new LCChatKitUser("Bob", "Bob", "http://www.avatarsdb.com/avatars/bath_bob.jpg"));
//    }

    @Override
    public void fetchProfiles(List<String> list, final LCChatProfilesCallBack callBack) {
        final List<LCChatKitUser> userList = new ArrayList<LCChatKitUser>();
        final int len = list.size();
        for (int i = 0; i < len; i++) {
            final String userId = list.get(i);
            if (partUsers.get(userId) != null) {
                userList.add(partUsers.get(userId));
                if (i == len - 1) {
                    callBack.done(userList, null);
                }
            } else {
                AVQuery<AVUser> userQuery = new AVQuery<>("_User");
                userQuery.whereEqualTo("name", userId);
                final int finalI = i;
                userQuery.findInBackground(new FindCallback<AVUser>() {
                    @Override
                    public void done(List<AVUser> list, AVException e) {
                        if (e == null && list != null && list.size() > 0) {
                            AVUser user = list.get(0);
                            partUsers.put(userId, new LCChatKitUser(user.getString("name"), user.getString("name"), user.getString("avatar_url")));
                            if (finalI == len - 1) {
                                callBack.done(userList, null);
                            }
                        }
                    }
                });
            }
        }
//        callBack.done(userList, null);
    }

    public List<LCChatKitUser> getAllUsers() {
        return new ArrayList<>();
    }
}
