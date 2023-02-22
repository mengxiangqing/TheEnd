package cn.edu.sdu.wh.djl.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName single_class
 */
@TableName(value = "single_class")
@Data
public class SingleClass implements Serializable {
    /**
     * 单节课id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程ID
     */
    @TableField(value = "course_id")
    private Long courseId;

    /**
     * 授课教师ID
     */
    @TableField(value = "teacher_id")
    private Long teacherId;

    /**
     * 上课时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 下课时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 抬头率列表
     */
    @JSONField(name = "up_rates")
    @TableField(value = "up_rates")
    private String upRates;

    /**
     * 到课率列表
     */

    @JSONField(name = "attend_rates")
    @TableField(value = "attend_rates")
    private String attendRates;

    /**
     * 前排率列表
     */
    @JSONField(name = "front_rates")
    @TableField(value = "front_rates")
    private String frontRates;

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
     *
     */
    @TableField(value = "up_rate")
    private Double upRate;

    /**
     *
     */
    @TableField(value = "attend_rate")
    private Double attendRate;

    /**
     *
     */
    @TableField(value = "front_rate")
    private Double frontRate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}