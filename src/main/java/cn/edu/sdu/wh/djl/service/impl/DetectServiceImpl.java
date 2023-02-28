package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.model.domain.Course;
import cn.edu.sdu.wh.djl.model.domain.SingleClass;
import cn.edu.sdu.wh.djl.service.CourseService;
import cn.edu.sdu.wh.djl.service.DetectService;
import cn.edu.sdu.wh.djl.service.SingleClassService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static cn.edu.sdu.wh.djl.controller.DetectController.IMAGES;

/**
 * @author 蒙西昂请 创建于：2022/11/10 15:45:49
 */
@Slf4j
@Service
public class DetectServiceImpl implements DetectService {


    @Resource
    private SingleClassService singleClassService;

    @Resource
    private CourseService courseService;

    @Value("#{'${detect.headUrl}'}")
    private String detectHeadUrl;
    @Value("#{'${detect.upDownUrl}'}")
    private String detectUpDownUrl;


    /**
     * @param file 图片
     * @return 参考链接：https://juejin.cn/post/7039891760452993037
     */
    @Override
    public JSONArray postFlask(File file, String detectUrl) {

        RestTemplate restTemplate = new RestTemplate();
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        //设置请求体，注意是LinkedMultiValueMap
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("image", new FileSystemResource(file));

        //封装请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

        return JSONArray.parseArray(restTemplate.postForObject(detectUrl, files, Json.class).value());
    }


    @Override
    public JSONArray detect(MultipartFile file, String detectUrl) {
        JSONArray res;
        long t1 = System.currentTimeMillis();
        try {
            // MultipartFile 转 File
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String[] filename = originalFilename.split("\\.");
            String suffix = filename[1].toLowerCase();
            if (!IMAGES.contains(suffix)) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "请输入图片");
            }
            File dst = File.createTempFile(filename[0], '.' + filename[1]);
            file.transferTo(dst);
            // 向推理API发请求
            res = this.postFlask(dst, detectUrl);
            log.info(String.format("推理成功，共耗时:%.3fs", (System.currentTimeMillis() - t1) / 1000.0));
            // 删除临时文件
            dst.deleteOnExit();

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件转换异常");
        }
        return res;
    }


    @Override
    public JSONArray detectHead(MultipartFile file) {
        return this.detect(file, detectHeadUrl);
    }

    @Override
    public JSONArray detectUpDown(MultipartFile file) {
        return this.detect(file, detectUpDownUrl);
    }

    @Override
    public void detectMp4(MultipartFile file, Long courseId, int capacity) {
        int imageCount = 0;
        int numofi = 1;
        int timeStamp = 5;
        try {
            //1. 将上传的视频文件分割成图片文件
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file.getInputStream());
            grabber.start();
            Frame frame;
            //1.1 每隔五分钟取一帧画面
            long timestamp = timeStamp * 60 * 1000 * 1000;
            while (null != (frame = grabber.grab())) {
                // 判断当前帧的时间戳是否超过上一帧时间戳加上间隔时间
                if (frame.image != null) {
                    // 取出当前帧的图像数据
                    Java2DFrameConverter converter = new Java2DFrameConverter();
                    BufferedImage image = converter.convert(frame);
                    //1.2 保存图像数据
                    String filePath = "image" + imageCount + ".jpg";
                    File imageFile = new File(filePath);
                    log.info("分割图片成功: " + filePath);
                    ImageIO.write(image, "jpg", imageFile);
                    numofi++;
                    grabber.setTimestamp(timestamp * numofi);
                    imageCount++;
                }
            }
            grabber.stop();
            // 2 保存这节课的指标列表
            double[] attendRates = new double[imageCount];
            double[] upRates = new double[imageCount];
            double[] frontRates = new double[imageCount];
            JSONArray upRateJsonArray = new JSONArray();
            JSONArray attendRateJsonArray = new JSONArray();
            JSONArray frontRateJsonArray = new JSONArray();

            //2.1 将图片文件发送到Flask服务器进行处理
            for (int i = 0; i < imageCount; i++) {
                File imageFile = new File("image" + i + ".jpg");
                MockMultipartFile multipartFile = new MockMultipartFile("file", imageFile.getName(), null, new FileInputStream(imageFile));
                if (imageFile.exists()) {
                    //2.2 获取教室内人数
                    JSONArray detectHeadResult = this.detectHead(multipartFile);
                    // 2.2 计算出勤率
                    attendRates[i] = (double) detectHeadResult.size() / capacity * 100.0;
                    // 2.2 存入详细数据
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("time", timeStamp * i);
                    jsonObject.put("rate", attendRates[i]);
                    attendRateJsonArray.add(jsonObject);
                    //2.3 获取抬头低头人数
                    JSONArray upDownResult = this.detectUpDown(multipartFile);
                    int up = 0;
                    int down = 0;
                    for (Object o : upDownResult) {
                        String name = (String) ((JSONObject) o).get("name");
                        if ("up".equals(name)) {
                            up++;
                        } else if ("down".equals(name)) {
                            down++;
                        }
                    }
                    // 2.3 计算抬头率
                    upRates[i] = (double) up / (up + down) * 100.0;
                    jsonObject = new JSONObject();
                    jsonObject.put("time", timeStamp * i);
                    jsonObject.put("rate", upRates[i]);
                    upRateJsonArray.add(jsonObject);
                    // TODO 计算前排率,现为假数据
                    JSONArray frontResult = this.detectUpDown(multipartFile);
                    frontRates[i] = (double) up / (up + down) * 100.0;
                    jsonObject = new JSONObject();
                    jsonObject.put("time", timeStamp * i);
                    jsonObject.put("rate", upRates[i]);
                    frontRateJsonArray.add(jsonObject);


                    // 删除图片文件
                    imageFile.delete();
                }
            }

            // 2.4 更新单讲课表
            SingleClass singleClass = new SingleClass();
            singleClass.setCourseId(courseId);

            singleClass.setUpRates(upRateJsonArray.toString());
            singleClass.setUpRate(Arrays.stream(upRates).sum() / imageCount);

            singleClass.setAttendRates(attendRateJsonArray.toString());
            singleClass.setAttendRate(Arrays.stream(attendRates).sum() / imageCount);

            singleClass.setFrontRates(frontRateJsonArray.toString());
            singleClass.setFrontRate(Arrays.stream(frontRates).sum() / imageCount);

            singleClass.setStartTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            singleClass.setEndTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            singleClassService.save(singleClass);

            // 2.5 更新这门课程表
            QueryWrapper<SingleClass> singleClassQueryWrapper = new QueryWrapper<>();
            singleClassQueryWrapper.eq("course_id", courseId);
            List<SingleClass> singleClasses = singleClassService.list(singleClassQueryWrapper);
            double aveUpRate = singleClasses.stream().mapToDouble(SingleClass::getUpRate).sum() / singleClasses.size();
            double aveAttendRate = singleClasses.stream().mapToDouble(SingleClass::getAttendRate).sum() / singleClasses.size();
            double aveFrontRate = singleClasses.stream().mapToDouble(SingleClass::getFrontRate).sum() / singleClasses.size();
            Course course = new Course();
            course.setId(courseId);
            course.setAverageUpRate(aveUpRate);
            course.setAverageAttendRate(aveAttendRate);
            courseService.saveOrUpdate(course);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

    }


}
