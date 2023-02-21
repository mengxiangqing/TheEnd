package cn.edu.sdu.wh.djl.model.request;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author 蒙西昂请 创建于：2022/10/9 23:15:26
 */
@Data
public class UserUpdateRequest implements Serializable {
    private static final long serialVersionUID = -3314497645445105581L;

    /**
     * 用户唯一id，不可变，前端必须传
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户账号，管理员不可更改
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
     * 0-普通成员，1-管理员，2-超级管理员(可提拔管理员)
     */
    private Integer userRole;

    /**
     * 个人简介
     */
    private String profile;

}
