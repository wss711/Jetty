package com.gr.imclient.client;


import com.gr.im.common.bean.User;
import com.gr.im.common.bean.msg.ProtoMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-08 14:49
 */
@Slf4j
@Data
public class ClientSession {

    public static final AttributeKey<ClientSession> SESSION_KEY
                                = AttributeKey.valueOf("SESSION_KEY");

    /** 用户实现客户端会话管理的核心 */
    private Channel channel;
    private User user;

    /** 保存登录后的服务端sessionid */
    private String sessionId;
    private boolean isConnected = false;
    private boolean isLogin = false;

    /** session中存储的session 变量属性值 */
    private Map<String,Object> map = new HashMap<>();


    // 绑定通道
    public ClientSession(Channel channel){
        this.channel = channel;
        this.sessionId = String.valueOf(-1);
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }
    // 登录成功之后，设置sessionId
    public static void loginSuccess(ChannelHandlerContext ctx, ProtoMsg.Message pmm){
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(pmm.getSessionId());
        session.setLogin(true);
        log.info("登录成功");
    }
    // 获取channel
    public static ClientSession getSession(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        return session;
    }
    // 获取远程调用地址
    public String getRemoteAddress(){
        return channel.remoteAddress().toString();
    }
    // 写protobuf 数据帧
    public ChannelFuture writeAndFlush(Object pmm){
        ChannelFuture f = channel.writeAndFlush(pmm);
        return f;
    }
    public void writeAndClose(Object pmm){
        ChannelFuture f = channel.writeAndFlush(pmm);
        f.addListener(ChannelFutureListener.CLOSE);
    }
    // 关闭通道
    public void close(){
        isConnected = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    log.info("连接顺利断开");
                }
            }
        });
    }

}
