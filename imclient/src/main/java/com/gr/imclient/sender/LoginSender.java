package com.gr.imclient.sender;


import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imclient.protoBuilder.LoginMsgBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-10 16:56
 */
@Slf4j
@Service("LoginSender")
public class LoginSender extends BaseSender{

    public void sendLoginMsg(){
        if(!isConnected()){
            log.info("还未建立连接");
            return;
        }

        log.info("构造登录信息");
        ProtoMsg.Message message = LoginMsgBuilder.buildLoginMsg(getUser(),getSession());
        log.info("发送登录信息");
        super.sendMsg(message);
    }
}
