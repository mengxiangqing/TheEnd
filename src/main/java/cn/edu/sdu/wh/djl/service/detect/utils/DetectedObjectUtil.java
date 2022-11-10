package cn.edu.sdu.wh.djl.service.detect.utils;

import ai.djl.modality.cv.output.BoundingBox;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author bjiang
 * @Description //TODO
 * @Date 2021/12/30 10:50
 * @Version 1.0
 */
public class DetectedObjectUtil {
    private static DetectedObjectUtil instance;
    public static DetectedObjectUtil me() {
        if (instance == null) {
            instance = new DetectedObjectUtil();
        }
        return instance;
    }

    /**
     * @Author bjiang
     * @Description //TODO 重构detection,对象后增加可能性
     * @Date 10:06 2021/12/31
     * @Version 1.0
     * @Param [detection]
     * @return ai.djl.modality.cv.output.DetectedObjects
     */
    public DetectedObjects getDetectedObjects(DetectedObjects detection){
        List<String> className=new ArrayList<>();
        List<Double> probability=new ArrayList<>();
        List<BoundingBox> boundingBoxes=new ArrayList<>();
        for (DetectedObjects.DetectedObject obj : detection.<DetectedObjects.DetectedObject>items()) {
            BoundingBox bbox = obj.getBoundingBox();
            Rectangle rectangle = bbox.getBounds();
            className.add(obj.getClassName()+" " + obj.getProbability());
            probability.add(obj.getProbability());
            Rectangle rectangleNew=new Rectangle(rectangle.getX(),rectangle.getY(),
                    rectangle.getWidth(),rectangle.getHeight());
            boundingBoxes.add(rectangleNew);
        }
        DetectedObjects detectionNew=new DetectedObjects(className,probability,boundingBoxes);
        return detectionNew;
    }
    /**
     * @Author bjiang
     * @Description //TODO 冒泡排序
     * @Date 8:28 2022/4/18
     * @Version 1.0
     * @Param
     * @return
     */

}
