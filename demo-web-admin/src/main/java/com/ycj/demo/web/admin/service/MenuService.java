package com.ycj.demo.web.admin.service;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.ycj.demo.domain.Menu;
import com.ycj.demo.web.admin.dao.MenuMapper;
import com.ycj.demo.exception.BusinessException;
import com.ycj.demo.result.CodeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    MenuMapper menuMapper;

    public int selectCountByNameAndNotId(String name, String idStr){
        Integer id = "undefined".equals(idStr) ? null : Integer.valueOf(idStr);
        return menuMapper.selectCountByNameAndNotId(name, id);
    }

    private Menu handleMenu(Menu menu, List<Menu> menus){
        // 子菜单
        List<Menu> childList = new ArrayList<>();
        for (Menu menu1 : menus) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (menu1.getParentId() != null && menu1.getParentId().equals(menu.getId())) {
                childList.add(menu1);
            }
        }
        menu.setChildren(childList);

        // 把子菜单的子菜单再循环一遍
        for (Menu menu1 : childList) {
            handleMenu(menu1, menus);
        }

        return menu;
    }

    public Page<Menu> selectAllMenu(){
        List<Menu> menus = menuMapper.selectAllMenu();
        List<Menu> menuPage = new Page<>();
        menus.forEach(menu ->{
            if(menu.getParentId() == null){
                menuPage.add(handleMenu(menu, menus));
            }
        });
        return (Page<Menu>) menuPage;
    }

    public List<Menu> selectMenuByPermissions(List<String> permissions){
        List<Integer> parentIds = menuMapper.selectParentMenuIdByPermissions(permissions);
        if(parentIds.size() == 0){
            return null;
        }
        List<Menu> menus = menuMapper.selectMenuByPermissionsAndParent(permissions, parentIds);
        List<Menu> menuPage = new Page<>();
        menus.forEach(menu ->{
            if(menu.getParentId() == null){
                menuPage.add(handleMenu(menu, menus));
            }
        });
        return menuPage;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveMenu(Menu menu){

        if(menuMapper.selectCountByNameAndNotId(menu.getName(), menu.getId()) > 0){
            throw new BusinessException(CodeMsg.MENU_ERROR.setMessage("菜单名称已存在！"));
        }

        if(StrUtil.isEmpty(menu.getPath())){
            throw new BusinessException(CodeMsg.MENU_ERROR.setMessage("菜单URL不能为空！"));
        }

        menuMapper.insertMenu(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Integer id){
        Integer parentId = menuMapper.selectByPrimaryKey(id).getParentId();
        menuMapper.deleteByPrimaryKey(id);
        menuMapper.updateChildrenParentByParentId(id, parentId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Menu menu){

        if(menuMapper.selectCountByNameAndNotId(menu.getName(), menu.getId()) > 0){
            throw new BusinessException(CodeMsg.MENU_ERROR.setMessage("菜单名称已存在！"));
        }

        if(StrUtil.isEmpty(menu.getPath())){
            throw new BusinessException(CodeMsg.MENU_ERROR.setMessage("菜单URL不能为空！"));
        }

        menuMapper.updateMenu(menu);

    }

    public void updateMenuPermissionByPermissionId(Integer permissionId){
        menuMapper.updatePermissionByPermissionId(permissionId);
    }

}
