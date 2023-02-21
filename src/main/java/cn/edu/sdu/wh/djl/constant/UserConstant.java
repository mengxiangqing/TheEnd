package cn.edu.sdu.wh.djl.constant;

/**
 * @author SDDX
 */
public interface UserConstant {
    /**
     * 用户登录状态
     */
    String USER_LOGIN_STATE = "userLoginState";
    /**
     * 普通用户
     */
    int DEFAULT_ROLE = 0;

    /**
     * 学生用户
     */
    int STUDENT = 1;

    /**
     * 教师用户
     */
    int TEACHER = 2;

    /**
     * 督导员
     */
    int SUPERVISOR =3;

    /**
     * 管理员
     */
    int ADMIN = 4;

    /**
     * 超级管理员
     */
    int SUPER_ADMIN = 666;
}
