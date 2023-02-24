package cn.edu.sdu.wh.djl.controller;

import cn.edu.sdu.wh.djl.common.BaseResponse;
import cn.edu.sdu.wh.djl.common.ResultUtils;
import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.request.CourseSearchRequest;
import cn.edu.sdu.wh.djl.model.vo.SingClassResult;
import cn.edu.sdu.wh.djl.service.SingleClassService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 蒙西昂请 创建于：2023/2/23 0:55:02
 */
@RestController
@RequestMapping("/class")
@Slf4j
@CrossOrigin(value = {"http://127.0.0.1:3000", "http://127.0.0.1:3001", "http://localhost:3000", "http://localhost:8000"}, allowCredentials = "true")
public class SingleClassController {

    @Resource
    private SingleClassService singleClassService;

    /**
     * 获取单讲课的课堂数据
     */
    @PostMapping("/getRate")
    public BaseResponse<SingClassResult> getRate(@RequestBody long singleClassId, @RequestBody long courseId, HttpServletRequest request) {


        SingClassResult result = new SingClassResult();

        return ResultUtils.success(result);
    }

}
