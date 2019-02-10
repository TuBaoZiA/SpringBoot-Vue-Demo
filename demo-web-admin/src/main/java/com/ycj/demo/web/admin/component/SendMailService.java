package com.ycj.demo.web.admin.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;

@Component
@Slf4j
public class SendMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String from;

    private void sendMail(String from, String content, String subject, String... to) throws MessagingException {
        sendMail(from, content, subject, null, to);
    }

    /**
     * 发送邮件
     * @param from 发件人
     * @param content 内容
     * @param subject 主题
     * @param files 附件数组
     * @param to 收件人
     * @throws MessagingException
     */
    private void sendMail(String from, String content, String subject, File[] files, String... to) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(from);
        helper.setTo(to);
        helper.setText(content, true);
        helper.setSubject(subject);

        if(files != null){
            for (File file : files) {
                try {
                    helper.addAttachment(MimeUtility.encodeWord(file.getName()),file);
                } catch (UnsupportedEncodingException e) {
                    log.error("添加附件失败："+e.getMessage());
                }
            }
        }

        mailSender.send(message);
    }

    public void sendSimpleMailByDefaultFrom(String content, String subject, String... to) throws MessagingException {
        sendMail(from, content, subject, to);
    }

    public void sendSimpleMail(String from, String content, String subject, String... to) throws MessagingException {
        sendMail(from, content, subject, to);
    }

    public void sendFileMailByDefaultFrom(String content, String subject, String[] filePaths, String... to) throws UnsupportedEncodingException, MessagingException {
        sendFileMail(from, content, subject, filePaths, to);
    }

    public void sendFileMail(String from, String content, String subject, String[] filePaths, String... to) throws UnsupportedEncodingException, MessagingException {
        File[] files = new File[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            files[i] = new File(filePaths[i]);
        }
        sendFileMail(from, content, subject, files, to);
    }

    public void sendFileMail(String from, String content, String subject, File[] files, String... to) throws MessagingException, UnsupportedEncodingException {
        sendMail(from, content, subject, files, to);
    }

    public void sendFreeMarkerMailByDefaultFrom(String subject, String templateName, Context context, String... to) throws MessagingException {
        sendMail(from, templateEngine.process(templateName, context), subject, to);
    }

    public void sendFreeMarkerMail(String from, String subject, String templateName, Context context, String... to) throws MessagingException {
        sendMail(from, templateEngine.process(templateName, context), subject, to);
    }

}
