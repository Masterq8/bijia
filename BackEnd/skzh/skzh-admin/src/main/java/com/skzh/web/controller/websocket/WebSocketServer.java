package com.skzh.web.controller.websocket;

import com.skzh.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket服务
 */
@Slf4j
@Component
@ServerEndpoint(value = "/notice/{userName}",encoders = { ServerEncoder.class })
public class WebSocketServer {

    //存储客户端session信息
    public static Map<String, Session> clients = new ConcurrentHashMap<>();
    //存储不同用户的客户端session信息集合
    public static Map<String, Set<String>> connection = new ConcurrentHashMap<>();

    //会话id
    private String sid = null;

    //建立连接的用户id
    private String userName = null;

    /**
     * 当与前端的websocket连接成功时，执行该方法
     * @PathParam 获取ServerEndpoint路径中的占位符信息类似 控制层的 @PathVariable注解
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName){
        this.sid = UUID.randomUUID().toString();
        this.userName = userName;
        clients.put(this.sid,session);

        //判断用户是否存在会话信息，不存在则进行添加
        Set<String> clientSet = connection.get(userName);
        if(clientSet == null){
            clientSet = new HashSet<>();
            connection.put(userName,clientSet);
        }
        clientSet.add(this.sid);
        log.info(this.userName+"用户建立连接，"+this.sid+"连接开启！！！！！！！！！！");
    }

    /**
     * 连接断开时执行
     */
    @OnClose
    public void onClose(){
        clients.remove(this.sid);
        log.info(this.sid+"连接断开！！！！！！！！！");
    }

    /**
     * 收到前台发送消息
     */
    @OnMessage
    public void onMessage(String message, Session session){
//        log.info("收到来自用户："+this.userName+"发来的消息："+message);
        System.out.println("收到来自用户："+this.userName+"发来的消息："+message);

    }

    /**
     * 连接发生错误
     */
    @OnError
    public void onError(Throwable error){
        log.info("系统错误");
        error.printStackTrace();
    }

    public void sendMessageByUserName(String userName, String message){
        if(StringUtils.isNotEmpty(userName)){
            System.out.println("服务端发送消息："+userName);
            //获取用户连接
            Set<String> clientSet = connection.get(userName);
            if(StringUtils.isNotNull(clientSet)){
                Iterator<String> iterator = clientSet.iterator();
                while(iterator.hasNext()){
                    //获取会话
                    String sid = iterator.next();
                    Session session = clients.get(sid);
                    //向会话发送消息
                    if(StringUtils.isNotNull(session)){
                        try {
                            session.getBasicRemote().sendText(message);
                        }catch (IOException e){

                        }
                    }
                }
            }
        }
    }

    /**
     * 发送自定义消息
     */
    public void sendObjMsg(String userName, Map<String, Object> map) {
        System.out.println("接收用户名："+userName+",发送消息=>" + map);
        if(StringUtils.isNotEmpty(userName)){
            //获取用户连接
            Set<String> clientSet = connection.get(userName);
            if(StringUtils.isNotNull(clientSet)){
                Iterator<String> iterator = clientSet.iterator();
                while(iterator.hasNext()){
                    //获取会话
                    String sid = iterator.next();
                    Session session = clients.get(sid);
                    System.out.println("接收sid："+sid);
                    //向会话发送消息
                    if(StringUtils.isNotNull(session)){
                        try {
                            System.out.println("已发送【"+sid+"】消息");
                            session.getBasicRemote().sendObject(map);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * 发送所有用户自定义消息
     */
    public void sendAllMsg(Map<String, Object> map) {
        System.out.println("发送所有用户消息=>" + map);
        Iterator<Map.Entry<String, Set<String>>> ite = connection.entrySet().iterator();
        while (ite.hasNext()) {
            Map.Entry<String, Set<String>> next = ite.next();
            //获取用户连接
            Set<String> clientSet = connection.get(next.getKey());
            if (StringUtils.isNotNull(clientSet)) {
                Iterator<String> iterator = clientSet.iterator();
                while (iterator.hasNext()) {
                    //获取会话
                    String sid = iterator.next();
                    Session session = clients.get(sid);
                    //向会话发送消息
                    if (StringUtils.isNotNull(session)) {
                        try {
                            session.getBasicRemote().sendObject(map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
