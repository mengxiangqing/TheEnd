package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.model.request.CourseAddRequest;
import cn.edu.sdu.wh.djl.model.request.CourseUpdateRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.service.CourseService;
import cn.edu.sdu.wh.djl.mapper.CourseMapper;
import org.springframework.stereotype.Service;

/**
 * @author 蒙西昂请
 * @description 针对表【course】的数据库操作Service实现
 * @createDate 2023-02-21 15:55:11
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {

    @Override
    public long addCourse(CourseAddRequest courseAddRequest) {
        return 0;
    }

    @Override
    public int updateCourse(CourseUpdateRequest courseUpdateRequest) {
        return 0;
    }
}




