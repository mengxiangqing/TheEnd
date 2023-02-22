package cn.edu.sdu.wh.djl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蒙西昂请 创建于：2023/2/23 0:55:02
 */
@RestController
@RequestMapping("/class")
@Slf4j
@CrossOrigin(value = {"http://127.0.0.1:3000", "http://127.0.0.1:3001",
        "http://localhost:3000", "http://localhost:8000"}, allowCredentials = "true")
public class SingleClassController {



}
