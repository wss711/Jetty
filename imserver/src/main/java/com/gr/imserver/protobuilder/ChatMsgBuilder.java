package com.gr.imserver.protobuilder;

import com.gr.im.common.ProtoInstant;
import com.gr.im.common.bean.msg.ProtoMsg;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 14:56
 */
public class ChatMsgBuilder {

    /** 创建回话响应 */
    public static ProtoMsg.Message buildChatResponse(
            long seqId, ProtoInstant.ResultCodeEnum rce){

        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.MESSAGE_RESPONSE)    //设置消息类型
                .setSequence(seqId);        // 设置应答流水，与请求对应

        ProtoMsg.MessageResponse.Builder mpb = ProtoMsg.MessageResponse.newBuilder()
                .setCode(rce.getCode())
                .setInfo(rce.getDesc())
                .setExpose(1);

        mb.setMessageResponse(mpb.build());

        return mb.build();
    }

    /** 登录响应 应答消息protobuf */
    public static ProtoMsg.Message buildLoginResponse(
            long seqId,ProtoInstant.ResultCodeEnum rce){

        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.MESSAGE_RESPONSE) //设置消息类型
                .setSequence(seqId);    // 设置应答流水，与请求对应

        ProtoMsg.LoginResponse.Builder lpb = ProtoMsg.LoginResponse.newBuilder()
                .setCode(rce.getCode())
                .setInfo(rce.getDesc())
                .setExpose(1);
        mb.setLoginResponse(lpb.build());

        return mb.build();
    }
}
