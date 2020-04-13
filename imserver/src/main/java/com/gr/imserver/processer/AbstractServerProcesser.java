package com.gr.imserver.processer;

import com.gr.imserver.server.ServerSession;
import io.netty.channel.Channel;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 15:51
 */
public abstract class AbstractServerProcesser implements  ServerProcesser {

    protected String getKey(Channel ch){
        return ch.attr(ServerSession.USER_ID_KEY).get();
    }

    protected void setKey(Channel ch,String key){
        ch.attr(ServerSession.USER_ID_KEY).set(key);
    }

    protected void checkAuth(Channel ch)throws Exception{
        if(null == getKey(ch)){
            throw new Exception("此用户还没有登录");
        }
    }
}
