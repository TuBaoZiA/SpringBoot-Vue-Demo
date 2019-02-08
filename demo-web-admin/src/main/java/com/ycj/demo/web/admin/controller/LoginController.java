package com.ycj.demo.web.admin.controller;

import com.ycj.demo.domain.User;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录 游客身份才能访问
     * @param user
     * @param rememberMe
     */
    @PostMapping(value = "login")
    @RequiresGuest
    public Result login(@RequestBody User user, @RequestParam(required = false) boolean rememberMe){
        return Result.success(userService.sign(user.getUsername(), user.getPassword()));
    }

    /**
     * 根据token查询用户信息
     * @param token
     */
    @GetMapping("{token}")
    public Result getInfo(@PathVariable String token){
        return Result.success(userService.getUserInfoByToken(token));
    }

    @PostMapping("reset/{username}")
    public Result resetUserInfo(@PathVariable String username){
        userService.resetUserInfo(username);
        return Result.success();
    }

    @RequestMapping(value="logout/{token}")
    public Result logout(@PathVariable String token){
        userService.logout(token);
        return Result.success();
    }

}
