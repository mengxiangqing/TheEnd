package cn.edu.sdu.wh.djl.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求类
 *
 * @author meng
 */
@Data
public class UserRegisterRequest implements Serializable {

  private static final long serialVersionUID = -5939492830118922451L;
  private String userAccount;
  private String userPassword;
  private String checkPassword;
}
