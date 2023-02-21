package cn.edu.sdu.wh.djl.model.vo;

import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.domain.User;
import lombok.Data;

import java.util.List;

/**
 * @author 蒙西昂请 创建于：2023/2/21 17:04:51
 */
@Data
public class Teacher {

    /**
     * 用户信息
     */
    private User userInfo;

    /**
     * 教的课列表
     */
    private List<Course> teachersCourse;
    /**
     * 平均抬头率
     */
    private Double averageUpRate;
    /**
     * 平均到课率
     */
    private Double averageAttendRate;
    /**
     * 平均前排率
     */
    private Double averageFrontRate;
}
