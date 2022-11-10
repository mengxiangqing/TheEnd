package cn.edu.sdu.wh.djl.service.detect.utils;

import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

/**
 * @author 蒙西昂请 创建于：2022/11/6 16:16
 */
public class ImageUtil {
    public static Image mat2Image(Mat mat) {
        return ImageFactory.getInstance().fromImage(HighGui.toBufferedImage(mat));
    }

}
