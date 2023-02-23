package cn.edu.sdu.wh.djl.service;

import cn.edu.sdu.wh.djl.model.domain.Classroom;
import cn.edu.sdu.wh.djl.model.request.ClassRoomSearchRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 蒙西昂请
* @description 针对表【classroom】的数据库操作Service
* @createDate 2023-02-23 00:50:44
*/
public interface ClassroomService extends IService<Classroom> {

    List<Classroom> searchClassRoom(ClassRoomSearchRequest classRoomSearchRequest);
}
