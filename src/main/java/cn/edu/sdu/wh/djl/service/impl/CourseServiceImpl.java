package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.mapper.CourseMapper;
import cn.edu.sdu.wh.djl.mapper.SingleClassMapper;
import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.domain.SingleClass;
import cn.edu.sdu.wh.djl.model.domain.User;
import cn.edu.sdu.wh.djl.model.request.CourseAddRequest;
import cn.edu.sdu.wh.djl.model.request.CourseSearchRequest;
import cn.edu.sdu.wh.djl.model.request.CourseUpdateRequest;
import cn.edu.sdu.wh.djl.model.vo.Rates;
import cn.edu.sdu.wh.djl.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    SingleClassMapper singleClassMapper;

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
        return courseMapper.updateById(newCourse);
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

    @Override
    public List<Course> searchCourses(CourseSearchRequest courseSearchRequest) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        //添加课程名搜索
        if (StringUtils.isNotBlank(courseSearchRequest.getCourseName())) {
            queryWrapper.like("course_name", courseSearchRequest.getCourseName());
        }
        //添加课程号搜索
        if (StringUtils.isNotBlank(courseSearchRequest.getCourseNumber())) {
            queryWrapper.like("course_number", courseSearchRequest.getCourseNumber());
        }
        // 添加根据创建时间排序条件
        if (!courseSearchRequest.getSort().isEmpty()) {
            String sortOps = courseSearchRequest.getSort().get("createTime");
            if ("ascend".equals(sortOps)) {
                queryWrapper.orderByAsc("create_time");
            } else {
                queryWrapper.orderByDesc("create_time");
            }
        }
        //添加过滤条件
        // if (!courseSearchRequest.getFilter().isEmpty()) {
        //     List<String> status = courseSearchRequest.getFilter().get("userStatus");
        //     List<String> roles = courseSearchRequest.getFilter().get("userRole");
        //     if (status != null && !status.isEmpty()) {
        //         List<Long> statusNum = status.stream().map(Long::valueOf).collect(Collectors.toList());
        //         queryWrapper.in("user_status", statusNum);
        //     }
        //     if (roles != null && !roles.isEmpty()) {
        //         List<Long> rolesNum = roles.stream().map(Long::valueOf).collect(Collectors.toList());
        //         queryWrapper.in("user_role", rolesNum);
        //     }
        // }

        List<Course> courseList = this.list(queryWrapper);
        courseList.forEach(course -> {
            // 获取课程的每一节课
            QueryWrapper<SingleClass> classQueryWrapper = new QueryWrapper<>();
            classQueryWrapper.eq("course_id", course.getId());
            classQueryWrapper.orderByAsc("create_time");
            // 数据库发送请求
            List<SingleClass> singleClasses = singleClassMapper.selectList(classQueryWrapper);
            // 计算每一节课的平均抬头率、出勤率、前排率
            double aveUpRate = 0.0;
            int upRateCount = 0;
            int attendRateCount = 0;
            int frontRateCount = 0;
            double aveAttendRate = 0.0;
            double aveFrontRate = 0.0;

            for (SingleClass singleClass : singleClasses) {
                try {
                    List<Rates> dataList = new ObjectMapper().readValue(singleClass.getUpRates(), new TypeReference<List<Rates>>() {
                    });
                    aveUpRate += dataList.stream().mapToDouble(Rates::getRate).sum();
                    upRateCount += dataList.size();

                    dataList = new ObjectMapper().readValue(singleClass.getAttendRates(), new TypeReference<List<Rates>>() {
                    });
                    aveAttendRate += dataList.stream().mapToDouble(Rates::getRate).sum();
                    attendRateCount += dataList.size();

                    dataList = new ObjectMapper().readValue(singleClass.getFrontRates(), new TypeReference<List<Rates>>() {
                    });
                    aveFrontRate += dataList.stream().mapToDouble(Rates::getRate).sum();
                    frontRateCount += dataList.size();

                } catch (JsonProcessingException e) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "序列化JSON数据异常");
                }
            }

            course.setClasses(singleClasses);
            course.setAverageUpRate(aveUpRate/upRateCount);
            course.setAverageAttendRate(aveAttendRate/attendRateCount);
            course.setAverageFrontRate(aveFrontRate/frontRateCount);
        });
        return courseList;

    }
}




