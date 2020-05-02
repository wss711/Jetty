package com.gr.imserver.server;

import com.gr.im.common.bean.User;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-12 15:52
 */
@Slf4j
@Data
public final class SessionMap {

    // 单例操作
    private static SessionMap INSTANCE = new SessionMap();

    private SessionMap() {
    }

    public static SessionMap getInstance(){
        return INSTANCE;
    }

    // 会话集合
    private ConcurrentHashMap<String,ServerSession> map = new ConcurrentHashMap<>();

    /** 增加session对象 */
    public void addSession(String sessionId,ServerSession ss){

        map.put(sessionId,ss);

        log.info("用户登录:id= " + ss.getUser().getUid() +
                        "    在线总数: " + map.size());

    }

    /** 根据Session id 获取session对象 */
    public ServerSession getSession(String sessionId){
        if(map.containsKey(sessionId)){
            return map.get(sessionId);
        }else {
            return null;
        }
    }

    /**根据用户id，获取session对象 */
    public List<ServerSession> getSessionByUserId(String userId){

        List<ServerSession> list = map.values()
                .stream()
                .filter(serverSession -> serverSession.getUser().getUid().equals(userId))
                .collect(Collectors.toList());

        return list;
    }

    /** 根据Session id  删除session */
    public void removeSession(String sessionId){

        if(!map.containsKey(sessionId)){
            return;
        }

        ServerSession ss = map.get(sessionId);
        map.remove(sessionId);
        log.info("用户下线:id= " + ss.getUser().getUid() + "   在线总数: " + map.size());
    }

    /** 判断用户是否已登录 */
    public boolean hasLogin(User user){

        Iterator<Map.Entry<String,ServerSession>> iterator =
                map.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String,ServerSession> next = iterator.next();
            User u = next.getValue().getUser();
            if(u.getUid().equals(user.getUid()) &&
                    u.getPlatform().equals(user.getPlatform())){
                return true;
            }
        }

        return false;
    }
}
