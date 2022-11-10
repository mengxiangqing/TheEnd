package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.common.CheckUtils;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.mapper.UserMapper;
import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.request.ChangePasswordRequest;
import cn.edu.sdu.wh.djl.model.request.UserUpdateRequest;
import cn.edu.sdu.wh.djl.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static cn.edu.sdu.wh.djl.constant.UserConstant.*;

/**
 * 用户服务接口
 *
 * @author meng
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "user-center";



    @Resource
    private UserMapper userMapper;


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "参数为空");
        }
        CheckUtils.checkUserAccount(userAccount);
        CheckUtils.checkUserPassword(userPassword);
        CheckUtils.checkUserPassword(checkPassword);

        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "两次密码不一致");
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户已存在");
        }

        // 2.加密
        String entryPassword = getEntryPassword(userPassword);

        // 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(entryPassword);

        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @NotNull
    // 获取加密后的密码值
    private String getEntryPassword(String userPassword) {
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        CheckUtils.checkUserPassword(userPassword);
        CheckUtils.checkUserAccount(userAccount);

        // 2.加密
        String encryptPassword = getEntryPassword(userPassword);

        User user = getUserByAccountAndPassword(userAccount, encryptPassword);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户不存在或密码错误");
        }

        // 3.用户信息脱敏
        User safeUser = getSafetyUser(user);

        // 4.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);

        return safeUser;
    }

    // 根据账户密码获取用户
    private User getUserByAccountAndPassword(String userAccount, String encryptPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setTags(originUser.getTags());
        safeUser.setProfile(originUser.getProfile());
        return safeUser;
    }

    /**
     * 用户注销
     *
     * @param request 网络
     * @return 1
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


    @Override
    public int updateUser(UserUpdateRequest user, User loginUser) {
        // 如果用户没有传任何更新的值，会报错
        Long userId = user.getId();
        // 1. 检查用户输入的id
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        // 2. 如果是管理员，允许修改任意用户
        if (!isAdmin(loginUser) && !loginUser.getId().equals(userId)) {
            // 如果不是管理员，只允许修改自己的信息
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 3. 获取旧的用户信息
        User oldUser = userMapper.selectById(userId);
        if (oldUser == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户不存在");
        }
        // 修改用户权限
        if (!oldUser.getUserRole().equals(user.getUserRole()) && !isSuperAdmin(loginUser)) {
            // 只有超级管理员可以修改用户权限
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 修改用户账号
        String newUserAccount = user.getUserAccount();
        CheckUtils.checkUserAccount(newUserAccount);
        if (!oldUser.getUserAccount().equals(newUserAccount) && !isAdmin(loginUser)) {
            // 只有管理员或用户自己才可以修改账户
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 校验想修改的内容不包含特殊字符
        CheckUtils.checkUserAccount(user.getUsername());
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        return userMapper.updateById(newUser);
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest changePasswordRequest, User loginUser) {

        String oldPwd = changePasswordRequest.getOldPassword();
        String newPwd = changePasswordRequest.getNewPassword();
        String confirmPwd = changePasswordRequest.getConfirmPassword();
        // 1. 检查输入参数
        CheckUtils.checkUserPassword(newPwd);
        CheckUtils.checkUserPassword(oldPwd);
        CheckUtils.checkUserPassword(confirmPwd);

        Long userId = loginUser.getId();
        User user = getUserByAccountAndPassword(loginUser.getUserAccount(), getEntryPassword(oldPwd));
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "旧密码不匹配");
        }
        if (newPwd.equals(oldPwd)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "与最近的密码重复");
        }
        // 检查两次新密码是否匹配
        if (!newPwd.equals(confirmPwd)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "两次密码不一致");
        }
        String newPwdEntry = getEntryPassword(newPwd);
        User newUser = new User();
        newUser.setId(userId);
        newUser.setUserPassword(newPwdEntry);
        return this.updateById(newUser);
    }

    /**
     * 是否是管理员
     *
     * @param request 请求
     * @return true or false
     */
    @Override
    public boolean isAdmin(@NotNull HttpServletRequest request) {
        // 仅管理员可查询
        User user = getCurrentUser(request);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && (user.getUserRole() == ADMIN_ROLE || isSuperAdmin(user));
    }

    public boolean isSuperAdmin(User user) {
        return user != null && user.getUserRole() == SUPER_ADMIN;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return user;
    }

}
