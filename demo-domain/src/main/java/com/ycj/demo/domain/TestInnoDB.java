package com.ycj.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@Table(name = "test_InnoDB")
public class TestInnoDB extends BaseEntity{

    public TestInnoDB(){}

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