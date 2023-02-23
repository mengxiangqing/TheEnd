package cn.edu.sdu.wh.djl.model.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 蒙西昂请 创建于：2023/2/23 3:21:46
 */
@Data
public class ClassRoomSearchRequest {
    /**
     * 教室详细名字
     */
    private String roomName;



    /**
     * 所在教学楼
     */
    private String[] address;

    /**
     * 教师状态，0-正常，1-有课，2-教室关闭
     */
    private Integer roomStatus;


    /**
     * 空座率
     */
    private Integer seatRate;

    private String sort;
    private int current;
    private int pageSize;
}
