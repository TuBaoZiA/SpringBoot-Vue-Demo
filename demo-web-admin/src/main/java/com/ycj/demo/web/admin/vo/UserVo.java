package com.ycj.demo.web.admin.vo;

import lombok.Data;

import java.util.List;

@Data
public class UserVo {

    private String password;

    private String sourcePass;

    private String username;

    private List<Integer> roles;

}
