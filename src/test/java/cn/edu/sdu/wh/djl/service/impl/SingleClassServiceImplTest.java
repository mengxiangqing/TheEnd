package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.service.SingleClassService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 蒙西昂请 创建于：2023/2/25 8:20:26
 */
@SpringBootTest
class SingleClassServiceImplTest {
    @Resource
    private SingleClassService singleClassService;

    @Test
    void getSingleClassDetail() {
        long singleClassId = 2;
        long courseId = 1;
        singleClassService.getSingleClassDetail(singleClassId, courseId);
    }
}