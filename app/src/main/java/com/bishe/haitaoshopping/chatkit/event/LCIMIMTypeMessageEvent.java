package com.bishe.haitaoshopping.chatkit.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

/**
 * Created by wli on 15/8/23.
 * 收到 AVIMTypedMessage 消息后的事件
 */
public class LCIMIMTypeMessageEvent {
  public AVIMTypedMessage message;
  public AVIMMessage messageSys;
  public AVIMConversation conversation;
}
