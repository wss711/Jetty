package com.gr.imserver.server;

import com.gr.im.common.bean.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 15:52
 */
@Slf4j
@Data
public class ServerSession {

    public static final AttributeKey<String> USER_ID_KEY =
            AttributeKey.valueOf("user_id_key");

    public static final AttributeKey<ServerSession> SESSION_KEY =
            AttributeKey.valueOf("session_key");


    /** 用户实现服务端会话管理的核心 */
    private Channel channel;            // 通道
    private User user;                  // 用户
    private final String sessionId;     // session 唯一标识
    private boolean isLogin = false;    // 登录状态

    /** session中存储的session 变量属性值 */
    private Map<String,Object> map = new HashMap<>();

    public ServerSession(Channel channel) {
        this.channel = channel;
        this.sessionId = buildNewSessionId();
    }

    /** 反向重定向 */
    public static ServerSession getSession(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        return channel.attr(ServerSession.SESSION_KEY).get();
    }

    /** 关闭连接 */
    public static void closeSession(ChannelHandlerContext ctx){

        ServerSession session = ctx.channel().attr(ServerSession.SESSION_KEY).get();

        if(null != session && session.isValid()){
            session.close();
            SessionMap.getInstance().removeSession(session.getSessionId());
        }
    }

    public boolean isValid(){
        return getUser() != null?true:false;
    }

    /** 关闭连接 */
    public synchronized void close(){
        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(!future.isSuccess()){
                    log.error("Channle 关闭错误");
                }
            }
        });
    }

    /** 和 channel 通道实现双向绑定 */
    public ServerSession bind(){

        log.info(" ServerSession 绑定会话 " + channel.remoteAddress());

        channel.attr(ServerSession.SESSION_KEY).set(this);
        SessionMap.getInstance().addSession(getSessionId(),this);
        isLogin = true;

        return this;
    }

    /** 结束绑定 */
    public ServerSession unbind(){
        isLogin = false;
        SessionMap.getInstance().removeSession(getSessionId());
        this.close();
        return this;
    }

    /** 创建新的session id */
    private static String buildNewSessionId(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }

    /** 写Protobuf数据帧 */
    public synchronized void writeAndFlush(Object pmm){
        channel.writeAndFlush(pmm);
    }

}
