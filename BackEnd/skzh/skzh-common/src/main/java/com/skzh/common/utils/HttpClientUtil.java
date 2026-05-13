package com.skzh.common.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 封装了采用HttpClient发送HTTP请求的方法
 */
public class HttpClientUtil {
    private HttpClientUtil(){}

    /**
     * 发送HTTP_GET请求
     */
    public static String sendGetRequest(String reqURL){
        System.out.println(reqURL);
        String respContent = "通信失败"; //响应内容
        HttpClient httpClient = new DefaultHttpClient(); //创建默认的httpClient实例
        //设置代理服务器
        //httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("10.0.0.4", 8080));
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); //连接超时10s
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);         //读取超时20s
        HttpGet httpGet = new HttpGet(reqURL); //创建org.apache.http.client.methods.HttpGet
        try{
            HttpResponse response = httpClient.execute(httpGet); //执行GET请求
            HttpEntity entity = response.getEntity();            //获取响应实体
            if(null != entity){
                //respCharset=EntityUtils.getContentCharSet(entity)也可以获取响应编码,但从4.1.3开始不建议使用这种方式
                Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                respContent = EntityUtils.toString(entity, respCharset);
                //Consume response content
                EntityUtils.consume(entity);
            }
            StringBuilder respHeaderDatas = new StringBuilder();
            for(Header header : response.getAllHeaders()){
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
        }catch (Exception e){
            System.out.println("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下"+e);
        }finally{
            //关闭连接,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return respContent;
    }

    /**
     * 发送HTTP_POST请求，响应header数据，返回token
     */
    public static String sendPostHeaderRequest(String reqURL, Map<String, Object> params, String encodeCharset){
        String reseContent = "通信失败";
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000000);
        HttpPost httpPost = new HttpPost(reqURL);
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=" + encodeCharset);
        try{
        	if(null != params){
                StringEntity entity = new StringEntity(JSONObject.toJSONString(params));
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            for(Header header : response.getAllHeaders()){
                if ("X-Subject-Token".equals(header.getName())) {
                    reseContent = header.getValue();
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("请求通信[" + reqURL + "]时遇异常,堆栈轨迹如下"+e);
        }finally{
            httpClient.getConnectionManager().shutdown();
        }
        return reseContent;
    }

    /**
     * 发送HTTP_POST请求，响应body数据，返回response
     **/
    public static String sendPostBodyRequest(String reqURL, Map<String, String> params, String encodeCharset, String authToken){
        String reseContent = "通信失败";
        CloseableHttpClient httpClient = null;
        try{
            // 跳过SSL认证
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (x509CertChain, authType) -> true)
                    .build();
            httpClient = HttpClientBuilder.create()
                    .setSSLContext(sslContext)
                    .setConnectionManager(
                            new PoolingHttpClientConnectionManager(
                                    RegistryBuilder.<ConnectionSocketFactory>create()
                                            .register("http", PlainConnectionSocketFactory.INSTANCE)
                                            .register("https", new SSLConnectionSocketFactory(sslContext,
                                                    NoopHostnameVerifier.INSTANCE))
                                            .build()
                            ))
                    .build();
            HttpPost httpPost = new HttpPost(reqURL);
            httpPost.setHeader("content-type", "application/json; charset=" + encodeCharset);
            httpPost.setHeader("x-auth-token", authToken);
            if(null != params){
                StringEntity entity = new StringEntity(JSONObject.toJSONString(params), encodeCharset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("请求通信[" + reqURL + "]时遇异常,堆栈轨迹如下"+e);
        }finally{
            httpClient.getConnectionManager().shutdown();
        }
        return reseContent;
    }

}
