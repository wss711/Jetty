package com.gr.imserver.processer;

import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imserver.server.ServerSession;
import com.gr.imserver.server.SessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 20:15
 */
@Slf4j
@Service("ChatRedirectProcesser")
public class ChatRedirectProcesser extends AbstractServerProcesser {
    /**
     * 消息类型
     */
    @Override
    public ProtoMsg.HeadType type() {
        return ProtoMsg.HeadType.MESSAGE_REQUEST;
    }

    /**
     * 执行动作
     *
     * @param fromSession
     * @param protoMsg
     */
    @Override
    public boolean action(ServerSession fromSession, ProtoMsg.Message protoMsg) {

        // 消息交互处理
        ProtoMsg.MessageRequest msg = protoMsg.getMessageRequest();
        log.info("ChatMsg | from=" + msg.getFrom() + " , to=" + msg.getTo() +
                " , content=" + msg.getContent());
        // 获取接收方的chatID
        String to = msg.getTo();
        List<ServerSession> toSessions = SessionMap.getInstance().getSessionByUserId(to);
        if(toSessions == null){
            //接收方离线
            log.info("[" + to + "] 不在线，发送失败!");
        }else {

            toSessions.forEach((session)->{
                // 将IM消息发送到接收方
                session.writeAndFlush(protoMsg);
            });
        }
        return true;
    }

}
