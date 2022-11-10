package cn.edu.sdu.wh.djl.service.detect;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import java.io.File;

/**
 * @author 蒙西昂请 创建于：2022/11/10 15:45:49
 */
@Data
@Slf4j
@Service
public class PostFlask {


    /**
     * @param file 图片
     * @return // 作者：阿豪_mike
     * 链接：https://juejin.cn/post/7039891760452993037
     * 来源：稀土掘金
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    public Json postFlask(File file) {
        String url = "http://10.190.0.30:2580/yolov5sHead";
        RestTemplate restTemplate = new RestTemplate();

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        //设置请求体，注意是LinkedMultiValueMap
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("image", new FileSystemResource(file));

        //封装请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

        return restTemplate.postForObject(url, files, Json.class);
    }

}
