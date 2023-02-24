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
import cn.edu.sdu.wh.djl.model.vo.CourseDetailResult;
import cn.edu.sdu.wh.djl.service.CourseService;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 蒙西昂请
 * @description 针对表【course】的数据库操作Service实现
 * @createDate 2023-02-21 15:55:11
 */
@Service
@Slf4j
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

        List<Course> courseList = this.list(queryWrapper);
        courseList.forEach(course -> {
            // 获取课程的每一节课
            QueryWrapper<SingleClass> classQueryWrapper = new QueryWrapper<>();
            classQueryWrapper.eq("course_id", course.getId());
            classQueryWrapper.orderByAsc("create_time");
            // 数据库发送请求
            List<SingleClass> singleClasses = singleClassMapper.selectList(classQueryWrapper);
            // 计算每一节课的平均抬头率、出勤率、前排率
            course.setClasses(singleClasses);
            course.setAverageUpRate(singleClasses.stream().mapToDouble(SingleClass::getUpRate).sum() / singleClasses.size());
            course.setAverageAttendRate(singleClasses.stream().mapToDouble(SingleClass::getUpRate).sum() / singleClasses.size());
            course.setAverageFrontRate(singleClasses.stream().mapToDouble(SingleClass::getFrontRate).sum() / singleClasses.size());
        });
        return courseList;

    }

    @Override
    public CourseDetailResult getCourseDetail(long courseId) {
        CourseDetailResult result = new CourseDetailResult();
        result.setCourseId(courseId);

        // 1.获取课程平均指标
        Course course = this.getById(courseId);
        result.setAverageUpRate(course.getAverageUpRate());
        result.setAverageAttendRate(course.getAverageAttendRate());
        result.setAverageFrontRate(course.getAverageFrontRate());

        QueryWrapper<SingleClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        List<SingleClass> singleClassList = singleClassMapper.selectList(queryWrapper);
        // 如果数据不为空
        if (singleClassList != null && singleClassList.size() != 0) {
            // 2.获取最近一堂课平均指标
            SingleClass latestSingleClass = singleClassList.stream().max(Comparator.comparingLong(SingleClass::getId)).get();
            result.setLatestUpRate(latestSingleClass.getUpRate());
            result.setLatestAttendRate(latestSingleClass.getAttendRate());
            result.setLatestFrontRate(latestSingleClass.getFrontRate());
            // 3.获取本月平均指标
            //  3.1获取当前月份
            YearMonth currentMonth = YearMonth.now();
            //  3.2过滤单个类列表，只保留本月的数据
            List<SingleClass> currentMonthClasses = singleClassList.stream()
                    .filter(singleClass -> YearMonth.from(singleClass.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).equals(currentMonth))
                    .collect(Collectors.toList());
            result.setAverageUpRateThisMonth(currentMonthClasses.stream().mapToDouble(SingleClass::getUpRate).sum() / currentMonthClasses.size());
            result.setAverageAttendRateThisMonth(currentMonthClasses.stream().mapToDouble(SingleClass::getAttendRate).sum() / currentMonthClasses.size());
            result.setAverageFrontRateThisMonth(currentMonthClasses.stream().mapToDouble(SingleClass::getFrontRate).sum() / currentMonthClasses.size());

            // 4.获取上月平均指标
            //  4.1获取当前月份的上一月
            YearMonth previousMonth = YearMonth.now().minusMonths(1);
            //  4.2过滤单个类列表，只保留上月的数据
            List<SingleClass> previousMonthClasses = singleClassList.stream()
                    .filter(singleClass -> YearMonth.from(singleClass.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).equals(previousMonth))
                    .collect(Collectors.toList());
            result.setAverageUpRateLastMonth(previousMonthClasses.stream().mapToDouble(SingleClass::getUpRate).sum() / previousMonthClasses.size());
            result.setAverageAttendRateLastMonth(previousMonthClasses.stream().mapToDouble(SingleClass::getAttendRate).sum() / previousMonthClasses.size());
            result.setAverageFrontRateLastMonth(previousMonthClasses.stream().mapToDouble(SingleClass::getFrontRate).sum() / previousMonthClasses.size());
            // 5.计算折线图需要的数据
            List<JSONObject> jsonList = new ArrayList<>();
            for (int i = 0; i < singleClassList.size(); i++) {
                SingleClass singleClass = singleClassList.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("time", i + 1);
                jsonObject.put("upRate", singleClass.getUpRate());
                jsonObject.put("upName", "抬头率");
                jsonObject.put("attendRate", singleClass.getAttendRate());
                jsonObject.put("attendName", "出勤率");
                jsonObject.put("frontRate", singleClass.getFrontRate());
                jsonObject.put("frontName", "前排率");
                jsonList.add(jsonObject);
            }
            result.setCourseData(jsonList.toString());
            log.debug("获取courseData: " + jsonList.toString());

            // 6. 计算雷达图需要的数据
            List<JSONObject> jsonListOfAve = new ArrayList<>();
            JSONObject upJsonObject = new JSONObject();
            upJsonObject.put("name", "听讲状态");
            upJsonObject.put("rate", result.getAverageUpRate());
            jsonListOfAve.add(upJsonObject);
            JSONObject attendJsonObject = new JSONObject();
            attendJsonObject.put("name", "出勤比例");
            attendJsonObject.put("rate", result.getAverageAttendRate());
            jsonListOfAve.add(attendJsonObject);
            JSONObject frontJsonObject = new JSONObject();
            frontJsonObject.put("name", "前排情况");
            frontJsonObject.put("rate", result.getAverageFrontRate());
            jsonListOfAve.add(frontJsonObject);

            result.setAverageData(jsonListOfAve.toString());
            System.out.println(result);
            return result;
        }


        return null;
    }
}




