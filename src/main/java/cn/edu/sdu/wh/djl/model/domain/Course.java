package cn.edu.sdu.wh.djl.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @TableName course
 */
@TableName(value = "course")
@Data
public class Course implements Serializable {
    /**
     * 课程ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程号
     */
    @TableField(value = "course_number")
    private String courseNumber;

    /**
     * 课程名字
     */
    @TableField(value = "course_name")
    private String courseName;

    /**
     * 授课学期
     */
    @TableField(value = "teaching_time")
    private String teachingTime;

    /**
     * 所属单位
     */
    @TableField(value = "college")
    private String college;

    /**
     * 联系电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 授课教师ID
     */
    @TableField(value = "teachers")
    private String teachers;

    /**
     * 授课教室
     */
    @TableField(value = "classroom_id")
    private Long classroomId;

    /**
     * 选课人数
     */
    @TableField(value = "choose_num")
    private Integer chooseNum;

    /**
     * 起始周
     */
    @TableField(value = "start_week")
    private Integer startWeek;

    /**
     * 结束周
     */
    @TableField(value = "end_week")
    private Integer endWeek;

    /**
     * 平均抬头率
     */
    @TableField(value = "average_up_rate")
    private Double averageUpRate;

    /**
     * 平均到课率
     */
    @TableField(value = "average_attend_rate")
    private Double averageAttendRate;

    /**
     * 平均前排率
     */
    @TableField(value = "average_front_rate")
    private Double averageFrontRate;

    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     *
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     *
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     *
     */
    @TableField(value = "update_user")
    private Long updateUser;

    /**
     *
     */
    @TableField(value = "remark")
    private String remark;

    /**
     *
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    /**
     * 这门课程的每一节课
     */
    @TableField(exist = false)
    private List<SingleClass> classes;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}