package cn.edu.sdu.wh.djl.service;

import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.request.CourseAddRequest;
import cn.edu.sdu.wh.djl.model.request.CourseUpdateRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 蒙西昂请
* @description 针对表【course】的数据库操作Service
* @createDate 2023-02-21 15:55:11
*/
public interface CourseService extends IService<Course> {

    /**
     * 添加课程
     * @param courseAddRequest
     * @return
     */
    long addCourse(CourseAddRequest courseAddRequest);

    /**
     * 修改课程
     * @param courseUpdateRequest
     * @return
     */
    int updateCourse(CourseUpdateRequest courseUpdateRequest);

}
