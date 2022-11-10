package cn.edu.sdu.wh.djl.controller;

import cn.edu.sdu.wh.djl.common.BaseResponse;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.common.ResultUtils;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.request.ChangePasswordRequest;
import cn.edu.sdu.wh.djl.model.request.UserLoginRequest;
import cn.edu.sdu.wh.djl.model.request.UserRegisterRequest;
import cn.edu.sdu.wh.djl.model.request.UserUpdateRequest;
import cn.edu.sdu.wh.djl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static cn.edu.sdu.wh.djl.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author SDDX
 */
@RestController
@RequestMapping("/user")
@Slf4j
@CrossOrigin(value = {"http://127.0.0.1:3000", "http://127.0.0.1:3001", "http://localhost:3000", "http://localhost:8000"}, allowCredentials = "true")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "参数为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAllBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);

        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "参数为空");
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN, "用户未登录");
        }
        Long userId = currentUser.getId();
        /* TODO 校验用户是否合法 */
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非管理员");
        }

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "请求参数id错误");
        }

        return ResultUtils.success(userService.removeById(id));
    }

    /**
     * 更新用户信息
     *
     * @param user    用户Id为必填项
     * @param request 请求
     * @return 结果
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateUser(@RequestBody UserUpdateRequest user, HttpServletRequest request) {
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        int result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }

    @PostMapping("/changePwd")
    public BaseResponse<Boolean> changePwd(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {
        if (changePasswordRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        Boolean result = userService.changePassword(changePasswordRequest, loginUser);
        return ResultUtils.success(result);
    }


}
