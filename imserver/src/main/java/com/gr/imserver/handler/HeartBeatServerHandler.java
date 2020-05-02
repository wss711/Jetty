package com.gr.imserver.handler;

import com.gr.concurrent.FutureTaskScheduler;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imserver.server.ServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/** TODO 心跳--利用netty自带的IdleStateHandler实现空闲状态处理
 *
 * @author WSS
 * @descripton
 * @date 2020-04-12 19:36
 */
@Slf4j
public class HeartBeatServerHandler extends IdleStateHandler {


    private static final int READ_IDLE_GAP = 150; // 心跳间隔

    public HeartBeatServerHandler(){
        //
        super(READ_IDLE_GAP,0,0, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{

        // 判断消息实例
        if(null == msg || !(msg instanceof ProtoMsg.Message)){
            super.channelRead(ctx,msg);
            return;
        }

        // 判断消息类型
        ProtoMsg.Message pmm = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType headType = pmm.getType();
        if(headType.equals(ProtoMsg.HeadType.HEART_BEAT)){

            //异步处理,将心跳包，直接回复给客户端
            FutureTaskScheduler.add(() -> {
                if(ctx.channel().isActive()){
                    ctx.writeAndFlush(msg);
                }
            });
        }

        super.channelRead(ctx,msg);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception{

        log.info(READ_IDLE_GAP + "秒内未读到数据，关闭连接");
        ServerSession.closeSession(ctx);

    }
}
