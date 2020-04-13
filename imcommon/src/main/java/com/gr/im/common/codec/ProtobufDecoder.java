package com.gr.im.common.codec;

import com.gr.im.common.ProtoInstant;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.im.common.exception.InvalidFrameException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-08 11:16
 */
public class ProtobufDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        /** 标记一下当前的readIndex的位置 */
        in.markReaderIndex();

        /** 判断包头长度 */
        if(in.readableBytes() < 8){ //不够包头长度
            return;
        }

        /** 读取魔数 */
        short magic = in.readShort();
        if(magic != ProtoInstant.MAGIC_CODE){
            String error = "客户端口令不对：" + ctx.channel().remoteAddress();
            throw new InvalidFrameException(error);
        }


        /** 读取版本 */
        short version = in.readShort();
        /** 读取消息长度 */
        int length = in.readInt();

        if(length < 0){ //非法数据，关闭连接
            ctx.close();
        }

        if(length > in.readableBytes()){ //读到的消息体的长度如果小于传过来的消息的长度
            in.resetReaderIndex();   //重置读取位置
            return;
        }

        /** 获取业务消息 */
        // 获取消息数组
        byte[] array;
        if(in.hasArray()){ //有支撑数组，堆缓冲
            ByteBuf slice = in.slice();
            array = slice.array();
        }else{  // 直接缓冲
            array = new byte[length];
            in.readBytes(array,0,length);
        }
        // 字节数组转成对象
        ProtoMsg.Message outMsg = ProtoMsg.Message.parseFrom(array);
        if(outMsg != null){ //获取业务消息
            out.add(outMsg);
        }
    }
}
