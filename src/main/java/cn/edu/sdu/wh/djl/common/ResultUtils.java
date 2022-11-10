package cn.edu.sdu.wh.djl.common;

/**
 * 返回工具类
 *
 * @author SDDX
 */
public class ResultUtils {

    /**
     * 成功
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok", "成功");
    }

    /**
     * 失败
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    public static <T> BaseResponse<T> error(int errorCode, String message, String description) {
        return new BaseResponse<>(errorCode, null, message, description);
    }
}
