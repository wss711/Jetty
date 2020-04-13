package com.gr.imserver.protobuilder;

import com.gr.im.common.ProtoInstant;
import com.gr.im.common.bean.msg.ProtoMsg;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 15:15
 */
@Service("LoginResponceBuilder")
public class LoginResponceBuilder {

    public ProtoMsg.Message loginResponce(
                            long seqId,
                            String sessionId,
                            ProtoInstant.ResultCodeEnum rce){
        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.LOGIN_RESPONSE)  //设置消息类型
                .setSequence(seqId)
                .setSessionId(sessionId);       //设置应答流水，与请求对应

        ProtoMsg.LoginResponse.Builder lpb = ProtoMsg.LoginResponse.newBuilder()
                .setCode(rce.getCode())
                .setInfo(rce.getDesc())
                .setExpose(1);

        mb.setLoginResponse(lpb.build());

        return mb.build();
    }

}
