package com.gr.im.common.bean;

import com.gr.im.common.bean.msg.ProtoMsg;
import lombok.Data;
import org.apache.commons.lang.StringUtils;


/**
 * @author WSS
 * @descripton
 * @date 2020-04-09 18:56
 */
@Data
public class ChatMsg {

    private User user;

    public enum MSGTYPE{   // 消息类型
        TEXT,   // 1、纯文本
        AUDIO,  // 2、音频
        VIDEO,  // 3、视频
        POSITION,    // 4、位置
        OTHER;  // 5、其他
    }

    public ChatMsg(User user) {
        this.user = user;
    }

    private long msgId;
    private String from;
    private String to;
    private long time;
    private MSGTYPE msgType;
    private String content;
    private String url;
    private String property;
    private String fromNick;
    private String json;

    public void fillMsg(ProtoMsg.MessageRequest.Builder rb){
        if (msgId > 0) {
            rb.setMsgId(msgId);
        }
        if (StringUtils.isNotEmpty(from)) {
            rb.setFrom(from);
        }
        if (StringUtils.isNotEmpty(to)) {
            rb.setTo(to);
        }
        if (time > 0) {
            rb.setTime(time);
        }
        if (msgType != null) {
            rb.setMsgType(msgType.ordinal());
        }
        if (StringUtils.isNotEmpty(content)) {
            rb.setContent(content);
        }
        if (StringUtils.isNotEmpty(url)) {
            rb.setUrl(url);
        }
        if (StringUtils.isNotEmpty(property)) {
            rb.setProperty(property);
        }
        if (StringUtils.isNotEmpty(fromNick)) {
            rb.setFromNick(fromNick);
        }

        if (StringUtils.isNotEmpty(json)) {
            rb.setJson(json);
        }
    }


}
