package cn.edu.sdu.wh.djl.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class UserSearchRequest implements Serializable {

    private static final long serialVersionUID = -5939492830118922451L;
    private String userAccount;
    private String username;
    private Map<String, List<String>> filter;
    private Map<String, String> sort;
    private int current;
    private int pageSize;
}
