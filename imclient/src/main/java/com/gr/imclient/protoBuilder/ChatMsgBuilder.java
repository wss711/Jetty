package com.gr.imclient.protoBuilder;


import com.gr.im.common.bean.ChatMsg;
import com.gr.im.common.bean.User;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imclient.client.ClientSession;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-10 17:24
 */
public class ChatMsgBuilder extends BaseBuilder {

    private ChatMsg chatMsg;
    private User user;

    public ChatMsgBuilder(ChatMsg chatMsg, User user, ClientSession session) {

        super(ProtoMsg.HeadType.MESSAGE_REQUEST,session);

        this.chatMsg = chatMsg;
        this.user = user;
    }

    public ProtoMsg.Message build(){

        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.MessageRequest.Builder reqBuilder
                = ProtoMsg.MessageRequest.newBuilder();

        chatMsg.fillMsg(reqBuilder);
        return message.toBuilder().setMessageRequest(reqBuilder).build();
    }

    public static ProtoMsg.Message buildChatMsg(ChatMsg chatMsg,User user,ClientSession session) {
        ChatMsgBuilder builder = new ChatMsgBuilder(chatMsg,user,session);
        return builder.build();
    }
}
