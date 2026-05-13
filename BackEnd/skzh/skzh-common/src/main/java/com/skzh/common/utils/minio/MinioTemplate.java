package com.skzh.common.utils.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @ClassName
 * @Description
 * @Author wangq
 * @Date 16:02 2024/8/14
 **/
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioTemplate implements Serializable {

    /**
     * 访问地址
     */
    private String endpoint;

    /**
     * accessKey类似于用户ID，用于唯一标识你的账户
     */
    private String accessKey;

    /**
     * secretKey是你账户的密码
     */
    private String secretKey;

    /**
     * 默认存储桶
     */
    private String bucketName;
}
