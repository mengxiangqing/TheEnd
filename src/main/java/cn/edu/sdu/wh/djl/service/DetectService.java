package cn.edu.sdu.wh.djl.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;

/**
 * @author 蒙西昂请 创建于：2022/11/10 15:45:49
 */

@Service
public interface DetectService {


    Json postFlask(File file, String detectUrl);


    Json detect(MultipartFile file, String detectUrl);


    Json detectHead(MultipartFile file);

    Json detectUpDown(MultipartFile file);


}
