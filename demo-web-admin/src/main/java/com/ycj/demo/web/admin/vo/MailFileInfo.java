package com.ycj.demo.web.admin.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MailFileInfo extends MailInfo{

    @NotNull(message = "请选择一个附件")
    private String[] files;
}
