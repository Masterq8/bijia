package com.skzh.web.controller.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.Map;

/**
 * @ClassName
 * @Description
 * @Author wangq
 * @Date 13:40 2022/6/23
 **/
public class ServerEncoder implements Encoder.Text<Map> {

    @Override
    public void destroy() {
    }

    @Override
    public void init(EndpointConfig arg0) {

    }

    /*
     *  encode()方法里的参数和Text<T>里的T一致，如果你是Student，这里就是encode（Student student）
     */
    @Override
    public String encode(Map map) throws EncodeException {
        try {
            /*
             * 这里是重点，只需要返回Object序列化后的json字符串就行
             * 你也可以使用gosn，fastJson来序列化。
             */
            JsonMapper jsonMapper = new JsonMapper();
            return jsonMapper.writeValueAsString(map);
        } catch ( JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
