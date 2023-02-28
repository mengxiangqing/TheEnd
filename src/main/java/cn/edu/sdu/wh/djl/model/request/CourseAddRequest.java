package cn.edu.sdu.wh.djl.model.request;

import lombok.Data;

/**
 * @author 蒙西昂请 创建于：2023/2/21 16:14:28
 */
@Data
public class CourseAddRequest {

    /**
     * 课程号
     */
    private String courseNumber;

    /**
     * 课程名字
     */
    private String courseName;

    /**
     * 授课学期
     */
    private String teachingTime;

    /**
     * 所属单位
     */
    private String college;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 授课教师ID
     */
    private Long teacher;

    /**
     * 授课教室
     */
    private Long classroomId;

    /**
     * 选课人数
     */
    private Integer chooseNum;

    /**
     * 起始周
     */
    private Integer startWeek;

    /**
     * 结束周
     */
    private Integer endWeek;

    /**
     * 备注
     */
    private String remark;

}
