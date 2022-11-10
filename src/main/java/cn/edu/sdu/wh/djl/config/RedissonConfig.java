package cn.edu.sdu.wh.djl.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置
 *
 * @author 蒙西昂请 创建于：2022/10/4 14:32:23
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
@Slf4j
public class RedissonConfig {
    private String host;
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        // 1. Create config object
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        log.info("redisAddress: " + redisAddress);
        config.useSingleServer().setAddress(redisAddress).setDatabase(3);
        // 2. Create Redisson instance
        // Sync and Async API
        return Redisson.create(config);
    }
}
