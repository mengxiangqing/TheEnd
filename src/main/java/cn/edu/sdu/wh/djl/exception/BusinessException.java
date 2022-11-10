package cn.edu.sdu.wh.djl.exception;

import cn.edu.sdu.wh.djl.common.ErrorCode;

/**
 * 自定义异常类，更灵活、快捷的设置字段
 *
 * @author SDDX
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -7061889126467896242L;
    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDesc();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
