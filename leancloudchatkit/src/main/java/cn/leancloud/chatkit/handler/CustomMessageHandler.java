package cn.leancloud.chatkit.handler;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.event.LCIMIMTypeMessageEvent;
import cn.leancloud.chatkit.utils.LCIMLogUtils;
import de.greenrobot.event.EventBus;

/**
 * Created by yhviews on 2019/4/22.
 */

public class CustomMessageHandler extends AVIMMessageHandler {
    /**
     * 重载此方法来处理接收消息
     *
     * @param message
     * @param conversation
     * @param client
     */
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client){
        if (message == null || message.getMessageId() == null) {
            LCIMLogUtils.d("may be SDK Bug, message or message id is null");
            return;
        }

        if (LCChatKit.getInstance().getCurrentUserId() == null) {
            LCIMLogUtils.d("selfId is null, please call LCChatKit.open!");
            client.close(null);
        } else {
            if (!client.getClientId().equals(LCChatKit.getInstance().getCurrentUserId())) {
                client.close(null);
            } else {
                LCIMConversationItemCache.getInstance().insertConversation(message.getConversationId());
                if (!message.getFrom().equals(client.getClientId())) {
                    sendEvent(message, conversation);
                }
            }
        }
    }

    /**
     * 发送消息到来的通知事件
     *
     * @param message
     * @param conversation
     */
    private void sendEvent(AVIMMessage message, AVIMConversation conversation) {
        LCIMIMTypeMessageEvent event = new LCIMIMTypeMessageEvent();
        event.messageSys = message;
        event.conversation = conversation;
        EventBus.getDefault().post(event);
    }

}
