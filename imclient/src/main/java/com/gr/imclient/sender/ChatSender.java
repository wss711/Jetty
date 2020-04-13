package com.gr.imclient.sender;


import com.gr.im.common.bean.ChatMsg;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imclient.protoBuilder.ChatMsgBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-10 16:56
 */
@Slf4j
@Service("ChatSender")
public class ChatSender extends BaseSender{

    public void sendChatMsg(String toUid,String content){

        log.info("发送消息 startConnectServer");

        ChatMsg chatMsg = new ChatMsg(getUser());
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setContent(content);
        chatMsg.setTo(toUid);
        chatMsg.setMsgId(System.currentTimeMillis());

        ProtoMsg.Message message = ChatMsgBuilder.buildChatMsg(chatMsg,getUser(),getSession());

        super.sendMsg(message);
    }
    @Override
    public void sendSucced(ProtoMsg.Message message){
        log.info("发送成功： " + message.getMessageRequest().getContent());
    }

    @Override
    public void sendFailed(ProtoMsg.Message message){
        log.info("发送失败： " + message.getMessageRequest().getContent());
    }
}
