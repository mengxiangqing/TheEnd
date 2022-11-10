package cn.edu.sdu.wh.djl.controller;

import ai.djl.modality.cv.BufferedImageFactory;
import ai.djl.modality.cv.Image;
import cn.edu.sdu.wh.djl.common.BaseResponse;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.common.ResultUtils;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.model.vo.DetectResult;
import cn.edu.sdu.wh.djl.service.UserService;
import cn.edu.sdu.wh.djl.service.detect.DetectService;
import cn.edu.sdu.wh.djl.service.detect.PostFlask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author 蒙西昂请 创建于：2022/11/7 22:39:08
 */

@RestController
@RequestMapping("/detect")
@Slf4j
public class DetectController {
    @Resource
    private UserService userService;
    @Resource
    private DetectService detectService;

    @Resource
    private PostFlask postFlask;

    @PostMapping("/image")
    public BaseResponse<Json> detectImage(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        // 检查是否登录
        userService.getCurrentUser(request);
        // DetectResult result = null;
        Json res = null;
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "上传文件为空，请确认文件");
        }
        long t1 = System.currentTimeMillis();
        // Image image = new BufferedImageFactory().fromInputStream(file.getInputStream());
        // result = detectService.doDetect(image);
        File dst = null;
        try {
            // MultipartFile 转 File
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String[] filename = originalFilename.split("\\.");
            String suffix = filename[1].toLowerCase();
            // List<String> VIDEOS = Arrays.asList("mp4", "mkv");
            List<String> images = Arrays.asList("jpg", "jpeg", "png", "bmp");
            if(!images.contains(suffix)){
                throw new BusinessException(ErrorCode.PARAM_ERROR, "请输入图片");
            }
            dst = File.createTempFile(filename[0], '.'+filename[1]);
            file.transferTo(dst);
            // 向推理API发请求
            res = postFlask.postFlask(dst);
            log.info(String.format("推理成功，共耗时:%.3fs", (System.currentTimeMillis() - t1) / 1000.0));
            // 删除临时文件
            dst.deleteOnExit();

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件转换异常");
        }

        return ResultUtils.success(res);
    }

    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        // 检查是否登录
        userService.getCurrentUser(request);
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "上传文件为空，请确认文件");
        }

        File targetFile = null;
        //返回存储路径
        String url = "";

        // 获取文件名以及后缀
        String fileName = null;
        try {
            fileName = file.getOriginalFilename();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "获取文件名失败");
        }
        if (StringUtils.isNotBlank(fileName)) {
            // 存储路径
            String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/upload/imgs/";
            //文件存储位置
            String path = request.getSession().getServletContext().getRealPath("upload/imgs");
            //文件后缀
            String fileF = fileName.substring(fileName.lastIndexOf("."));
            //新的文件名
            fileName = System.currentTimeMillis() + "_" + new Random().nextInt(1000) + fileF;

            // 先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileNameStr = sdf.format(new Date());
            // 获取文件夹路径
            File fileDir = new File(path + "/" + fileNameStr);
            // 如果文件夹不存在则创建
            if (!fileDir.exists() && !fileDir.isDirectory()) {
                // 注意创建多级目录mkdirs
                boolean mkdirRes = fileDir.mkdirs();
                if (!mkdirRes) {
                    log.error("创建文件夹失败");
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建文件夹失败");
                }
            }
            //    将图片存入文件夹
            targetFile = new File(fileDir, fileName);
            try {
                file.transferTo(targetFile);
                url = returnUrl + fileNameStr + "/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        log.info("上传成功！");
        return ResultUtils.success(url);
    }
}
