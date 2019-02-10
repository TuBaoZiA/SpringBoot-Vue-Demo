package com.ycj.demo.web.admin.dao;


import com.github.pagehelper.Page;
import com.ycj.demo.domain.User;
import com.ycj.demo.web.admin.component.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

    User getUserByUserName(String username);

    int updatePassById(@Param("id") int id, @Param("password") String password);

    Page<User> selectUserByUsernameAndRoles(@Param("username") String username, @Param("list") List<Integer> roleIds);

    int insertUserRole(User user);

    int deleteUserRoleByUserId(int id);

    int selectCountByUserNameAndNotId(@Param("username") String username, @Param("id") Integer id);

}