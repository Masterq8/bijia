package com.skzh.quartz.task;

import com.skzh.common.core.redis.RedisCache;
import com.skzh.common.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName 第三方用户token任务调度
 * @Description
 * @Author wangq
 * @Date 9:04 2024/8/29
 **/
@Component("subjectTokenTask" )
public class SubjectTokenTask {

    @Value("${subjectToken.url}" )
    private String url;

    @Value("${subjectToken.name}" )
    private String name;

    @Value("${subjectToken.ak}" )
    private String ak;

    @Value("${subjectToken.sk}" )
    private String sk;

    @Autowired
    private RedisCache redisCache;

    public void getToken() {
        Map<String, Object> params = new HashMap<>();
        Map<String, String> access = new HashMap<>();
        access.put("key", ak);
        Map<String, String> secret = new HashMap<>();
        secret.put("key", sk);
        Map<String, Map<String, String>> hwAkSk = new HashMap<>();
        hwAkSk.put("access", access);
        hwAkSk.put("secret", secret);
        List<String> methods = new ArrayList<>();
        methods.add("hw_ak_sk");
        Map<String, Object> identity = new HashMap<>();
        identity.put("methods", methods);
        identity.put("hw_ak_sk", hwAkSk);
        Map<String, String> project = new HashMap<>();
        project.put("name", name);
        Map<String, Object> scope = new HashMap<>();
        scope.put("project", project);
        Map<String, Object> auth = new HashMap<>();
        auth.put("identity", identity);
        auth.put("scope", scope);
        params.put("auth", auth);
        String resToken = HttpClientUtil.sendPostHeaderRequest(url, params, "UTF-8");
        redisCache.setCacheObject("X-Subject-Token", resToken);// 存放到redis中
    }

}
