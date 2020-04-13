package com.gr.imserver.handler;

import com.gr.concurrent.FutureTaskScheduler;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imserver.processer.ChatRedirectProcesser;
import com.gr.imserver.server.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 19:35
 */
@Slf4j
@ChannelHandler.Sharable
@Service("ChatRedirectHandler")
public class ChatRedirectHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    ChatRedirectProcesser chatRedirectProcesser;

    /** TODO 处理收到的消息
     *
     * @param
     * @return
     * @Author WSS
     * @date 2020-04-13
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{

        // 判断消息实例
        if(null == msg || !(msg instanceof ProtoMsg.Message)){
            super.channelRead(ctx, msg);
            return;
        }

        // 判断消息类型
        ProtoMsg.Message pmm = (ProtoMsg.Message)msg;
        ProtoMsg.HeadType headType = ((ProtoMsg.Message)msg).getType();
        if(!headType.equals(chatRedirectProcesser.type())){
            super.channelRead(ctx, msg);
            return;
        }

        // 判断是否登录
        ServerSession session = ServerSession.getSession(ctx);
        if(null == session || !session.isLogin()){
            log.error("用户尚未登录，不能发送消息");
            return;
        }

        // 异步处理IM消息转发的逻辑
        FutureTaskScheduler.add(()->{
            chatRedirectProcesser.action(session,pmm);
        });

    }

}
