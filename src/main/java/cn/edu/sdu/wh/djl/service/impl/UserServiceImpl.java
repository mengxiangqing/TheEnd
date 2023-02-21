package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.common.CheckUtils;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.mapper.CourseMapper;
import cn.edu.sdu.wh.djl.mapper.UserMapper;
import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.request.ChangePasswordRequest;
import cn.edu.sdu.wh.djl.model.request.UserSearchRequest;
import cn.edu.sdu.wh.djl.model.request.UserUpdateRequest;
import cn.edu.sdu.wh.djl.model.vo.Teacher;
import cn.edu.sdu.wh.djl.service.CourseService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private CourseService courseService;


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
        queryWrapper.eq("user_account", userAccount);
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

    /**
     * 获取加密后的密码值
     */
    @NotNull
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
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
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
        safeUser.setGender(originUser.getGender());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setUpdateTime(originUser.getUpdateTime());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setBirth(originUser.getBirth());
        safeUser.setCollege(originUser.getCollege());
        safeUser.setGrade(originUser.getGrade());
        safeUser.setTitle(originUser.getTitle());
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
    public int updateUser(UserUpdateRequest userUpdateRequest, User loginUser) {
        // 如果用户没有传任何更新的值，会报错
        Long userId = userUpdateRequest.getId();
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
        if (!oldUser.getUserRole().equals(userUpdateRequest.getUserRole()) && !isSuperAdmin(loginUser)) {
            // 只有超级管理员可以修改用户权限
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 修改用户账号
        String newUserAccount = userUpdateRequest.getUserAccount();
        CheckUtils.checkUserAccount(newUserAccount);
        if (!oldUser.getUserAccount().equals(newUserAccount) && !isAdmin(loginUser)) {
            // 只有管理员或用户自己才可以修改账户
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        // 校验想修改的内容不包含特殊字符
        CheckUtils.checkUserAccount(userUpdateRequest.getUsername());
        User newUser = new User();
        BeanUtils.copyProperties(userUpdateRequest, newUser);
        return userMapper.updateById(newUser);
    }

    @Override
    public Boolean changePassword(ChangePasswordRequest changePasswordRequest, User loginUser) {

        String oldPwd = changePasswordRequest.getOldPassword();
        String newPwd = changePasswordRequest.getNewPassword();
        String confirmPwd = changePasswordRequest.getConfirmPassword();
        // 如果当前用户是管理员，只需要检查新密码
        if (this.isAdmin(loginUser)) {
            CheckUtils.checkUserPassword(newPwd);
        } else {
            // 用户自己修改
            // 1. 检查输入参数
            CheckUtils.checkUserPassword(newPwd);
            CheckUtils.checkUserPassword(oldPwd);
            CheckUtils.checkUserPassword(confirmPwd);
            // 2.尝试使用提供的账户密码去匹配用户
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
        }
        // 获取新密码加密值
        String newPwdEntry = getEntryPassword(newPwd);
        User newUser = new User();
        newUser.setId(loginUser.getId());
        newUser.setUserPassword(newPwdEntry);
        // 刷新更新人
        newUser.setUpdateUser(loginUser.getId());
        return this.updateById(newUser);
    }

    @Override
    public List<User> searchUsers(UserSearchRequest userSearchRequest) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userSearchRequest.getUsername())) {
            queryWrapper.like("user_name", userSearchRequest.getUsername());
        }
        if (StringUtils.isNotBlank(userSearchRequest.getUserAccount())) {
            queryWrapper.like("user_account", userSearchRequest.getUserAccount());
        }
        if (!userSearchRequest.getSort().isEmpty()) {
            String sortOps = userSearchRequest.getSort().get("createTime");
            if ("ascend".equals(sortOps)) {
                queryWrapper.orderByAsc("create_time");
            } else {
                queryWrapper.orderByDesc("create_time");
            }
        }

        if (!userSearchRequest.getFilter().isEmpty()) {
            List<String> status = userSearchRequest.getFilter().get("userStatus");
            List<String> roles = userSearchRequest.getFilter().get("userRole");
            if (status != null && !status.isEmpty()) {
                List<Long> statusNum = status.stream().map(Long::valueOf).collect(Collectors.toList());
                queryWrapper.in("user_status", statusNum);
            }
            if (roles != null && !roles.isEmpty()) {
                List<Long> rolesNum = roles.stream().map(Long::valueOf).collect(Collectors.toList());
                queryWrapper.in("user_role", rolesNum);
            }
        }

        List<User> userList = this.list(queryWrapper);
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
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
        return user != null && this.isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && (user.getUserRole() == ADMIN || isSuperAdmin(user));
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
