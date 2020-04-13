package com.gr.imserver.processer;

import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imserver.server.ServerSession;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 15:51
 */
public interface ServerProcesser {

    /** 消息类型 */
    ProtoMsg.HeadType type();

    /** 执行动作 */
    boolean action(ServerSession session,ProtoMsg.Message protoMsg);

}
