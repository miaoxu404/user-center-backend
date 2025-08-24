package com.max.usercebter.common;

/**
 * 错误码类
 *
 * @author max
 * 8/14/25
 */
public enum ErrorCode {

    // 成功状态码
    SUCCESS(0, "成功", "请求成功"),
    
    // 通用错误码
    PRAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", "请求数据不能为空"),
    NOT_LOGIN(40100, "未登录", ""),
    NOT_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常","");

    private final int code;

    /**
     * 错误信息
     */
    private final String message;

    /**
     * 错误描述
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
