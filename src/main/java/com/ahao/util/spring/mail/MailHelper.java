package com.ahao.util.spring.mail;

import com.ahao.util.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

public class MailHelper {
    private static final Logger logger = LoggerFactory.getLogger(MailHelper.class);
    // ======================================== 依赖 ==================================================
    private volatile static MailProperties properties;
    private volatile static JavaMailSender mailSender;

    public static JavaMailSender getMailSender() {
        if (mailSender == null) {
            synchronized (MailHelper.class) {
                if (mailSender == null) {
                    init();
                }
            }
        }
        return mailSender;
    }

    public static MailProperties getProperties() {
        if (properties == null) {
            synchronized (MailHelper.class) {
                if (properties == null) {
                    init();
                }
            }
        }
        return properties;
    }

    private static void init() {
        properties = SpringContextHolder.getBean(MailProperties.class);
        mailSender = SpringContextHolder.getBean(JavaMailSender.class);
    }
    // ======================================== 依赖 ==================================================

    public static boolean sendText(String email, String title, String content) {
        return sendTextWithFile(email, title, content, null);
    }

    public static boolean sendTextWithFile(String email, String title, String content, Map<String, InputStreamSource> files) {
        return send(email, title, content, false, files);
    }

    public static boolean sendHtml(String email, String title, String html) {
        return sendHtmlWithFile(email, title, html, null);
    }

    public static boolean sendHtmlWithFile(String email, String title, String html, Map<String, InputStreamSource> files) {
        return send(email, title, html, true, files);
    }

    public static boolean send(String email, String title, String content, boolean html, Map<String, InputStreamSource> files) {
        logger.debug("发送邮件给{}, 标题:{}, 内容:{}", email, title, content);
        try {
            MimeMessage mimeMessage = getMailSender().createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(getProperties().getUsername());
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(content, html);

            if (files != null) {
                for (Map.Entry<String, InputStreamSource> entry : files.entrySet()) {
                    helper.addAttachment(entry.getKey(), entry.getValue());
                }
            }

            getMailSender().send(mimeMessage);
            return true;
        } catch (MessagingException | MailException e) {
            logger.error("发送 带附件的邮件 失败", e);
        }
        return false;
    }
}
