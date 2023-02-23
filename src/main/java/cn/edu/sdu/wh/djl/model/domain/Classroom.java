package cn.edu.sdu.wh.djl.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName classroom
 */
@TableName(value ="classroom")
@Data
public class Classroom implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 教室详细名字
     */
    @TableField(value = "room_name")
    private String roomName;

    /**
     * 教室容量
     */
    @TableField(value = "capacity")
    private Integer capacity;

    /**
     * 现有人数
     */
    @TableField(value = "humans")
    private Integer humans;

    /**
     * 所在教学楼
     */
    @TableField(value = "address")
    private String address;

    /**
     * 教师状态，0-正常，1-有课，2-教室关闭
     */
    @TableField(value = "room_status")
    private Integer roomStatus;

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
     * 空座率
     */
    @TableField(value = "seat_rate")
    private Double seatRate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}