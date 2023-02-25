package cn.edu.sdu.wh.djl.service;

import cn.edu.sdu.wh.djl.model.domain.SingleClass;
import cn.edu.sdu.wh.djl.model.vo.SingClassResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 蒙西昂请
* @description 针对表【single_class】的数据库操作Service
* @createDate 2023-02-23 00:48:56
*/
public interface SingleClassService extends IService<SingleClass> {

    SingClassResult getSingleClassDetail(long singleClassId, long courseId);
}
