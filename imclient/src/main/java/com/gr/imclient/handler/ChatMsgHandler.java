package com.gr.imclient.handler;

import com.gr.im.common.bean.msg.ProtoMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * @author WSS
 * @descripton
 * @date 2020-04-09 18:55
 */
@Slf4j
@ChannelHandler.Sharable
@Service("ChatMsgHandler")
public class ChatMsgHandler extends ChannelInboundHandlerAdapter {

    public ChatMsgHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 判断消息实例
        if(null == msg ||!(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        // 判断类型
        ProtoMsg.Message pmm = (ProtoMsg.Message)msg;
        ProtoMsg.HeadType headType = pmm.getType();
        if(!headType.equals(ProtoMsg.HeadType.MESSAGE_REQUEST)){
            super.channelRead(ctx,msg);
            return;
        }

        ProtoMsg.MessageRequest req = pmm.getMessageRequest();
        String content = req.getContent();
        String uid = req.getFrom();

        System.out.println(" 收到消息 from uid:" + uid + " -> " + content);
    }
}
