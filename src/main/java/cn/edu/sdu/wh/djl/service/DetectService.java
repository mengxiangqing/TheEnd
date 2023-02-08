package cn.edu.sdu.wh.djl.service;

import cn.edu.sdu.wh.djl.common.ErrorCode;
import cn.edu.sdu.wh.djl.exception.BusinessException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.IOException;

import static cn.edu.sdu.wh.djl.controller.DetectController.IMAGES;

/**
 * @author 蒙西昂请 创建于：2022/11/10 15:45:49
 */
@Data
@Slf4j
@Service
public class DetectService {

    @Value("#{'${detect.headUrl}'}")
    private String detectHeadUrl;
    @Value("#{'${detect.upDownUrl}'}")
    private String detectUpDownUrl;


    /**
     * @param file 图片
     * @return // 作者：阿豪_mike
     * 链接：https://juejin.cn/post/7039891760452993037
     * 来源：稀土掘金
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    private Json postFlask(File file, String detectUrl) {

        RestTemplate restTemplate = new RestTemplate();
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        //设置请求体，注意是LinkedMultiValueMap
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("image", new FileSystemResource(file));

        //封装请求报文
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);

        return restTemplate.postForObject(detectUrl, files, Json.class);
    }


    public Json detect(MultipartFile file,String detectUrl) {
        Json res;
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
            res = this.postFlask(dst,detectUrl);
            log.info(String.format("推理成功，共耗时:%.3fs", (System.currentTimeMillis() - t1) / 1000.0));
            // 删除临时文件
            dst.deleteOnExit();

        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件转换异常");
        }
        return res;
    }


    public Json detectHead(MultipartFile file) {
        return this.detect(file, detectHeadUrl);
    }

    public Json detectUpDown(MultipartFile file) {
        return this.detect(file, detectUpDownUrl);
    }
}
