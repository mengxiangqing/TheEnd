package cn.edu.sdu.wh.djl.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @author SDDX
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 4925069859145308231L;

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDesc());
    }
}
