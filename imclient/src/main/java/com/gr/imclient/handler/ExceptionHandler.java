package com.gr.imclient.handler;

import com.gr.im.common.exception.InvalidFrameException;
import com.gr.imclient.client.ClientSession;
import com.gr.imclient.client.CommandController;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-09 18:28
 */
@Slf4j
@ChannelHandler.Sharable
@Service("ExceptionHandler")
public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private CommandController commandController;

    /** 抛出异常，并进行重连 */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{

        if(cause instanceof InvalidFrameException){
            log.error(cause.getMessage());
            ClientSession.getSession(ctx).close();
        }else {
            log.error(cause.getMessage());
            ctx.close();

            // 开始重连
            commandController.setConnectFlag(false);
            commandController.startConnectServer();
        }
    }

    /** 通道 Read 读取 Complete 完成,做刷新操作 ctx.flush() */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

}