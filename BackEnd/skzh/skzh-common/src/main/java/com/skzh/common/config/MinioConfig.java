package com.skzh.common.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @ClassName
 * @Description
 * @Author wangq
 * @Date 9:51 2024/9/11
 **/

@Data
@Component
public class MinioConfig {

    /**
     * 端点
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 用户名
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * 密码
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 存储桶名称
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }

}
