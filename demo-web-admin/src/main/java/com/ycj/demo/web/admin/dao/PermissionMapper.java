package com.ycj.demo.web.admin.dao;



import com.ycj.demo.domain.Permission;
import com.ycj.demo.web.admin.component.MyMapper;


import java.util.List;

public interface PermissionMapper extends MyMapper<Permission> {

    /**
     * 根据角色查询权限
     * @param roles
     * @return
     */
    List<String> getPermissionsByRoles(List<String> roles);

    /**
     * 查询所有权限code
     * @return
     */
    List<String> selectAllPermissions();

    /**
     * 根据权限ID 删除角色权限
     * @param permissionId
     * @return
     */
    int deleteRolePermissionByPermissionId(Integer permissionId);


}
