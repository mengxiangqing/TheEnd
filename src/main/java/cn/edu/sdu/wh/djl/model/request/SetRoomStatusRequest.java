package cn.edu.sdu.wh.djl.model.request;

import lombok.Data;

/**
 * @author 蒙西昂请 创建于：2023/3/1 1:09:49
 */
@Data
public class SetRoomStatusRequest {
    long id;
    int roomStatus;

}
