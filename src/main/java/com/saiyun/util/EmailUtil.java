package com.saiyun.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class EmailUtil {
    private static String from = "teleport_one@163.com"; // 发件人邮箱地址
    private static String user = "teleport_one@163.com"; // 发件人称号，同邮箱地址
    private static String password = "OYDOUCPDMIQOZEJG"; // 发件人邮箱客户端授权码

    /**
     *
     * @param to
     * @param text
     * @param title
     */
    /* 发送验证信息的邮件 */
    public static boolean  sendMail(String to, String text, String title) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.163.com"); // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", "smtp.163.com"); // 需要经过授权，也就是用户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true"); // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props); // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(true); // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session); // 加载发件人地址
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); // 加载收件人地址
            message.setSubject(title); // 加载标题
            Multipart multipart = new MimeMultipart(); // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            BodyPart contentPart = new MimeBodyPart(); // 设置邮件的文本内容
            contentPart.setContent(text, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            message.saveChanges(); // 保存变化
            Transport transport = session.getTransport("smtp"); // 连接服务器的邮箱
            transport.connect("smtp.163.com", user, password); // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // Teleporturl Teleportcode
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<table cellpadding='0' cellspacing='0' style='border: 1px solid #cdcdcd; width: 640px; margin:auto;font-size: 12px; color: #1E2731; line-height: 20px;'>");
        stringBuffer.append("<tbody><tr>");
        stringBuffer.append("<td colspan='3' align='center' style='background-color:#454c6d; height: 55px; padding: 30px 0'><a href='Teleporturl' target='_blank' rel='noopener'><img src='javascript:;'></a></td>");
        stringBuffer.append("</tr><tr style='height: 30px;'></tr><tr><td width='20'></td><td style='line-height: 40px'>您好：<br>");
        stringBuffer.append("【Teleport】注册验证码：<b>Teleportcode</b>，您正在尝试【注册】，该验证码将于15分钟后失效。<br>");
        stringBuffer.append("验证码仅用于Huobi官网，如果非本人操作，请不要在任何地方输入该验证码并立即修改登录密码！ <br>为防止非法诈骗，请勿将帐号、密码、验证码在除官网外任何其他第三方网站输入。<br>");
        stringBuffer.append("</td><td width='20'></td></tr><tr style='height: 20px;'></tr><tr><td width='20'></td><td><br>Teleport团队<br>");
        stringBuffer.append("<a href='Teleporturl' rel='noopener' target='_blank'>Teleporturl</a><br></td><td width='20'></td></tr>");
        stringBuffer.append("<tr style='height: 50px;'></tr></tbody></table>");
        String body = stringBuffer.toString();
        String teleporturl = body.replaceAll("Teleporturl", "https://www.baidu.com/");
        String teleportcode = teleporturl.replaceAll("Teleportcode", "123456");
        EmailUtil.sendMail("1372501851@qq.com", teleportcode, "【Teleport】注册成功");
    }
}
