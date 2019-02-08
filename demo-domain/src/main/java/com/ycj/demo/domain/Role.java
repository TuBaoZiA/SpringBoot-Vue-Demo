package com.ycj.demo.domain;

import lombok.Data;

import javax.persistence.Table;
import java.util.List;

@Data
@Table(name = "sys_role")
public class Role extends BaseEntity {

    private String name;

    private List<Integer> permissionIds;

    private List<Permission> permissions;

}
