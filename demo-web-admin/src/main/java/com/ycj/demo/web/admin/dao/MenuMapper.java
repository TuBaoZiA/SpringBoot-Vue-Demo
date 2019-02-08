package com.ycj.demo.web.admin.dao;

import com.github.pagehelper.Page;
import com.ycj.demo.domain.Menu;
import com.ycj.demo.web.admin.tkmybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends MyMapper<Menu> {

    /**
     * 根据权限查询所有父级菜单
     * @param permissions
     * @return
     */
    List<Integer> selectParentMenuIdByPermissions(List<String> permissions);

    /**
     * 根据权限和父级菜单ID查询所有子级菜单
     * @param permissions
     * @param parentIds
     * @return
     */
    List<Menu> selectMenuByPermissionsAndParent(@Param("list") List<String> permissions, @Param("parentIds") List<Integer> parentIds);

    /**
     * 查询所有菜单
     * @return
     */
    Page<Menu> selectAllMenu();

    /**
     * 查询所有不是自己子类的菜单
     * @param id
     * @param keepParent 如果不为null 保留父类
     * @return
     */
    List<Menu> selectNotChildrenMenuById(@Param("id") Integer id, @Param("keepParent") Boolean keepParent);

    /**
     * 查询name数量
     * @param name
     * @param id
     * @return
     */
    int selectCountByNameAndNotId(@Param("name") String name, @Param("id") Integer id);

    /**
     * 插入菜单
     * @param menu
     * @return
     */
    int insertMenu(Menu menu);

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    int updateMenu(Menu menu);

    /**
     * 删除父菜单后，将子菜单的parentId修改为父菜单的parentId
     * @param parentId
     * @param newParentId
     * @return
     */
    int updateChildrenParentByParentId(@Param("parentId") Integer parentId, @Param("newParentId") Integer newParentId);

    /**
     * 将对应菜单权限置为空
     * @param permissionId
     * @return
     */
    int updatePermissionByPermissionId(Integer permissionId);

}
