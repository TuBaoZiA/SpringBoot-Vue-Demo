package com.ycj.demo.web.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ycj.demo.domain.User;
import com.ycj.demo.constant.SystemConstant;
import com.ycj.demo.web.admin.dao.MenuMapper;
import com.ycj.demo.web.admin.dao.PermissionMapper;
import com.ycj.demo.web.admin.dao.RoleMapper;
import com.ycj.demo.web.admin.dao.UserMapper;
import com.ycj.demo.exception.BusinessException;
import com.ycj.demo.web.admin.component.RedisService;
import com.ycj.demo.web.admin.util.redis.key.TokenKey;
import com.ycj.demo.web.admin.util.redis.key.UserKey;
import com.ycj.demo.result.CodeMsg;
import com.ycj.demo.web.admin.component.shiro.JWTToken;
import com.ycj.demo.web.admin.util.JwtUtil;
import com.ycj.demo.web.admin.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.ycj.demo.web.admin.util.ShiroEncryptUtil.encryptPassBySalt;

@Service
public class UserService extends BaseService<User, UserMapper>{

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuMapper menuMapper;

    public String sign(String username, String password){
        //根据用户名到数据库查询用户信息
        User user = getUserByUsername(username);

        if(user != null){

            if(checkPwd(password, user)){
                String token = JwtUtil.sign(username, user.getPassword());
                //将token 存入redis，过期时间为30分钟
                redisService.set(TokenKey.TOKEN, token, token);

                Subject subject = SecurityUtils.getSubject();

                try {
                    subject.login(new JWTToken(token));
                } catch (AuthenticationException e) {
                    throw new BusinessException(CodeMsg.LOGIN_ERROR);
                }

                return token;
            }

        }
        throw new BusinessException(CodeMsg.LOGIN_ERROR);
    }

    public void logout(String token){
        redisService.delete(TokenKey.TOKEN, token);
        redisService.delete(UserKey.userInfo, JwtUtil.getUsername(token));
    }

    private boolean checkPwd(String password, User user){
        return user.getPassword().equals(encryptPassBySalt(password, user.getSalt()));
    }

    public int selectCountByNameAndNotId(String username, String idStr){
        Integer id = idStr.equals("undefined") ? null : Integer.valueOf(idStr);
        return mapper.selectCountByUserNameAndNotId(username, id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Integer userId){
        //删除用户角色关系
        mapper.deleteUserRoleByUserId(userId);
        //删除用户信息
        mapper.deleteByPrimaryKey(userId);
    }

    /**
     * 修改密码
     * @param id
     * @param userVo
     * @return
     */
    public int rePassById(Integer id, UserVo userVo){
        User user = mapper.selectByPrimaryKey(id);
        if(!encryptPassBySalt(userVo.getSourcePass(), user.getSalt()).equals(user.getPassword())){
            throw new BusinessException(CodeMsg.PASSWORD_ERROR);
        }

        return mapper.updatePassById(id, encryptPassBySalt(userVo.getPassword(), user.getSalt()));
    }

    @Transactional(rollbackFor = Exception.class)
    public int saveUser(User user){

        if (mapper.selectCountByUserNameAndNotId(user.getUsername(), user.getId()) > 0) {
            throw new BusinessException(CodeMsg.USERNAME_EXIST);
        }

        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setSalt(user.getUsername());
        user.setPassword(encryptPassBySalt(SystemConstant.DEFAULT_PASSWORD, user.getSalt()));

        mapper.insertUseGeneratedKeys(user);

        if(CollUtil.isNotEmpty(user.getRoles())){
            mapper.insertUserRole(user);
        }

        return user.getId();
    }

    public void updateUserInfo(User user){
        user.setUpdateTime(new Date());

        if (mapper.selectCountByUserNameAndNotId(user.getUsername(), user.getId()) > 0) {
            throw new BusinessException(CodeMsg.USERNAME_EXIST);
        }else {
            mapper.updateByPrimaryKeySelective(user);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(User user){
        if(CollUtil.isNotEmpty(user.getRoles())){
            mapper.deleteUserRoleByUserId(user.getId());
            mapper.insertUserRole(user);
        }
    }

    public User getUserByUsername(String username){
        return mapper.getUserByUserName(username);
    }

    public void resetUserInfo(String username){
        redisService.delete(UserKey.userInfo, username);
    }

    public User getUserInfoByToken(String token){
        User user;

        String username = JwtUtil.getUsername(token);

        if(username == null){
            throw new BusinessException(CodeMsg.UNAUTHENTICATED);
        }

        user = redisService.get(UserKey.userInfo, username, User.class);

        if(user == null) {

            user = getUserByUsername(username);

            if (user == null) {
                throw new BusinessException(CodeMsg.UNAUTHENTICATED);
            }

            user.setPassword("");
            user.setSalt("");

            user.setRoles(roleMapper.selectRolesCodeByUserId(user.getId()));
            if (user.getRoles().contains(SystemConstant.ADMIN)) {
                user.setPermissions(permissionMapper.selectAllPermissions());
                user.setMenus(menuService.selectAllMenu());
            } else if (CollUtil.isNotEmpty(user.getRoles())) {
                user.setPermissions(permissionMapper.getPermissionsByRoles(user.getRoles()));
                user.setMenus(menuService.selectMenuByPermissions(user.getPermissions()));
            }

            redisService.set(UserKey.userInfo, username, user);
        }

        return user;
    }

    public Page<User> selectUsersByUserNameAndRoles(UserVo userVo, Integer page, Integer limit){
        PageHelper.startPage(page, limit);
        Page<User> users = mapper.selectUserByUsernameAndRoles(userVo.getUsername(), userVo.getRoles());
        users.forEach(user -> {
            user.setRoleList(roleMapper.selectRolesByUserId(user.getId()));
        });
        return users;
    }

}
