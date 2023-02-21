package cn.edu.sdu.wh.djl.controller;


import cn.edu.sdu.wh.djl.common.BaseResponse;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.common.ResultUtils;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.model.request.CourseAddRequest;
import cn.edu.sdu.wh.djl.model.request.CourseUpdateRequest;
import cn.edu.sdu.wh.djl.service.CourseService;
import cn.edu.sdu.wh.djl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 蒙西昂请 创建于：2023/2/21 16:10:38
 */
@RestController
@RequestMapping("/course")
@Slf4j
@CrossOrigin(value = {"http://127.0.0.1:3000", "http://127.0.0.1:3001",
        "http://localhost:3000", "http://localhost:8000"}, allowCredentials = "true")
public class CourseController {

    @Resource
    private CourseService courseService;
    @Resource
    private UserService userService;

    /**
     * 添加课程
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCourse(@RequestBody CourseAddRequest courseAddRequest) {
        if (courseAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }

        long result = courseService.addCourse(courseAddRequest);
        return ResultUtils.success(result);
    }


    /**
     * 删除课程
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCourse(@RequestBody long id, HttpServletRequest request) {
        if (userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非管理员");
        }

        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "请求参数id错误");
        }
        return ResultUtils.success(courseService.removeById(id));
    }

    /**
     * 更新课程
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest, HttpServletRequest request) {
        if (courseUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        // 只有管理员可以管理课程
        if (userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非管理员");
        }
        int result = courseService.updateCourse(courseUpdateRequest);

        return ResultUtils.success(result);
    }


}
