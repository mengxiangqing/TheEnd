package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.mapper.UserMapper;
import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.vo.Teacher;
import cn.edu.sdu.wh.djl.service.TeacherService;
import cn.edu.sdu.wh.djl.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.sdu.wh.djl.constant.UserConstant.TEACHER;

/**
 * @author 蒙西昂请 创建于：2023/2/21 17:19:38
 */
public class TeacherServiceImpl implements TeacherService {
    @Resource
    private UserService userService;
    @Resource
    private UserMapper userMapper;



    public Teacher getTeacherById(long userId) {
        User userInfo = userMapper.selectById(userId);
        double averageUpRate = 0;
        double averageAttendRate = 0;
        double averageFrontRate = 0;
        // 查询课程
        List<Course> courses = new ArrayList<>();

        // 创建教师对象
        Teacher teacher = new Teacher();
        teacher.setUserInfo(userInfo);
        teacher.setTeachersCourse(courses);
        teacher.setAverageUpRate(averageUpRate);
        teacher.setAverageAttendRate(averageAttendRate);
        teacher.setAverageFrontRate(averageFrontRate);
        return teacher;

    }

    public List<Teacher> getTeachersByName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", username);
        queryWrapper.eq("user_role", TEACHER);
        List<User> userList = userMapper.selectList(queryWrapper);
        // 查询结果为空
        if (userList.isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<Teacher> teachers = new ArrayList<>();
        for (User user : userList) {

            QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
            courseQueryWrapper.eq("teacher_id", user.getId());
            // 用户信息脱敏
            Teacher teacher = new Teacher();
            teacher.setUserInfo(userService.getSafetyUser(user));
            // 从授课关系表找出此教师的课


            teachers.add(teacher);
        }
        return teachers;
    }
}
