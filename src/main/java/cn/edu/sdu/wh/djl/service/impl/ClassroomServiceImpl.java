package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.mapper.ClassroomMapper;
import cn.edu.sdu.wh.djl.model.domain.Classroom;
import cn.edu.sdu.wh.djl.model.request.ClassRoomSearchRequest;
import cn.edu.sdu.wh.djl.service.ClassroomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author 蒙西昂请
 * @description 针对表【classroom】的数据库操作Service实现
 * @createDate 2023-02-23 00:50:44
 */
@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom>
        implements ClassroomService {

    @Override
    public List<Classroom> searchClassRoom(ClassRoomSearchRequest classRoomSearchRequest) {

        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();
        // 查询教室名
        if (StringUtils.isNotBlank(classRoomSearchRequest.getRoomName())) {
            queryWrapper.like("room_name", classRoomSearchRequest.getRoomName());
        }
        // 查询 空座率大于 某某 的教室
        if (classRoomSearchRequest.getSeatRate() != null) {
            // 校验参数
            if (classRoomSearchRequest.getSeatRate() < 0 || classRoomSearchRequest.getSeatRate() > 100) {
                throw new BusinessException(ErrorCode.PARAM_ERROR);
            }
            queryWrapper.gt("seat_rate", classRoomSearchRequest.getSeatRate());
        }
        // 根据空座率排序
        if (classRoomSearchRequest.getSort() != null) {
            String sortOps = classRoomSearchRequest.getSort();
            if ("ascend".equals(sortOps)) {
                queryWrapper.orderByAsc("seat_rate");
            } else {
                queryWrapper.orderByDesc("seat_rate");
            }
        }

        // 筛选教室状态
        if (classRoomSearchRequest.getRoomStatus() != null) {
            Integer status = classRoomSearchRequest.getRoomStatus();
            queryWrapper.eq("room_status", status);
        }

        if (classRoomSearchRequest.getAddress() != null && classRoomSearchRequest.getAddress().length > 0) {
            // 筛选教学楼
            String[] address = classRoomSearchRequest.getAddress();
            // 根据教学楼名字
            queryWrapper.in("address", Arrays.asList(address));
        }


        return this.list(queryWrapper);

    }
}




