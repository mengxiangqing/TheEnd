package cn.edu.sdu.wh.djl.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author 蒙西昂请
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rates {
    private int time;
    private double rate;

}
