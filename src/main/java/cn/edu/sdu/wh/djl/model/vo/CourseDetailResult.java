package cn.edu.sdu.wh.djl.model.vo;

import lombok.Data;

/**
 * @author 蒙西昂请 创建于：2023/2/25 2:57:49
 */

@Data
public class CourseDetailResult {

    // private Long id;

    private long courseId;
    // 最近一讲课堂指标

    private Double latestUpRate;
    private Double latestAttendRate;
    private Double latestFrontRate;
    // 本月平均指标

    private Double averageUpRateThisMonth;
    private Double averageAttendRateThisMonth;
    private Double averageFrontRateThisMonth;
    // 上月平均指标

    private Double averageUpRateLastMonth;
    private Double averageAttendRateLastMonth;
    private Double averageFrontRateLastMonth;
    // 课程平均指标

    private Double averageUpRate;
    private Double averageAttendRate;
    private Double averageFrontRate;

    // 该课程每一讲的平均指标

    private String courseData;

    private String averageData;

}
