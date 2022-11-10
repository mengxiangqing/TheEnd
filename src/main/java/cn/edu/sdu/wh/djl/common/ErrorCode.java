package cn.edu.sdu.wh.djl.common;

/**
 * 错误码
 *
 * @author SDDX
 */

public enum ErrorCode {
    /*参数错误*/
    SUCCESS(0, "ok", ""),
    PARAM_ERROR(40000, "请求参数错误", ""),
    PARAM_NULL_ERROR(40001, "请求参数为空", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    NULL_ERROR(40002, "查询结果为空", ""),
    FORBIDDEN(40301, "禁止操作", "");

    private final int code;
    private final String message;
    private final String desc;

    ErrorCode(int code, String message, String desc) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDesc() {
        return desc;
    }
}
