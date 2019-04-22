package cn.leancloud.chatkit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMChatRoom;
import com.avos.avoscloud.im.v2.AVIMTemporaryConversation;
import com.avos.avoscloud.im.v2.AVIMConversationsQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;

import java.util.Arrays;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.R;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMConversationUtils;
import cn.leancloud.chatkit.utils.LCIMLogUtils;

/**
 * Created by wli on 16/2/29.
 * 会话详情页
 * 包含会话的创建以及拉取，具体的 UI 细节在 LCIMConversationFragment 中
 */
public class LCIMConversationActivity extends AppCompatActivity implements View.OnClickListener {

    protected LCIMConversationFragment conversationFragment;
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivThumbnail;
    private TextView itemTvTitle;
    private TextView itemTvSubTitle;
    private TextView tvDetail;
    private String covId;
    private RelativeLayout detailShopContainer;
    private String shopId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lcim_conversation_activity);
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivThumbnail = findViewById(R.id.iv_thumbnail);
        itemTvTitle = findViewById(R.id.item_tv_title);
        itemTvSubTitle = findViewById(R.id.item_tv_subtitle);
        tvDetail = findViewById(R.id.tv_detail);
        detailShopContainer = findViewById(R.id.cov_detail_shop_container);
        ivBack.setOnClickListener(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        conversationFragment = (LCIMConversationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        conversationFragment.setHasOptionsMenu(true);
        initByIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initByIntent(intent);
    }

    private void initByIntent(Intent intent) {
        if (null == LCChatKit.getInstance().getClient()) {
            showToast("please login first!");
            finish();
            return;
        }

        Bundle extras = intent.getExtras();
        if (null != extras) {
            if (extras.containsKey(LCIMConstants.PEER_ID)) {
                getConversation(extras.getString(LCIMConstants.PEER_ID), extras.getString("cov_name"));
            } else if (extras.containsKey(LCIMConstants.CONVERSATION_ID)) {
                String conversationId = extras.getString(LCIMConstants.CONVERSATION_ID);
                updateConversation(LCChatKit.getInstance().getClient().getConversation(conversationId), extras.getString("cov_name"));
            } else {
                showToast("memberId or conversationId is needed");
                finish();
            }
            shopId = extras.getString("shop_id");
        }
    }

    protected void initActionBar(String title) {
        tvTitle.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 主动刷新 UI
     *
     * @param conversation
     */
    protected void updateConversation(AVIMConversation conversation, String covName) {
        if (null != conversation) {
            if (conversation instanceof AVIMTemporaryConversation) {
                System.out.println("Conversation expired flag: " + ((AVIMTemporaryConversation) conversation).isExpired());
            }
            conversationFragment.setConversation(conversation);
            LCIMConversationItemCache.getInstance().insertConversation(conversation.getConversationId());

            if (covName != null) {
                initActionBar(covName);
            } else {
                initActionBar(conversation.getName());
            }
            boolean sys = false;
            if (conversation.get("sys") != null) {
                sys = (boolean) conversation.get("sys");
            }
            if (sys) {
                detailShopContainer.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(shopId)) {
                shopId = (String) conversation.get("shop_id");
            }
            initShop(conversation);
        }
    }

    private void initShop(AVIMConversation conversation) {
        if (TextUtils.isEmpty(shopId)) {
           return;
        }
        AVQuery<AVObject> avQuery = new AVQuery<>("Shop");
        avQuery.whereEqualTo("objectId", shopId);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

            }
        });
    }

    private void setShop(String url, String title, String subTitle) {
        itemTvTitle.setText(title);
        itemTvSubTitle.setText(subTitle);
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 获取 conversation
     * 为了避免重复的创建，createConversation 参数 isUnique 设为 true·
     */
    protected void getConversation(final String memberId, final String covName) {
        LCChatKit.getInstance().getClient().createConversation(
                Arrays.asList(memberId), covName, null, false, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (null != e) {
                            showToast(e.getMessage());
                        } else {
                            covId = avimConversation.getConversationId();
                            updateConversation(avimConversation, covName);
                        }
                    }
                });
    }

    /**
     * 弹出 toast
     *
     * @param content
     */
    private void showToast(String content) {
        Toast.makeText(LCIMConversationActivity.this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
//            finishActivity(RESULT_OK);
            finishActivity();
        }
    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra("cov_id", covId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }
}