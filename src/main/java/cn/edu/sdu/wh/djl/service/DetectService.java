package cn.edu.sdu.wh.djl.service;

import com.alibaba.fastjson2.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author 蒙西昂请 创建于：2022/11/10 15:45:49
 */

@Service
public interface DetectService {


    JSONArray postFlask(File file, String detectUrl);


    JSONArray detect(MultipartFile file, String detectUrl);


    JSONArray detectHead(MultipartFile file);

    JSONArray detectUpDown(MultipartFile file);


    void detectMp4(MultipartFile file, Long courseId, int capacity);
}
