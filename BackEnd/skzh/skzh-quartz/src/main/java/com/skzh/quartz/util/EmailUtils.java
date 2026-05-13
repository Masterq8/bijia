package com.skzh.quartz.util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailUtils {

        public static void sendAuthCodeEmail(String email,String authCode) {
            try {
                SimpleEmail mail = new SimpleEmail();
                mail.setHostName("smtp.qq.com");//发送邮件的服务器,这个是qq邮箱的，不用修改
                mail.setAuthentication("2396930535@qq.com", "vqhwufqngqehecii");//第一个参数是对应的邮箱用户名一般就
                mail.setFrom("2396930535@qq.com", "zhb"); //发送邮件的邮箱和发件人
                mail.setSSLOnConnect(false);//使用安全链接
                mail.addTo(email);//接收的邮箱
                mail.setSubject("验证码");//设置邮件的主愿
                mail.setMsg("尊敬的用户:你好!\n 發陆验证码为:" + authCode + "\n" + " (有效期为一分钟)");//设置邮件的内容
                mail.send();//发送
            } catch (EmailException e) {
                e.printStackTrace();
            }
        }
}
