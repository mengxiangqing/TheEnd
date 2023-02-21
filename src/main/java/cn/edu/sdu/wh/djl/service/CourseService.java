package cn.edu.sdu.wh.djl.service;

import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.domain.User;
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
     * @param currentUser
     * @return 课程ID
     */
    long addCourse(CourseAddRequest courseAddRequest, User currentUser);

    /**
     * 修改课程
     * @param courseUpdateRequest
     * @param currentUser
     * @return
     */
    int updateCourse(CourseUpdateRequest courseUpdateRequest, User currentUser);

    /**
     * 删除课程
     * @param id 课程id
     * @param currentUser
     * @return
     */
    boolean deleteCourse(long id, User currentUser);
}
