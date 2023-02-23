package cn.edu.sdu.wh.djl.controller;

import cn.edu.sdu.wh.djl.common.BaseResponse;
import cn.edu.sdu.wh.djl.common.ResultUtils;
import cn.edu.sdu.wh.djl.model.domain.Classroom;
import cn.edu.sdu.wh.djl.model.request.ClassRoomSearchRequest;
import cn.edu.sdu.wh.djl.service.ClassroomService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 蒙西昂请 创建于：2023/2/23 3:16:18
 */

@RestController
@RequestMapping("/room")
@Slf4j
@CrossOrigin(value = {"http://127.0.0.1:3000", "http://127.0.0.1:3001",
        "http://localhost:3000", "http://localhost:8000"}, allowCredentials = "true")
public class RoomController {
    @Resource
    private ClassroomService classroomService;

    @PostMapping("/search")
    public BaseResponse<Page<Classroom>> searchClassRoom(@RequestBody ClassRoomSearchRequest classRoomSearchRequest, HttpServletRequest request) {

        Page<Classroom> collect = classroomService.searchClassRoom(classRoomSearchRequest);
        return ResultUtils.success(collect);
    }

}