package com.gr.im.common.codec;

import com.gr.im.common.ProtoInstant;
import com.gr.im.common.bean.msg.ProtoMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-07 19:44
 */
public class ProtobufEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtoMsg.Message msg, ByteBuf out) throws Exception {
        // 写入魔数
        out.writeShort(ProtoInstant.MAGIC_CODE);
        // 写入版本
        out.writeShort(ProtoInstant.VERSION_CODE);

        // 将对象转化为byte
        byte[] bytes = msg.toByteArray();
        //读取消息长度
        int length = bytes.length;
        // 写入消息长度
        out.writeInt(length);
        // 写入内容
        out.writeBytes(bytes);
    }
}
