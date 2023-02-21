package cn.edu.sdu.wh.djl.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @author 蒙西昂请
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学工号
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 用户姓名
     */
    @TableField(value = "user_name")
    private String username;

    /**
     * 教职工职称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 入校年份
     */
    @TableField(value = "grade")
    private Integer grade;

    /**
     * 登录密码
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     * 用户角色：1-学生，2-教师，3-督导员，4-管理员
     */
    @TableField(value = "user_role")
    private Integer userRole;

    /**
     * 用户状态：0-正常，1-学生毕业，2-学生结业，3-学生肄业，4-教职工离职，5-教职工退休
     */
    @TableField(value = "user_status")
    private Integer userStatus;

    /**
     * 性别，0-女，1-男
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 出生年月
     */
    @TableField(value = "birth")
    private String birth;

    /**
     * 所属单位
     */
    @TableField(value = "college")
    private String college;

    /**
     * 所属班级
     */
    @TableField(value = "stu_class")
    private String stuClass;

    /**
     * 联系电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 电子邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建人，默认为0表示用户自己创建
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private Long updateUser;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}