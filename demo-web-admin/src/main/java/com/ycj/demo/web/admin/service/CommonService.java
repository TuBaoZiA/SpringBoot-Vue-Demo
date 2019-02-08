package com.ycj.demo.web.admin.service;


import com.ycj.demo.domain.Menu;
import com.ycj.demo.domain.Permission;
import com.ycj.demo.domain.Role;
import com.ycj.demo.web.admin.dao.MenuMapper;
import com.ycj.demo.web.admin.dao.PermissionMapper;
import com.ycj.demo.web.admin.dao.RoleMapper;
import com.ycj.demo.web.admin.vo.Drop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private MenuMapper menuMapper;

    public List<Drop> selectNotChildrenMenuById(Integer id, Boolean isKeepParent){
        List<Menu> menus = menuMapper.selectNotChildrenMenuById(id, isKeepParent);
        List<Drop> drops = new ArrayList<>(menus.size());
        menus.forEach(menu -> {
            drops.add(new Drop(menu.getId(), menu.getName()));
        });
        return drops;
    }

    public List<Drop> rolesDrop(){
        List<Role> roles = roleMapper.selectAll();
        List<Drop> drops = new ArrayList<>(roles.size());
        roles.forEach(role -> {
            drops.add(new Drop(role.getId(), role.getName()));
        });
        return drops;
    }

    public List<Drop> permissionsDrop(){
        List<Permission> permissions = permissionMapper.selectAll();
        List<Drop> drops = new ArrayList<>(permissions.size());
        permissions.forEach(permission -> {
            drops.add(new Drop(permission.getId(), permission.getDesc()));
        });
        return drops;
    }

}
