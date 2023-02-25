package cn.edu.sdu.wh.djl.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 蒙西昂请 创建于：2023/2/22 16:48:49
 */
@Data
public class CourseSearchRequest implements Serializable {

    private static final long serialVersionUID = -5939492830118922451L;
    private String courseNumber;
    private String courseName;
    private String college;
    private Long teacher;
    private Map<String, List<String>> filter;
    private Map<String, String> sort;
    private int current;
    private int pageSize;
}