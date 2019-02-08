package com.ycj.demo.web.admin.service;

import com.ycj.demo.domain.Permission;
import com.ycj.demo.web.admin.dao.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionService extends BaseService<Permission, PermissionMapper>{

    @Autowired
    private PermissionMapper mapper;

    @Autowired
    private MenuService menuService;

    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Integer permissionId){
        //1、删除role_permission对应的记录
        mapper.deleteRolePermissionByPermissionId(permissionId);
        //2、修改menu permission
        menuService.updateMenuPermissionByPermissionId(permissionId);
        //3、删除permission
        mapper.deleteByPrimaryKey(permissionId);
    }

}
