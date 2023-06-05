package com.example.common.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 邮箱服务工具类
 *
 * @author kfg
 * @date 2023/1/9 15:45
 */
public class MailUtil {

    private static final String SMTP = "smtp.163.com";

    private static final String USER = "xxxxxx";

    private static final String AUTH_CODE = "xxxxx";

    /**
     * 创建邮件消息
     *
     * @return 创建的邮件消息
     */
    private static MimeMessage createMail() {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", true);
            properties.put("mail.smtp.host", SMTP);
            properties.put("mail.user", USER);
            properties.put("mail.password", AUTH_CODE);
            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USER, AUTH_CODE);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(properties, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送一个简单的文本邮件
     *
     * @param to    收件人邮箱
     * @param title 邮件标题
     * @param text  邮件内容
     * @return
     */
    public static boolean sendMail(String to, String title, String text) {
        MimeMessage message = createMail();
        if (message == null) {
            return false;
        }
        try {
            // 设置发件人
            InternetAddress form = new InternetAddress(USER);
            message.setFrom(form);

            // 设置收件人
            InternetAddress toAddress = new InternetAddress(to);
            message.setRecipient(Message.RecipientType.TO, toAddress);

            // 设置邮件标题
            message.setSubject(title);

            // 设置邮件的内容体
            message.setContent(text, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

//    public static void main(String[] args) {
//        sendMail("1371199028@qq.com", "测试", "123456");
//    }
}
