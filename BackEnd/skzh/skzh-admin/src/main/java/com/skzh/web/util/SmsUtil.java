package com.skzh.web.util;

import com.aliyun.teaopenapi.models.Config;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

import static com.aliyun.teautil.Common.toJSONString;

/**
 * @ClassName
 * @Description
 * @Author wangq
 * @Date 15:58 2024/10/23
 **/
public class SmsUtil {

    public static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);

        // 配置 Endpoint
        config.endpoint = "dysmsapi.aliyuncs.com";

        return new Client(config);
    }

    public static void main(String[] args) throws Exception {
        // 封装请求参数
        String accessKeyId = "";
        String accessKeySecret = "";
        String signName = "";
        String templateCode = "";
        String phoneNumeber = "";
        Map<String, String> param = new HashMap<>();
        param.put("code", "");
        // 初始化请求客户端
        Client client = createClient(accessKeyId, accessKeySecret);

        // 构造请求对象，请填入请求参数值
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumeber)
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam(toJSONString(param));

        // 获取响应对象
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

        // 响应包含服务端响应的 body 和 headers
        System.out.println(toJSONString(sendSmsResponse));
    }
}
