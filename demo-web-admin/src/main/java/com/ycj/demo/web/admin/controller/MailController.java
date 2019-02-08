package com.ycj.demo.web.admin.controller;


import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.MailService;
import com.ycj.demo.web.admin.vo.MailFileInfo;
import com.ycj.demo.web.admin.vo.MailInfo;
import com.ycj.demo.web.admin.vo.MailTemplateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send/simple")
    public Result sendSimpleMail(@Validated @RequestBody MailInfo mailInfo) throws MessagingException {
        return mailService.sendSimpleMail(mailInfo.getFrom(), mailInfo.getContent(), mailInfo.getSubject(), mailInfo.getTo());
    }

    @PostMapping("/send/file")
    public Result sendFileMail(@Validated @RequestBody MailFileInfo mailInfo) throws MessagingException, UnsupportedEncodingException {
        return mailService.sendFileMail(mailInfo.getFrom(), mailInfo.getContent(), mailInfo.getSubject(),mailInfo.getFiles(), mailInfo.getTo());
    }

    @PostMapping("/send/template")
    public Result sendTemplateMail(@RequestBody MailTemplateInfo mailInfo) throws MessagingException {
        return mailService.sendTemplateMail(mailInfo.getFrom(), mailInfo.getSubject(), mailInfo.getTemplateName(), mailInfo.getContext(), mailInfo.getTo());
    }

}
