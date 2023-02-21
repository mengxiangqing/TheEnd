package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.request.CourseAddRequest;
import cn.edu.sdu.wh.djl.model.request.CourseUpdateRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.service.CourseService;
import cn.edu.sdu.wh.djl.mapper.CourseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 蒙西昂请
 * @description 针对表【course】的数据库操作Service实现
 * @createDate 2023-02-21 15:55:11
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {
    @Resource
    CourseMapper courseMapper;

    @Override
    public long addCourse(CourseAddRequest courseAddRequest, User currentUser) {
        Course course = new Course();
        BeanUtils.copyProperties(courseAddRequest, course);
        course.setUpdateUser(currentUser.getId());
        course.setCreateUser(currentUser.getId());
        boolean saveResult = this.save(course);
        if (!saveResult) {
            return -1;
        }
        return course.getId();
    }

    @Override
    public int updateCourse(CourseUpdateRequest courseUpdateRequest, User currentUser) {
        Long id = courseUpdateRequest.getId();
        // 1. 检查用户ID
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }

        // 2.检查课程是否存在
        Course oldCourse = courseMapper.selectById(id);
        if (oldCourse == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "课程不存在");
        }

        Course newCourse = new Course();
        BeanUtils.copyProperties(courseUpdateRequest, newCourse);
        // 记录更新人
        newCourse.setUpdateUser(currentUser.getId());
        return  courseMapper.updateById(newCourse);
    }

    @Override
    public boolean deleteCourse(long id, User currentUser) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "该课程不存在");
        }
        course.setUpdateUser(currentUser.getId());
        return this.removeById(id);
    }
}




