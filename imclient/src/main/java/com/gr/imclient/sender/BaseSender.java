package com.gr.imclient.sender;

import com.gr.im.common.bean.User;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imclient.client.ClientSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-10 12:09
 */
@Data
@Slf4j
public abstract class BaseSender {

    private User user;
    private ClientSession session;

    public boolean isConnected(){
        if(session == null){
            log.info("Session is null");
            return false;
        }

        return session.isConnected();
    }

    public boolean isLogin(){
        if(null == session){
            log.info("Session is null");
        }

        return session.isLogin();
    }

    public void sendMsg(ProtoMsg.Message msg){

        if(null == getSession() || !isConnected()){
            log.info("设备连接还未成功");
            return;
        }

        Channel channel = getSession().getChannel();
        ChannelFuture f = channel.writeAndFlush(msg);
        f.addListener(future -> {
            if(future.isSuccess()){
                sendSucced(msg);
            }else {
                sendFailed(msg);
            }
        });
    }

    protected void sendSucced(ProtoMsg.Message msg){
        log.info("发送成功");
    }

    protected void sendFailed(ProtoMsg.Message msg){
        log.info("发送失败");
    }

}
