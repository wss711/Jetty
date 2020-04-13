package com.gr.im.common;

/**
 * @author WSS
 * @descripton
 * @date 2020-04-07 19:47
 */
public class ProtoInstant {

    /** 魔数：可以通过配置获取 */
    public static final short MAGIC_CODE = 0x86;

    /** 版本号 */
    public static final short VERSION_CODE = 0x01;

    /**
     * 客户端平台
     * */
    public interface Platform{

        // windows端
        public static final int WINDOWS = 1;
        // mac端
        public static final int MAC = 2;
        // android端
        public static final int ANDEOID = 3;
        // IOS端
        public static final int IOS = 4;
        // WEB端
        public static final int WEB = 5;
        // 其他
        public static final int OTHER = 6;

    }

    /**
     *  返回码枚举类
     *  */
    public enum ResultCodeEnum{

        SUCCESS(0, "Success"),  // 成功
        AUTH_FAILED(1, "登录失败"),
        NO_TOKEN(2, "没有授权码"),
        UNKNOWN_ERROR(3, "未知错误"),;

        private Integer code;
        private String desc;

        ResultCodeEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
