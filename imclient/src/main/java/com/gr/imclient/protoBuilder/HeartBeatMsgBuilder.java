package com.gr.imclient.protoBuilder;


import com.gr.im.common.bean.User;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imclient.client.ClientSession;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-09 16:34
 */
public class HeartBeatMsgBuilder extends BaseBuilder{

    private final User user;

    public HeartBeatMsgBuilder(User user, ClientSession session) {
        super(ProtoMsg.HeadType.HEART_BEAT,session);
        this.user = user;
    }

    public ProtoMsg.Message buildMsg(){
        ProtoMsg.Message msg = buildCommon(-1);
        ProtoMsg.MessageHeartBeat.Builder hb =
                ProtoMsg.MessageHeartBeat.newBuilder()
                    .setSeq(0)
                .setJson("{\"from\":\"com.jr.imclient.client\"}")
                .setUid(user.getUid());
        return msg.toBuilder().setHeartBeat(hb).build();
    }
}
