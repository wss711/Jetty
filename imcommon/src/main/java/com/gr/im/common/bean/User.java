package com.gr.im.common.bean;

import com.gr.im.common.bean.msg.ProtoMsg;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-08 17:13
 */
@Slf4j
@Data
public class User {

    String uid;
    String devId;
    String token;
    String nickName = "nickname";
    PLATTYPE platform = PLATTYPE.WINDOWS;

    // windows,mac,android, ios, web , other
    public enum PLATTYPE {
        WINDOWS, MAC, ANDROID, IOS, WEB, OTHER;
    }

    private String sessionId;

    public void setPlatform(int platform) {
        PLATTYPE[] values = PLATTYPE.values();
        for(int i = 0; i < values.length; i++){
            if(values[i].ordinal() == platform){
                this.platform = values[i];
            }
        }
    }


    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", devId='" + devId + '\'' +
                ", token='" + token + '\'' +
                ", nickName='" + nickName + '\'' +
                ", platform=" + platform +
                '}';
    }

    /** TODO 从登录消息中，反序列化User
     *
     * @param info
     * @return User
     *
     * @Author WSS
     * @date 2020-04-12
     */
    public static User fromMsg(ProtoMsg.LoginRequest info){

        User user = new User();
        user.uid = new String(info.getUid());
        user.devId = new String(info.getDeviceId());
        user.token = new String(info.getToken());
        user.setPlatform(info.getPlatform());
        log.info("登录中: {}", user.toString());
        return user;
    }

}
