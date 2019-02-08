package com.ycj.demo.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

@Data
@Table(name = "sys_menu")
public class Menu extends BaseEntity{

    private String path;

    private String component;

    private String redirect;

    private String name;

    @Column(name = "`order`")
    private Integer order;

    private Integer parentId;

    private MenuMeta meta;

    private List<Menu> children;
}
