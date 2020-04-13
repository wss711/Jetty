package com.gr.imclient.client;


import com.gr.im.common.bean.User;
import com.gr.im.common.codec.ProtobufDecoder;
import com.gr.im.common.codec.ProtobufEncoder;
import com.gr.imclient.handler.ChatMsgHandler;
import com.gr.imclient.handler.ExceptionHandler;
import com.gr.imclient.handler.LoginResponceHandler;
import com.gr.imclient.sender.ChatSender;
import com.gr.imclient.sender.LoginSender;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-09 11:59
 */
@Slf4j
@Data
@Service("NettyClient")
public class NettyClient {

    // 服务器ip地址
//    @Value("${server.ip}")
    private String host = "192.168.50.37";

    // 服务器端口
//    @Value("${server.port}")
    private int port = 10711;

    @Autowired
    private ChatMsgHandler chatMsgHandler;

    @Autowired
    private LoginResponceHandler loginResponceHandler;

    @Autowired
    private ExceptionHandler exceptionHandler;

    private Channel channel;
    private ChatSender chatSender;
    private LoginSender loginSender;
    private boolean initFlag = true;
    private User user;
    private GenericFutureListener<ChannelFuture> connectedListener;
    private Bootstrap b;
    private EventLoopGroup g;

    /** 通过nio方式来接收连接和处理连接 */
    public NettyClient() {
        g = new NioEventLoopGroup();
    }

    /** 重连 */
    public void doConnect(){

        try {
            b = new Bootstrap();

            b.group(g);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.remoteAddress(host, port);

            // 设置通道初始化
            b.handler(
                    new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast("decoder", new ProtobufDecoder());
                            ch.pipeline().addLast("encoder", new ProtobufEncoder());
                            ch.pipeline().addLast(loginResponceHandler);
                            ch.pipeline().addLast(chatMsgHandler);
                            ch.pipeline().addLast(exceptionHandler);
                        }
                    }
            );
            log.info("客户端开始连接 四季领鲜[Fretty]服务器");

            ChannelFuture f = b.connect();
            f.addListener(connectedListener);


        }catch (Exception e){
            log.info("客户端连接失败!" + e.getMessage());
        }
    }

    public void close(){
        g.shutdownGracefully();
    }


}



