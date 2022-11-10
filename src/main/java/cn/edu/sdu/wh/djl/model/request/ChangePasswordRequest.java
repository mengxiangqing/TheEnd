package cn.edu.sdu.wh.djl.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 蒙西昂请 创建于：2022/10/14 21:53:03
 */
@Data
public class ChangePasswordRequest implements Serializable {
    private static final long serialVersionUID = 5276266697035532070L;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
