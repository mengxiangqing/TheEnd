package cn.edu.sdu.wh.djl.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName choose_class
 */
@TableName(value ="choose_class")
@Data
public class ChooseClass implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "student_id")
    private Long studentId;

    /**
     * 
     */
    @TableField(value = "course_id")
    private Long courseId;

    /**
     * 
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 
     */
    @TableField(value = "update_user")
    private Long updateUser;

    /**
     * 逻辑删除，默认0
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    /**
     * 
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}