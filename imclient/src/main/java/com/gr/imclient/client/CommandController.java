package com.gr.imclient.client;


import com.gr.concurrent.FutureTaskScheduler;
import com.gr.im.common.bean.User;
import com.gr.imclient.command.*;

import com.gr.imclient.sender.ChatSender;
import com.gr.imclient.sender.LoginSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-08 14:43
 */
@Slf4j
@Data
@Service("CommandController")
public class CommandController {

    // 聊天命令收集类
    @Autowired
    ChatConsoleCommand chatConsoleCommand;
    // 登录命令收集类
    @Autowired
    LoginConsoleCommand loginConsoleCommand;
    // 退出命令收集类
    @Autowired
    LogoutConsoleCommand logoutConsoleCommand;
    // 菜单命令收集类
    @Autowired
    ClientCommandMenu clientCommandMenu;

    private Map<String, BaseCommand> commandMap;

    private String menuString;

    private ClientSession session;

    @Autowired
    private NettyClient nettyClient;

    private Channel channel;

    @Autowired
    private ChatSender chatSender;

    @Autowired
    private LoginSender loginSender;

    private boolean connectFlag = false;
    private User user;

    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f)->{

        log.info(new Date() + ": 连接已经断开……");
        channel = f.channel();

        // 创建会话
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.close();

        // 唤醒用户线程
        notifyCommandThread();

    };

    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f)->{

        final EventLoop eventLoop = f.channel().eventLoop();

        if(!f.isSuccess()){
            log.info("连接失败!在10s之后准备尝试重连!");
            eventLoop.schedule(
                    () -> nettyClient.doConnect(),
                    10,
                    TimeUnit.SECONDS);

            connectFlag = false;

        }else {

            connectFlag = true;

            log.info("四季领鲜 Fretty 服务器 连接成功!");

            channel = f.channel();

            // 创建会话
            session = new ClientSession(channel);
            session.setConnected(true);
            channel.closeFuture().addListener(closeListener);

            // 唤醒用户线程
            notifyCommandThread();
        }
    };

    /** 初始化命令集 */
    public void initCommandMap(){

        commandMap = new HashMap<>();
        commandMap.put(clientCommandMenu.getKey(), clientCommandMenu);
        commandMap.put(chatConsoleCommand.getKey(), chatConsoleCommand);
        commandMap.put(loginConsoleCommand.getKey(), loginConsoleCommand);
        commandMap.put(logoutConsoleCommand.getKey(), logoutConsoleCommand);

        Set<Map.Entry<String,BaseCommand>> entries = commandMap.entrySet();
        Iterator<Map.Entry<String, BaseCommand>> iterator = entries.iterator();

        StringBuilder menus = new StringBuilder();
        menus.append("[menu]");

        while (iterator.hasNext()){

            BaseCommand next = iterator.next().getValue();

            menus.append(next.getKey())
                    .append("->")
                    .append(next.getTip())
                    .append(" | ");
        }

        menuString = menus.toString();

        clientCommandMenu.setAllCommandsShow(menuString);

    }


    /** 开始连接服务器 */
    public void startConnectServer(){

        FutureTaskScheduler.add(() -> {
            nettyClient.setConnectedListener(connectedListener);
            nettyClient.doConnect();
        });

    }


    /** 唤醒命令收集线程 */
    public synchronized void notifyCommandThread(){
        this.notify();
    }

    /** 休眠，命令收集线程 */
    public synchronized void waitCommandThread(){

        try {
            System.out.println("============1");
            this.wait();
            System.out.println("============2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** 开启命令线程 */
    public void startCommandThread() throws InterruptedException{

        Thread.currentThread().setName("命令线程");

        while(true){

            // 建立连接
            while(connectFlag == false){

                // 开始连接
                startConnectServer();
                waitCommandThread();
                System.out.println("============2");
            }

            // 处理命令
            while (null != session){

                Scanner scanner = new Scanner(System.in);

                clientCommandMenu.exec(scanner);
                String key = clientCommandMenu.getCommandInput();
                BaseCommand command = commandMap.get(key);


                if(null == command){
                    System.err.println("无法识别[" + command + "]指令，请重新输入!");
                    continue;
                }

                switch (key){
                    case ChatConsoleCommand.KEY:
                        command.exec(scanner);
                        startOneChat((ChatConsoleCommand) command);
                        break;

                    case LoginConsoleCommand.KEY:
                        command.exec(scanner);
                        startLogin((LoginConsoleCommand) command);

                    case LogoutConsoleCommand.KEY:
                        command.exec(scanner);
                        startLogout(command);
                        break;
                }
            }
        }
    }

    /** 发送单聊消息 */
    private void startOneChat(ChatConsoleCommand c){

        // 登录
        if(!isLogin()){
            log.info("还没有登录，请先登录");
            return;
        }

        chatSender.setSession(session);
        chatSender.setSession(session);
        chatSender.setUser(user);
        chatSender.sendChatMsg(c.getToUserId(),c.getMessage());

    }

    /** 开始登录 */
    private void startLogin(LoginConsoleCommand command){

        // 登录
        if(!isConnectFlag()){
            log.info("连接异常，请重新建立连接");
            return;
        }

        User user = new User();
        user.setUid(command.getUserName());
        user.setToken(command.getPassword());
        user.setDevId("1111");

        this.user = user;
        session.setUser(user);
        loginSender.setUser(user);
        loginSender.setSession(session);
        loginSender.sendLoginMsg();

    }

    /** 注销 */
    private void startLogout(BaseCommand command){

        if(!isLogin()){
            log.info("还没有登录，请先登录");
            return;
        }

    }
    /** 是否已登录 */
    public boolean isLogin(){

        if(null == session){
            log.info("session is null");
            return false;
        }

        return session.isLogin();
    }

}
