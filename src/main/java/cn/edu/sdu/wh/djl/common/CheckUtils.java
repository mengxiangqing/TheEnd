package cn.edu.sdu.wh.djl.common;

import cn.edu.sdu.wh.djl.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author 蒙西昂请 创建于：2022/10/9 23:03:54
 */
public class CheckUtils {
    public static final int USER_ACCOUNT_MIN_LENGTH = 4;
    public static final int USER_ACCOUNT_MAX_LENGTH = 16;
    public static final int USER_PASSWORD_MIN_LENGTH = 8;
    public static final int USER_PASSWORD_MAX_LENGTH = 16;

    public static void checkUserAccount(String account) {
        if (StringUtils.isBlank(account)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (account.length() > USER_ACCOUNT_MAX_LENGTH || account.length() < USER_ACCOUNT_MIN_LENGTH) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户账户长度错误，请设置在" + USER_ACCOUNT_MIN_LENGTH + "-" + USER_ACCOUNT_MAX_LENGTH + "位");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?！￥…（）—【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);

        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户账户不能包含特殊字符");
        }
    }

    public static void checkUserPassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        if (password.length() > USER_PASSWORD_MAX_LENGTH || password.length() < USER_PASSWORD_MIN_LENGTH) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户密码长度错误，请设置在" + USER_PASSWORD_MIN_LENGTH + "-" + USER_PASSWORD_MAX_LENGTH + "位");
        }
    }
}
