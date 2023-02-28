package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.mapper.ClassroomMapper;
import cn.edu.sdu.wh.djl.model.domain.Classroom;
import cn.edu.sdu.wh.djl.model.request.ClassRoomSearchRequest;
import cn.edu.sdu.wh.djl.model.request.SetRoomStatusRequest;
import cn.edu.sdu.wh.djl.service.ClassroomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author 蒙西昂请
 * @description 针对表【classroom】的数据库操作Service实现
 * @createDate 2023-02-23 00:50:44
 */
@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom>
        implements ClassroomService {


    @Override
    public Page<Classroom> searchClassRoom(ClassRoomSearchRequest classRoomSearchRequest) {

        QueryWrapper<Classroom> queryWrapper = new QueryWrapper<>();
        // 查询教室名
        if (StringUtils.isNotBlank(classRoomSearchRequest.getRoomName())) {
            queryWrapper.like("room_name", classRoomSearchRequest.getRoomName());
        }
        // 查询 空座率大于 某某 的教室
        if (classRoomSearchRequest.getSeatRate() != null) {
            // 校验参数
            Integer inputRate = classRoomSearchRequest.getSeatRate();
            // 等于666时，就不筛选
            if (inputRate != 666) {
                if (inputRate < 0 || inputRate > 100) {
                    throw new BusinessException(ErrorCode.PARAM_ERROR);
                }
                if (inputRate < 100) {
                    queryWrapper.ge("seat_rate", inputRate);
                    queryWrapper.lt("seat_rate", inputRate + 25);
                } else {
                    queryWrapper.eq("seat_rate", inputRate);
                }
            }

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

        Page<Classroom> page = new Page<>(classRoomSearchRequest.getCurrent(), classRoomSearchRequest.getPageSize());
        return this.page(page, queryWrapper);
        // return this.list(queryWrapper);

    }

    @Override
    public boolean setRoomStatus(SetRoomStatusRequest setRoomStatusRequest) {
        Classroom classroom = new Classroom();
        classroom.setId(setRoomStatusRequest.getId());
        classroom.setRoomStatus(setRoomStatusRequest.getRoomStatus());

         return this.updateById(classroom);

    }
}




