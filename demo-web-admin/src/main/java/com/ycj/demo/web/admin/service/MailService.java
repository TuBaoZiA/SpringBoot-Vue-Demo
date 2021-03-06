package com.ycj.demo.web.admin.service;

import cn.hutool.core.util.StrUtil;
import com.ycj.demo.web.admin.component.SendMailService;
import com.ycj.demo.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    @Autowired
    private SendMailService sendMailService;


    /**
     * 发送简单邮件
     * @param from
     * @param content
     * @param subject
     * @param to
     * @return
     * @throws MessagingException
     */
    public Result sendSimpleMail(String from, String content, String subject, String[] to) throws MessagingException {
        if (StrUtil.isEmpty(from)){
            sendMailService.sendSimpleMailByDefaultFrom(content, subject, to);
        }else{
            sendMailService.sendSimpleMail(from, content, subject, to);
        }
        return Result.success();
    }

    /**
     * 发送带附件的邮件
     * @param from
     * @param content
     * @param subject
     * @param filePaths
     * @param to
     * @return
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public Result sendFileMail(String from, String content, String subject, String[] filePaths, String[] to) throws UnsupportedEncodingException, MessagingException {
        if(StrUtil.isEmpty(from)){
            sendMailService.sendFileMailByDefaultFrom(content, subject, filePaths, to);
        }else{
            sendMailService.sendFileMail(from, content, subject, filePaths, to);
        }
        return Result.success();
    }

    /**
     * 发送模板邮件
     * @param from
     * @param subject
     * @param templateName
     * @param context
     * @param to
     * @return
     * @throws MessagingException
     */
    public Result sendTemplateMail(String from, String subject, String templateName, Context context, String[] to) throws MessagingException {
        if(StrUtil.isEmpty(from)){
            sendMailService.sendFreeMarkerMailByDefaultFrom(subject, templateName, context, to);
        }else{
            sendMailService.sendFreeMarkerMail(from, subject, templateName, context, to);
        }
        return Result.success();
    }



}
