package com.ycj.demo.web.admin.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MailInfo {

    private String from;

    @NotBlank(message = "内容不能为空")
    private String content;

    @NotBlank(message = "主题不能为空")
    private String subject;

    @NotNull(message = "请选择一个收件人")
    private String[] to;

}
