package com.ycj.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Table(name = "sys_user")
public class User extends BaseEntity{

    public User(){}

    private String username;

    private String salt;

    private String password;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private String avatar;

    private List<Role> roleList;

    private List<String> roles;

    private List<String> permissions;

    private List<Menu> menus;

}
