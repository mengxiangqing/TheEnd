package cn.edu.sdu.wh.djl.service.impl;

import cn.edu.sdu.wh.djl.mapper.SingleClassMapper;
import cn.edu.sdu.wh.djl.model.domain.SingleClass;
import cn.edu.sdu.wh.djl.model.vo.SingClassResult;
import cn.edu.sdu.wh.djl.service.SingleClassService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒙西昂请
 * @description 针对表【single_class】的数据库操作Service实现
 * @createDate 2023-02-23 00:48:56
 */
@Service
public class SingleClassServiceImpl extends ServiceImpl<SingleClassMapper, SingleClass>
        implements SingleClassService {

    @Override
    public SingClassResult getSingleClassDetail(long singleClassId, long courseId) {


        SingClassResult result = new SingClassResult();
        result.setId(singleClassId);
        result.setCourseId(courseId);
        QueryWrapper<SingleClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        queryWrapper.orderByAsc("course_id");
        List<SingleClass> singleClassList = this.list(queryWrapper);
        // 如果数据不为空
        if (singleClassList != null && singleClassList.size() != 0) {
            for (int i = 0; i < singleClassList.size(); i++) {
                //找到第几讲
                if (i == singleClassId - 1) {
                    // 获取该节课的数据列表, 获取的都是json字符串
                    String upRates = singleClassList.get(i).getUpRates();
                    JSONArray upRatesJson = JSONArray.parseArray(upRates);
                    String attendRates = singleClassList.get(i).getAttendRates();
                    JSONArray attendRatesJson = JSONArray.parseArray(attendRates);
                    String frontRates = singleClassList.get(i).getFrontRates();
                    JSONArray frontRatesJson = JSONArray.parseArray(frontRates);

                    List<JSONObject> jsonObjectList = new ArrayList<>();
                    for (Object item : upRatesJson) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("time", ((JSONObject) item).get("time"));
                        jsonObject.put("rate", ((JSONObject) item).get("rate"));
                        jsonObject.put("name", "抬头率");
                        jsonObjectList.add(jsonObject);
                    }
                    for (Object value : attendRatesJson) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("time", ((JSONObject) value).get("time"));
                        jsonObject.put("rate", ((JSONObject) value).get("rate"));
                        jsonObject.put("name", "出勤率");
                        jsonObjectList.add(jsonObject);
                    }
                    for (Object o : frontRatesJson) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("time", ((JSONObject) o).get("time"));
                        jsonObject.put("rate", ((JSONObject) o).get("rate"));
                        jsonObject.put("name", "前排率");
                        jsonObjectList.add(jsonObject);
                    }
                    result.setClassData(jsonObjectList.toString());
                    return result;
                }

            }
        }
        return null;
    }
}




