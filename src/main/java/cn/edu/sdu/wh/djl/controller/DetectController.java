package cn.edu.sdu.wh.djl.controller;

import cn.edu.sdu.wh.djl.common.BaseResponse;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.common.ResultUtils;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.service.DetectService;
import cn.edu.sdu.wh.djl.service.UserService;
import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

    public static final List<String> VIDEOS = Arrays.asList("mp4", "mkv");

    public static final List<String> IMAGES = Arrays.asList("jpg", "jpeg", "png", "bmp");
    @Resource
    private DetectService detectService;


    /**
     * 上传图片 检测图片里人数
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/detectHead")
    public BaseResponse<JSONArray> detectHead(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        // 检查是否登录
        userService.getCurrentUser(request);
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "上传文件为空，请确认文件");
        }
        JSONArray res = detectService.detectHead(file);

        return ResultUtils.success(res);
    }

    /**
     * 上传图片检测抬头率
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/detectUpDown")
    public BaseResponse<JSONArray> detectUpDown(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        // 检查是否登录
        userService.getCurrentUser(request);
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "上传文件为空，请确认文件");
        }
        JSONArray res = detectService.detectUpDown(file);

        return ResultUtils.success(res);
    }


    /**
     * 处理视频文件
     */
    @PostMapping("/detectMp4")
    public BaseResponse<String> detectMp4(@RequestParam(value = "file", required = true) MultipartFile file,
                                          @RequestParam(value = "courseId", required = true) Long courseId,
                                          @RequestParam(value = "capacity", required = true) int capacity,
                                          HttpServletRequest request) throws IOException {
        // 检查是否登录
        userService.getCurrentUser(request);
        if (file == null) {
            throw new BusinessException(ErrorCode.PARAM_NULL_ERROR, "上传文件为空，请确认文件");
        }
        detectService.detectMp4(file, courseId, capacity);
        return ResultUtils.success("分析视频数据已完成！");
    }


    /**
     * 上传文件接口
     *
     * @param file    文件
     * @param request 请求
     * @return 临时文件path
     */
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
