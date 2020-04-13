package com.gr.imserver.handler;

import com.gr.im.common.exception.InvalidFrameException;
import com.gr.imserver.server.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 19:35
 */
@Slf4j
@ChannelHandler.Sharable
@Service("ServerExceptionHandler")
public class ServerExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable t)throws Exception{
        if(t instanceof InvalidFrameException){
            log.error(t.getMessage());
            ServerSession.closeSession(ctx);
        }else{
            log.error(t.getMessage());
            ctx.close();
        }
    }

    /** 通道 Read 读取 Complete 完成做刷新操作 ctx.flush() */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }

    /** 通道失活 */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        ServerSession.closeSession(ctx);
    }
}


