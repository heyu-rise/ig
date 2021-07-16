package org.heyu.ig.core;

/**
 * @author heyu
 * @date 2021/3/30
 */
public enum UipRequestTypeEnum {

    CONTROLLER(1, "接收数据"),
    REQUEST(2, "请求");

    private final int code;
    private final String desc;

    UipRequestTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
