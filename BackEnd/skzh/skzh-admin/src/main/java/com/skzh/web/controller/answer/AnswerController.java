package com.skzh.web.controller.answer;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.core.redis.RedisCache;
import com.skzh.common.utils.HttpClientUtil;
import com.skzh.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName
 * @Description 智能问答控制层
 * @Author wangq
 * @Date 14:02 2024/8/29
 **/
@RestController
@RequestMapping("/answer" )
public class AnswerController {

    @Value("${subjectToken.url}" )
    private String url;

    @Value("${subjectToken.name}" )
    private String name;

    @Value("${subjectToken.ak}" )
    private String ak;

    @Value("${subjectToken.sk}" )
    private String sk;

    @Value("${answer.url}" )
    private String answerUrl;

    @Autowired
    private RedisCache redisCache;

    /**
     * @Description 根据内容获取智能问答结果
     * @Author wangq
     * @Date 2024/8/29 14:13
     * @Param [content]
     * @return
     **/
    @GetMapping("/getByContent")
    public AjaxResult batchGenCode(String content) {
        if (StringUtils.isEmpty(content)) {
            return AjaxResult.error("请输入内容进行询问");
        }
        String authToken = redisCache.getCacheObject("X-Subject-Token");
        if (StringUtils.isEmpty(authToken)) {// 若为空，重新获取token
            authToken = getAuthToken();
        }
        Map<String, String> ansParams = new HashMap<>();
        ansParams.put("text", content);
        String res = HttpClientUtil.sendPostBodyRequest(answerUrl, ansParams, "UTF-8", authToken);
        if ("通信失败".equals(res)) {
            return AjaxResult.error("通信失败，请稍后再试");
        }
        JSONObject obj = JSONObject.parseObject(res);
        if (obj.get("error_msg") != null) {
            return AjaxResult.error("通信失败:"+obj.get("error_msg"));
        }
        JSONArray array = (JSONArray) obj.get("response");
        if (array == null) {
            return AjaxResult.error("系统繁忙，请稍后再试");
        }
        String ansMsg = array.get(0).toString();
        System.out.println(ansMsg);
        ansMsg = ansMsg.substring(ansMsg.indexOf("答：")+3);
        return AjaxResult.success(ansMsg);
    }

    /**
     * 获取用户token
     **/
    public String getAuthToken() {
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
        String authToken = HttpClientUtil.sendPostHeaderRequest(url, params, "UTF-8");
        redisCache.setCacheObject("X-Subject-Token", authToken);// 存放到redis中
        return authToken;
    }

}
