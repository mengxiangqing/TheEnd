package cn.edu.sdu.wh.djl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author 蒙西昂请 创建于：2022/9/27 20:03
 */
@Configuration
@Profile({"dev"})
public class Knife4jConfiguration {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("1.1版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("cn.edu.sdu.wh.djl.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    /**
     * api 信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("后台接口")
                .description("接口文档")
                .termsOfServiceUrl("https://github.com/mengxiangqing")
                .contact(new Contact("蒙西昂请","https://github.com/mengxiangqing","xxx@qq.com"))
                .version("1.0")
                .build();
    }
}
