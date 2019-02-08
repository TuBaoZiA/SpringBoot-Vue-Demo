package com.ycj.demo.web.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ycj.demo.domain.Role;
import com.ycj.demo.web.admin.dao.RoleMapper;
import com.ycj.demo.exception.BusinessException;
import com.ycj.demo.result.CodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

    @Autowired
    RoleMapper roleMapper;

    public int selectCountByNameAndNotId(String name, String idstr){
        Integer id = idstr.equals("undefined") ? null : Integer.valueOf(idstr);
        return roleMapper.selectCountByNameAndNotId(name, id);
    }

    public Page<Role> selectRoleByName(Integer page, Integer limit, String roleName){
        PageHelper.startPage(page, limit);
        return roleMapper.selectRolesByRoleName(roleName);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRole(Role role){

        if(roleMapper.selectCountByNameAndNotId(role.getName(), role.getId()) > 0){
            throw new BusinessException(CodeMsg.ROLENAME_EXIST);
        }

        roleMapper.insertUseGeneratedKeys(role);

        if(CollUtil.isNotEmpty(role.getPermissionIds())){
            roleMapper.insertRolePermission(role);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Integer roleId){
        //删除角色权限信息
        roleMapper.deleteRolePermissionByRoleId(roleId);

        roleMapper.deleteByPrimaryKey(roleId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Role role){

        if(roleMapper.selectCountByNameAndNotId(role.getName(), role.getId()) > 0){
            throw new BusinessException(CodeMsg.ROLENAME_EXIST);
        }

        roleMapper.updateByPrimaryKeySelective(role);

        roleMapper.deleteRolePermissionByRoleId(role.getId());
        if(CollUtil.isNotEmpty(role.getPermissionIds())){
            roleMapper.insertRolePermission(role);
        }

    }

}
