package com.ycj.demo.web.admin.component.shiro;

import com.ycj.demo.domain.User;
import com.ycj.demo.web.admin.component.RedisService;
import com.ycj.demo.web.admin.util.redis.key.TokenKey;
import com.ycj.demo.web.admin.util.redis.key.UserKey;
import com.ycj.demo.result.CodeMsg;
import com.ycj.demo.web.admin.service.UserService;
import com.ycj.demo.web.admin.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Shiro自定义Realm
 */
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * arg0相当于登录用户输入创建的令牌,认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken arg0) throws AuthenticationException {
        System.out.println("-----------登录方法被调用------------");

        String token = (String) arg0.getCredentials();

        // 解密获得username，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException(CodeMsg.LOGIN_ERROR.getMsg());
        }

        User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new AuthenticationException(CodeMsg.LOGIN_ERROR.getMsg());
        }

        if (!JwtUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException(CodeMsg.LOGIN_ERROR.getMsg());
        }

        if(null == redisService.get(TokenKey.TOKEN, token, String.class)){
            throw new AuthenticationException(CodeMsg.UNAUTHENTICATED.getMsg());
        }

        redisService.set(TokenKey.TOKEN, token, token);

        return new SimpleAuthenticationInfo(username, token, getName());
    }

    /**
     * 赋权 arg0保存了用户的认证信息,授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        System.out.println("---------授权方法被调用-----------");

        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();

        User user = redisService.get(UserKey.userInfo, arg0.toString(), User.class);

        info.addRoles(user.getRoles());
        info.addStringPermissions(user.getPermissions());

        return info;
    }

}
