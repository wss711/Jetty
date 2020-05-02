package com.gr.imserver.processer;

import com.gr.im.common.ProtoInstant;
import com.gr.im.common.bean.User;
import com.gr.im.common.bean.msg.ProtoMsg;
import com.gr.imserver.protobuilder.LoginResponceBuilder;
import com.gr.imserver.server.ServerSession;
import com.gr.imserver.server.SessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 19:44
 */
@Slf4j
@Service("LoginProcesser")
public class LoginProcesser extends AbstractServerProcesser{

    @Autowired
    LoginResponceBuilder loginResponceBuilder;

    /**
     * 消息类型
     */
    @Override
    public ProtoMsg.HeadType type() {
        return ProtoMsg.HeadType.LOGIN_REQUEST;
    }

    /** TODO 核心动作，完成登录请求处理
     *
     * @param session
     * @param pmm
     */
    @Override
    public boolean action(ServerSession session, ProtoMsg.Message pmm) {

        // 取出token验证
        ProtoMsg.LoginRequest info = pmm.getLoginRequest();
        long seqNo = pmm.getSequence();

        User user = User.fromMsg(info);

        // 检查用户
        boolean isValidUser = checkUser(user);
        if(!isValidUser){
            // 设置授权
            ProtoInstant.ResultCodeEnum rce = ProtoInstant.ResultCodeEnum.NO_TOKEN;
            // 构造再次登录失败的报文
            ProtoMsg.Message response = loginResponceBuilder.loginResponce(seqNo,"-1",rce);
            // 发送登录失败的报文
            session.writeAndFlush(response);

            return false;
        }

        // 设置session用户
        session.setUser(user);
        // 绑定
        session.bind();

        // 登录成功
        ProtoInstant.ResultCodeEnum resultCodeEnum = ProtoInstant.ResultCodeEnum.SUCCESS;
        // 构造登录成功的报文
        ProtoMsg.Message response = loginResponceBuilder.loginResponce(
                seqNo, session.getSessionId(), resultCodeEnum);
        // 发送登录成功的报文
        session.writeAndFlush(response);

        return true;
    }

    /** TODO 检测用户是否已经登录
     *
     * @param user
     * @return boolean
     * @Author WSS
     * @date 2020-04-12
     */
    private boolean checkUser(User user){
        if(SessionMap.getInstance().hasLogin(user)){
            return false;
        }

        // 以后参与数据库记录验证
        return true;
    }
}
