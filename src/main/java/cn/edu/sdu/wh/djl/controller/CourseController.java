package cn.edu.sdu.wh.djl.controller;


import cn.edu.sdu.wh.djl.common.BaseResponse;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.common.ResultUtils;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.request.CourseAddRequest;
import cn.edu.sdu.wh.djl.model.request.CourseSearchRequest;
import cn.edu.sdu.wh.djl.model.request.CourseUpdateRequest;
import cn.edu.sdu.wh.djl.model.vo.CourseDetailResult;
import cn.edu.sdu.wh.djl.service.CourseService;
import cn.edu.sdu.wh.djl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 蒙西昂请 创建于：2023/2/21 16:10:38
 */
@RestController
@RequestMapping("/course")
@Slf4j
@CrossOrigin(value = {"http://127.0.0.1:3000", "http://127.0.0.1:3001",
        "http://localhost:3000", "http://localhost:8001", "http://localhost:8000"}, allowCredentials = "true")
public class CourseController {

    @Resource
    private CourseService courseService;
    @Resource
    private UserService userService;

    /**
     * 添加课程
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCourse(@RequestBody CourseAddRequest courseAddRequest, HttpServletRequest request) {
        if (courseAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        // 获取当前登录用户
        User currentUser = userService.getCurrentUser(request);

        // 只有管理员可以管理课程
        if (!userService.isAdmin(currentUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非管理员");
        }
        long result = courseService.addCourse(courseAddRequest, currentUser);
        return ResultUtils.success(result);
    }


    /**
     * 删除课程
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCourse(@RequestBody long id, HttpServletRequest request) {
        // 获取当前登录用户
        User currentUser = userService.getCurrentUser(request);

        // 只有管理员可以管理课程
        if (!userService.isAdmin(currentUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非管理员");
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "请求参数id错误");
        }
        return ResultUtils.success(courseService.deleteCourse(id, currentUser));
    }

    /**
     * 更新课程
     */
    @PostMapping("/update")
    public BaseResponse<Integer> updateCourse(@RequestBody CourseUpdateRequest courseUpdateRequest, HttpServletRequest request) {
        if (courseUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR);
        }
        // 获取当前登录用户
        User currentUser = userService.getCurrentUser(request);

        // 只有管理员可以管理课程
        if (!userService.isAdmin(currentUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "非管理员");
        }
        int result = courseService.updateCourse(courseUpdateRequest, currentUser);

        return ResultUtils.success(result);
    }


    /**
     * 搜索课程
     *
     * @param courseSearchRequest
     * @param request
     * @return
     */
    @PostMapping("/search")
    public BaseResponse<List<Course>> searchCourse(@RequestBody CourseSearchRequest courseSearchRequest, HttpServletRequest request) {

        // 获取当前用户，鉴定是否登录
        userService.getCurrentUser(request);

        List<Course> collect = courseService.searchCourses(courseSearchRequest);
        return ResultUtils.success(collect);
    }


    /**
     * 获取课程详细数据指标
     */
    @PostMapping("/detail")
    public BaseResponse<CourseDetailResult> getCourseDetail(@RequestBody long courseId, HttpServletRequest request) {
        // 获取当前用户，鉴定是否登录
        userService.getCurrentUser(request);
        CourseDetailResult collect = courseService.getCourseDetail(courseId);
        return ResultUtils.success(collect);
    }

    /**
     * 获取某教师的课程详细数据指标
     *
     * @param teacherNumber 教师学工号
     */
    @PostMapping("/byteacher")
    public BaseResponse<CourseDetailResult> getCourseDetailByTeacher(@RequestBody long teacherNumber, HttpServletRequest request) {
        // 获取当前用户，鉴定是否登录
        userService.getCurrentUser(request);

        CourseDetailResult collect = courseService.getCourseDetail(teacherNumber);
        return ResultUtils.success(collect);
    }


    /**
     * 获取用户已选课程
     *
     * @param courseSearchRequest
     * @param request
     * @return
     */
    @PostMapping("/selected")
    public BaseResponse<List<Course>> selectedCourse(@RequestBody CourseSearchRequest courseSearchRequest, HttpServletRequest request) {

        // 获取当前用户，鉴定是否登录
        User currentUser = userService.getCurrentUser(request);

        List<Course> collect = courseService.selectedCourses(currentUser);
        return ResultUtils.success(collect);
    }


    /**
     * 学生选课
     *
     * @param courseId
     * @param httpServletRequest
     * @return
     */
    @PostMapping("select")
    public BaseResponse<Integer> selectCourse(@RequestBody Long courseId, HttpServletRequest httpServletRequest) {
        User currentUser = userService.getCurrentUser(httpServletRequest);
        return ResultUtils.success(courseService.selectCourse(courseId, currentUser.getId()));
    }

    /**
     * 学生取消选课
     */
    @PostMapping("cancel")
    public BaseResponse<Integer> cancelSelectCourse(@RequestBody Long courseId, HttpServletRequest httpServletRequest) {
        User currentUser = userService.getCurrentUser(httpServletRequest);
        return ResultUtils.success(courseService.courseSelectCourse(courseId, currentUser.getId()));
    }


}
