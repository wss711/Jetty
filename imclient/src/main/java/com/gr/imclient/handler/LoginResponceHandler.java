package com.gr.imclient.handler;


import com.gr.im.common.ProtoInstant;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imclient.client.ClientSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-09 14:32
 */
@Slf4j
@ChannelHandler.Sharable
@Service("LoginResponceHandler")
public class LoginResponceHandler extends ChannelInboundHandlerAdapter {

    /** 业务逻辑处理 */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{

        // 判断消息实例
        if(null == msg || !(msg instanceof ProtoMsg.Message)){
            super.channelRead(ctx, msg);
            return;
        }

        //判断类型
        ProtoMsg.Message pmm = (ProtoMsg.Message)msg;
        ProtoMsg.HeadType headType = ((ProtoMsg.Message)msg).getType();
        if(!headType.equals(ProtoMsg.HeadType.LOGIN_RESPONSE)){
            super.channelRead(ctx, msg);
            return;
        }

        // 判断返回是否成功
        ProtoMsg.LoginResponse info = pmm.getLoginResponse();

        ProtoInstant.ResultCodeEnum resultCodeEnum =
                ProtoInstant.ResultCodeEnum.values()[info.getCode()];
        if(!resultCodeEnum.equals(ProtoInstant.ResultCodeEnum.SUCCESS)){
            // 登录失败
            log.info(resultCodeEnum.getDesc());
        }else{
            // 登录成功
            ClientSession.loginSuccess(ctx,pmm);
            ChannelPipeline p = ctx.pipeline();
            // 移除登录响应处理器
            p.remove(this);
            // 在编码器后面，动态插入心跳处理器
            p.addAfter("encoder","heartbeat",new HeartBeatClientHandler());
        }

    }
}
