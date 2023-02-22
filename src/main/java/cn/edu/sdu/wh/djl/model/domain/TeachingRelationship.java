package cn.edu.sdu.wh.djl.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName teaching_relationship
 */
@TableName(value ="teaching_relationship")
@Data
public class TeachingRelationship implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "teacher_id")
    private Long teacherId;

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
     * 是否删除，默认值为0
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

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
     * 
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}