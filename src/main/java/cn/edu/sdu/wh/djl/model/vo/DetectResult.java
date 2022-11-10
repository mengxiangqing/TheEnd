package cn.edu.sdu.wh.djl.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 蒙西昂请 创建于：2022/11/8 15:33:25
 */
@Data
public class DetectResult implements Serializable {
    private static final long serialVersionUID = 6521206966066043407L;
    /**
     * 总人数
     */
    private Integer headCount;

    private Integer upCount;

    private Integer downCount;

    private Double upRate;

    private String resultJson;


}
