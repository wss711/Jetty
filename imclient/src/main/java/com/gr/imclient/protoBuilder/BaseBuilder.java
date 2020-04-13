package com.gr.imclient.protoBuilder;


import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imclient.client.ClientSession;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-09 16:35
 */
public class BaseBuilder {

    protected ProtoMsg.HeadType type;
    private long seqId;
    private ClientSession session;

    public BaseBuilder(ProtoMsg.HeadType type, ClientSession session) {
        this.type = type;
        this.session = session;
    }

    /** 构建消息基础部分 */
    public ProtoMsg.Message buildCommon(long seqId){

        this.seqId = seqId;

        ProtoMsg.Message.Builder mb = ProtoMsg.Message
                                        .newBuilder()
                                        .setType(type)
                                        .setSessionId(session.getSessionId())
                                        .setSequence(seqId);
        return mb.buildPartial();
    }
}
