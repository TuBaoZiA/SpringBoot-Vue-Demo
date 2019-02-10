package com.ycj.demo.web.admin.dao;


import com.github.pagehelper.Page;
import com.ycj.demo.domain.Role;
import com.ycj.demo.web.admin.component.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {

    List<String> selectRolesByUsername(String username);

    List<Role> selectRolesByUserId(@Param("userId") Integer userId);

    List<String> selectRolesCodeByUserId(Integer userId);

    int insertRolePermission(Role role);

    int deleteRolePermissionByRoleId(Integer roleId);

    Page<Role> selectRolesByRoleName(String roleName);

    Role getRoleByRoleName(String roleName);

    int selectCountByNameAndNotId(@Param("name") String name, @Param("id") Integer id);

}
