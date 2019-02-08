package com.ycj.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@Table(name = "test_MyISAM")
public class TestMyISAM extends BaseEntity{

    public TestMyISAM(){}

    private String username;

    @Column(name = "`password`")
    private String password;

    private String email;

    private String address;

    private Integer sex;

    private Date birthday;

    @Column(name = "`desc`")
    private String desc;

    private String phone;

    private Integer price;

    @Column(name = "`level`")
    private Integer level;

    private String url;
}