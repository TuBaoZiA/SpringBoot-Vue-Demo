package com.ycj.demo.web.admin.vo;

import lombok.Data;
import org.thymeleaf.context.Context;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MailTemplateInfo extends MailInfo{

    @NotBlank(message = "请选择一个模板")
    private String templateName;

    @NotNull(message = "请选择模板内容")
    private Context context;
}
