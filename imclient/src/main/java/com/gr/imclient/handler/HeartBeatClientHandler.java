package com.gr.imclient.handler;


import com.gr.im.common.bean.User;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imclient.client.ClientSession;
import com.gr.imclient.protoBuilder.HeartBeatMsgBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/** TODO 心跳消息Builder
 * @author WSS
 * @descripton
 * @date 2020-04-09 15:30
 */
@Slf4j
@ChannelHandler.Sharable
@Service("HeartBeatClientHandler")
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

    //心跳的时间间隔，单位为s
    private static final int HEARTBEAT_INTERVAL = 100;

    // 在Handler被加入到Pipeline时，开始发送心跳
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        HeartBeatMsgBuilder hBuilder = new HeartBeatMsgBuilder(user,session);
        ProtoMsg.Message message = hBuilder.buildMsg();

        // 发送心跳
        heartBeat(ctx,message);
    }

    /** 使用定时器，发送心跳报文 */
    public void heartBeat(ChannelHandlerContext ctx,ProtoMsg.Message heartBeatMsg){
        ctx.executor().schedule(() -> {
            if(ctx.channel().isActive()){
                log.info(" 发送 HEART_BEAT  消息 to server");
                ctx.writeAndFlush(heartBeatMsg);

                //递归调用，发送下一次的心跳
                heartBeat(ctx, heartBeatMsg);
            }
        },HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    /** 接受到服务器的心跳回写 */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg)throws Exception{

        // 判断消息实例
        if(msg == null || !(msg instanceof ProtoMsg.Message)){
            super.channelRead(ctx,msg);
            return;
        }

        // 判断类型
        ProtoMsg.Message pmm = (ProtoMsg.Message)msg;
        ProtoMsg.HeadType headType = pmm.getType();
        if(headType.equals(ProtoMsg.HeadType.HEART_BEAT)){
            log.info(" 收到回写的 HEART_BEAT  消息 from server");
            return;
        }else{
            super.channelRead(ctx,msg);
        }
    }

}
