package cn.edu.sdu.wh.djl.service;

import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.request.ChangePasswordRequest;
import cn.edu.sdu.wh.djl.model.request.UserSearchRequest;
import cn.edu.sdu.wh.djl.model.request.UserUpdateRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author SDDX
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 二次密码
     * @return 返回用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      http请求
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser 用户原始信息
     * @return 脱敏用户信息
     */
    User getSafetyUser(User originUser);

    /**
     * 用户登出
     *
     * @param request 网络
     * @return 1
     */
    int userLogout(HttpServletRequest request);


    /**
     * 更新用户
     *
     * @param user      传过来的用户信息
     * @param loginUser 已登录的用户信息
     * @return 更新结果
     */
    int updateUser(UserUpdateRequest user, User loginUser);

    /**
     * 是否是管理员
     *
     * @param request 请求
     * @return true or false
     */
    boolean isAdmin(@NotNull HttpServletRequest request);

    /**
     * 是否是管理员
     *
     * @param user 用户
     * @return true or false
     */
    boolean isAdmin(User user);

    /**
     * 从session获取当前用户
     *
     * @param request 请求
     * @return 当前用户
     */
    User getCurrentUser(HttpServletRequest request);


    /**
     * 修改用户密码
     *
     * @param changePasswordRequest 更改密码请求体
     * @param loginUser             当前登录用户
     * @return 是否成功
     */
    Boolean changePassword(ChangePasswordRequest changePasswordRequest, User loginUser);

    List<User> searchUsers(UserSearchRequest userSearchRequest);
}
