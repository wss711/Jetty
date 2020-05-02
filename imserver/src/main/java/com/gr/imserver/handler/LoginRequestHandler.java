package com.gr.imserver.handler;

import com.gr.concurrent.CallbackTask;
import com.gr.concurrent.CallbackTaskScheduler;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imserver.processer.LoginProcesser;
import com.gr.imserver.server.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;
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
@Service("LoginRequestHandler")
public class LoginRequestHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    LoginProcesser loginProcesser;

    /** TODO 收到消息
     *
     * @param
     * @return
     * @Author WSS
     * @date 2020-04-12
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception{

        // 初步判定消息类型
        if(null == msg || !(msg instanceof ProtoMsg.Message)){
            super.channelRead(ctx,msg);
            return;
        }

        ProtoMsg.Message pmm = (ProtoMsg.Message)msg;

        // 取得请求类型
        ProtoMsg.HeadType headType = pmm.getType();
        if(!headType.equals(loginProcesser.type())){
            super.channelRead(ctx,msg);
            return;
        }

        //
        ServerSession session = new ServerSession(ctx.channel());

        // 异步任务，处理登录的逻辑
        CallbackTaskScheduler.add(new CallbackTask<Boolean>() {
            @Override
            public Boolean execute() throws Exception {
                boolean r = loginProcesser.action(session,pmm);
                return r;
            }

            // 异步任务返回
            @Override
            public void onBack(Boolean r) {
                 if(r){
                     ctx.pipeline().remove(LoginRequestHandler.this);
                     log.info("登录成功:" + session.getUser());
                 }else{
                     ServerSession.closeSession(ctx);
                     log.info("登录失败:" + session.getUser());
                 }
            }

            // 异步任务异常
            @Override
            public void onException(Throwable t) {
                ServerSession.closeSession(ctx);
                log.info("登录失败:" + session.getUser());
            }
        });
    }
}
