package cn.edu.sdu.wh.djl.service.detect;

import ai.djl.Application;
import ai.djl.Device;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.BufferedImageFactory;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.DetectedObjects.DetectedObject;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.translator.YoloV5Translator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Translator;
import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import cn.edu.sdu.wh.djl.model.vo.DetectResult;
import cn.edu.sdu.wh.djl.service.detect.utils.DetectedObjectUtil;
import cn.edu.sdu.wh.djl.service.detect.utils.ImageUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author SDDX
 */
@Data
@Slf4j
@Service
public class DetectService {
    private static String fileName = "E:\\movie\\6_49234.mp4";
    private static String detectNames = "up_down.names";
    private static String modelNames = "up_down_bifpn.torchscript1088-1920.pt";
    private static ZooModel<Image, DetectedObjects> model;
    private static final List<String> VIDEOS = Arrays.asList("mp4", "mkv");
    private static final List<String> IMAGES = Arrays.asList("jpg", "jpeg", "png", "bmp");

    public DetectService() {
        log.info("加载opencv库");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Map<String, Object> arguments = new ConcurrentHashMap<>(10);
        //图片以640 宽度进行操作
        arguments.put("width", 1920);
        //图片以640高度进行操作
        arguments.put("height", 1088);
        //调整图片大小
        arguments.put("resize", true);
        //图片值编程0-1之间
        arguments.put("rescale", true);
        // arguments.put("normalize", true);

        //转换成张量
        // arguments.put("toTensor", false);
        //范围
        // arguments.put("range", "0,1");

        // 正态化
        // arguments.put("normalize", "true");

        // 小于该阈值不显示
        arguments.put("threshold", 0.55);
        // NMS阈值
        arguments.put("nmsThreshold", 0.7);

        // 模型转换器
        Translator<Image, DetectedObjects> translator =
                YoloV5Translator.builder(arguments)
                        .optSynsetArtifactName(detectNames)
                        .build();
        Criteria<Image, DetectedObjects> criteria =
                Criteria.builder()
                        .setTypes(Image.class, DetectedObjects.class)
                        .optApplication(Application.CV.INSTANCE_SEGMENTATION)
                        .optDevice(Device.cpu())
                        .optModelUrls(Objects.requireNonNull(DetectService.class.getResource("/yolov5s")).getPath())
                        .optModelName(modelNames)
                        .optTranslator(translator)
                        .optEngine("PyTorch")
                        .build();

        try {
            model = ModelZoo.loadModel(criteria);
        } catch (Exception e) {
            // e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    public void doDetect() {
        doDetect(fileName, detectNames, modelNames);
    }

    public DetectResult doDetect(Image image) {
        return detect(image, model);
    }

    public void doDetect(String fileName, String detectNames, String modelNames) {

        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf('.') + 1);
        // 处理视频流
        if (VIDEOS.contains(suffix.toLowerCase())) {

            VideoCapture cap = null;
            try {
                cap = new VideoCapture(fileName);
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "OpenCV内部错误");
            }
            //isOpened函数用来判断摄像头调用是否成功
            if (!cap.isOpened()) {
                //如果摄像头调用失败，输出错误信息
                log.error("调用视频流错误");
            } else {
                //创建一个输出帧
                Mat frame = new Mat();
                //read方法读取摄像头的当前帧
                boolean flag = cap.read(frame);
                while (flag) {
                    Image img = ImageUtil.mat2Image(frame);
                    detect(img, model);
                    // 展示结果
                    HighGui.imshow("yolov5", frame);
                    HighGui.waitKey(2);
                    flag = cap.read(frame);
                }
            }
        }
        // 处理单张图片
        else if (IMAGES.contains(suffix.toLowerCase())) {
            try {
                Image image = new BufferedImageFactory().fromFile(Paths.get(fileName));
                detect(image, model);
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "读取图片失败");
            }


        }
    }

    static Rect rect = new Rect();
    static Scalar upColor = new Scalar(0, 255, 0);
    static Scalar downColor = new Scalar(255, 0, 255);
    static Scalar color = upColor;

    /**
     * 预测单帧图片
     * <p>
     * // FileOutputStream out = new FileOutputStream("E:\\study\\test.jpeg");
     * // img.save(out, "jpeg");
     *
     * @param img   单帧
     * @param model 检测模型
     */
    public DetectResult detect(Image img, ZooModel<Image, DetectedObjects> model) {
        //Mat图片
        Mat m = new Mat((img).getHeight(), (img).getWidth(), CvType.CV_8UC3);
        Image duplicate = img.duplicate();
        DetectResult res = new DetectResult();
        long startTime = 0;
        int upCount = 0;
        int downCount = 0;
        long t1 = System.currentTimeMillis();
        try (Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
            // 将图片进行预测
            DetectedObjects results = predictor.predict(img);
            log.info(String.format("推理耗时:%.2fs", (System.currentTimeMillis() - t1) / 1000.0));
            res.setResultJson(results.toJson());
            saveBoundingBoxImage(img, results);
            startTime = System.currentTimeMillis();
            for (DetectedObject obj : results.<DetectedObject>items()) {
                BoundingBox bbox = obj.getBoundingBox();
                Rectangle rectangle = bbox.getBounds();
                String showText = String.format("%s: %.2f", obj.getClassName(), obj.getProbability());
                rect.x = (int) rectangle.getX();
                rect.y = (int) rectangle.getY();
                rect.width = (int) rectangle.getWidth();
                rect.height = (int) rectangle.getHeight();
                if ("down".equals(obj.getClassName())) {
                    downCount++;
                    color = downColor;
                } else if ("up".equals(obj.getClassName())) {
                    upCount++;
                    color = upColor;
                }
                // 画框
                Imgproc.rectangle(m, rect, color, 2);
                //画名字
                Imgproc.putText(m, showText,
                        new Point(rect.x, rect.y),
                        Imgproc.FONT_HERSHEY_COMPLEX,
                        rectangle.getWidth() / 200,
                        color);

            }

            Image image = ImageUtil.mat2Image(m);
            FileOutputStream out = new FileOutputStream("save.png");
            duplicate.save(out, "png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        double upRate = upCount / ((upCount + downCount) * 1.0) * 100;
        log.info(String.format("总人数：%d 抬头：%d 低头：%d 抬头率%.2f%%", upCount + downCount, upCount, downCount, upRate));
        log.info(String.format(" 帧率:%.2f", 1000.0 / (System.currentTimeMillis() - t1)));
        log.info(String.format("画图耗时:%.4fs %n", (System.currentTimeMillis() - startTime) / 1000.0));
        res.setHeadCount(downCount + upCount);
        res.setDownCount(downCount);
        res.setUpCount(upCount);
        res.setUpRate(upRate);
        return res;
    }

    /**
     * @Author bjiang
     * @Description //TODO 根据detection绘制图片，输出到 build/output
     * @Date 10:08 2021/12/31
     * @Version 1.0
     * @Param [img, detection]
     * @return void
     */
    private static void saveBoundingBoxImage(Image img, DetectedObjects detection)
            throws IOException {
        Path outputDir = Paths.get("build/output");
        Files.createDirectories(outputDir);
        DetectedObjects detectionNew= DetectedObjectUtil.me().getDetectedObjects(detection);
        img.drawBoundingBoxes(detectionNew);
        Path imagePath = outputDir.resolve("instances.png");
        img.save(Files.newOutputStream(imagePath), "png");
        System.out.println("Segmentation result image has been saved in"+detectionNew);
    }
}


