package cn.edu.sdu.wh.djl.exception;


import cn.edu.sdu.wh.djl.common.BaseResponse;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 捕获代码中所有的异常，集中处理，内部消化，让前端得到更详细的业务报错、信息，同时屏蔽掉项目框架本身异常，不暴露服务器内部状态
 *
 * @author SDDX
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public <T> BaseResponse<T> businessExceptionHandler(BusinessException e) {
        log.warn("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> runtimeExceptionHandler(RuntimeException e) {
        log.warn("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }


}




