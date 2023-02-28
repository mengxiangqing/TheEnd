package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.mapper.UserMapper;
import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.request.CourseAddRequest;
import cn.edu.sdu.wh.djl.model.request.CourseUpdateRequest;
import cn.edu.sdu.wh.djl.model.vo.CourseDetailResult;
import cn.edu.sdu.wh.djl.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 蒙西昂请 创建于：2023/2/21 19:23:44
 */
@Slf4j
@SpringBootTest
class CourseServiceImplTest {
    @Resource
    private CourseService courseService;

    @Resource
    private UserMapper userMapper;

    @Test
    void addCourse() {
        CourseAddRequest courseAddRequest = new CourseAddRequest();
        courseAddRequest.setCourseName("软件工程");
        courseAddRequest.setCourseNumber("2020123");
        courseAddRequest.setTeachingTime("2022-2023");
        courseAddRequest.setCollege("机电与信息工程学院");
        courseAddRequest.setPhone("15588352518");
        courseAddRequest.setTeacher(202037510L);
        courseAddRequest.setClassroomId(1L);
        courseAddRequest.setStartWeek(1);
        courseAddRequest.setEndWeek(16);
        courseAddRequest.setChooseNum(50);

        User user = userMapper.selectById(1);
        long id = courseService.addCourse(courseAddRequest, user);

        courseService.removeById(id);

        log.debug(String.valueOf(id));

    }

    @Test
    void updateCourse() {

        CourseUpdateRequest courseUpdateRequest = new CourseUpdateRequest();
        courseUpdateRequest.setId(1L);
        courseUpdateRequest.setCourseName("软件工程");
        courseUpdateRequest.setCourseNumber("2020123");
        courseUpdateRequest.setTeachingTime("2022-2023");
        courseUpdateRequest.setCollege("机电学院");
        courseUpdateRequest.setPhone("15588352518");
        courseUpdateRequest.setClassroomId(1L);
        courseUpdateRequest.setStartWeek(1);
        courseUpdateRequest.setEndWeek(16);
        courseUpdateRequest.setChooseNum(50);

        User user = userMapper.selectById(1);
        int b = courseService.updateCourse(courseUpdateRequest, user);
        System.out.println(b);


    }

    @Test
    void getCourseDetail() {
        CourseDetailResult courseDetail = courseService.getCourseDetail(1);


    }
}