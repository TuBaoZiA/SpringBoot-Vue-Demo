package com.ycj.demo.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@Table(name = "sys_permission")
public class Permission extends BaseEntity{

    private String resource;

    private String code;

    private String handle;

    @Column(name = "`desc`")
    private String desc;

}
