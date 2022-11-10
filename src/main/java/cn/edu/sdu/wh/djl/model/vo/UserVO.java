package cn.edu.sdu.wh.djl.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息封装类（脱敏）
 * @author 蒙西昂请 创建于：2022/10/6 8:27:28
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -4496273847028594812L;
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 标签列表 json
     */
    private String tags;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;

    /**
     * 学号or工号
     */
    private String studentCode;

    /**
     * 个人简介
     */
    private String profile;

}
