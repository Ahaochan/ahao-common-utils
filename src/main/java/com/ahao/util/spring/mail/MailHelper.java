package com.ahao.util.spring.mail;

import com.ahao.util.spring.SpringContextHolder;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

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

    public static Builder setSubject(String subject) {
        Builder builder = new Builder();
        builder.subject = subject;
        return builder;
    }

    public static class Builder {
        private String subject;
        private String content;
        private boolean html;
        private String replyEmail;
        private Set<String> toEmails = new HashSet<>();
        private Set<String> ccEmails = new HashSet<>();
        private Set<String> bccEmails = new HashSet<>();
        private Map<String, Resource> inlineFile;
        private Map<String, Resource> extendFile;

        private MailPriority priority = MailPriority.NORMAL;
        private Date sendDate;

        public void send() {
            logger.debug("发送邮件标题:{}, 内容:{}", subject, content);
            logger.debug("邮件收件人: {}", toEmails);
            logger.debug("邮件抄送: {}", ccEmails);
            logger.debug("邮件密抄: {}", bccEmails);
            try {
                MimeMessage mimeMessage = getMailSender().createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom(getProperties().getUsername());
                helper.setTo(toEmails.toArray(new String[0]));
                helper.setCc(ccEmails.toArray(new String[0]));
                helper.setBcc(bccEmails.toArray(new String[0]));
                if(StringUtils.isNotEmpty(replyEmail)) {
                    helper.setReplyTo(replyEmail);
                }

                helper.setSubject(subject);
                helper.setText(content, html);

//                helper.setFileTypeMap(new FileTypeMap());
                helper.setValidateAddresses(true);
                helper.setPriority(priority.value);
                if(sendDate != null) {
                    helper.setSentDate(sendDate);
                }

                if(MapUtils.isNotEmpty(inlineFile)) {
                    for (Map.Entry<String, Resource> entry : inlineFile.entrySet()) {
                        helper.addInline(entry.getKey(), entry.getValue());
                    }
                }

                if (MapUtils.isNotEmpty(extendFile)) {
                    for (Map.Entry<String, Resource> entry : extendFile.entrySet()) {
                        helper.addAttachment(entry.getKey(), entry.getValue());
                    }
                }

                getMailSender().send(mimeMessage);
            } catch (MessagingException | MailException e) {
                logger.error("发送 带附件的邮件 失败", e);
                throw new MailSendException("发送邮件失败", e);
            }
        }

        // ====================== Getter And Setter ======================
        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setContent(String content, boolean html) {
            this.content = content;
            this.html = html;
            return this;
        }

        public Builder setReplyEmail(String replyEmail) {
            this.replyEmail = replyEmail;
            return this;
        }

        public Builder setToEmails(Collection<String> toEmails) {
            this.toEmails = new HashSet<>(ObjectUtils.defaultIfNull(toEmails, new HashSet<>(0)));
            return this;
        }

        public Builder addToEmail(String toEmail) {
            this.toEmails.add(toEmail);
            return this;
        }

        public Builder addToEmails(Collection<String> toEmails) {
            this.toEmails.addAll(toEmails);
            return this;
        }

        public Builder setCcEmails(Collection<String> ccEmails) {
            this.ccEmails = new HashSet<>(ObjectUtils.defaultIfNull(ccEmails, new HashSet<>(0)));
            return this;
        }

        public Builder addCcEmail(String ccEmail) {
            this.ccEmails.add(ccEmail);
            return this;
        }

        public Builder addCcEmails(Collection<String> ccEmails) {
            this.ccEmails.addAll(ccEmails);
            return this;
        }

        public Builder setBccEmails(Set<String> bccEmails) {
            this.bccEmails = new HashSet<>(ObjectUtils.defaultIfNull(bccEmails, new HashSet<>(0)));
            return this;
        }

        public Builder addBccEmail(String bccEmail) {
            this.bccEmails.add(bccEmail);
            return this;
        }

        public Builder addBccEmails(Collection<String> bccEmails) {
            this.bccEmails.addAll(bccEmails);
            return this;
        }

        public Builder setInlineFile(Map<String, Resource> inlineFile) {
            this.inlineFile = new HashMap<>(ObjectUtils.defaultIfNull(inlineFile, new HashMap<>(0)));
            return this;
        }

        public Builder addInlineFile(String contentId, Resource resource) {
            this.inlineFile.put(contentId, resource);
            return this;
        }

        public Builder addInlineFile(Map<String, Resource> inlineFile) {
            this.inlineFile.putAll(inlineFile);
            return this;
        }

        public Builder setExtendFile(Map<String, Resource> extendFile) {
            this.extendFile = new HashMap<>(ObjectUtils.defaultIfNull(extendFile, new HashMap<>(0)));
            return this;
        }

        public Builder addExtendFile(String contentId, Resource resource) {
            this.extendFile.put(contentId, resource);
            return this;
        }

        public Builder addExtendFile(Map<String, Resource> extendFile) {
            this.extendFile.putAll(ObjectUtils.defaultIfNull(extendFile, new HashMap<>(0)));
            return this;
        }

        public Builder setPriority(MailPriority priority) {
            this.priority = ObjectUtils.defaultIfNull(priority, MailPriority.NORMAL);
            return this;
        }

        public Builder setSendDate(Date sendDate) {
            this.sendDate = sendDate;
            return this;
        }
    }

    public enum MailPriority {
        HIGHEST(1), HIGH(2), NORMAL(3), LOW(4), LOWEST(5);
        private int value;
        MailPriority(int value) {
            this.value = value;
        }
    }
}
