package com.ycj.demo.web.admin.controller;


import com.github.pagehelper.Page;
import com.ycj.demo.domain.User;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.UserService;
import com.ycj.demo.web.admin.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("rePass/{id}")
    public Result updatePwd(@PathVariable Integer id, @RequestBody UserVo userVo){
        userService.rePassById(id, userVo);
        return Result.success();
    }

    @GetMapping("count/{name}")
    public Result selectCount(@PathVariable String name, @RequestParam(required = false) String id){
        return Result.success(userService.selectCountByNameAndNotId(name, id));
    }

    @PostMapping("select")
    public Result selectUsers(@RequestParam Integer page, @RequestParam Integer limit,@RequestBody UserVo userVo){
        Page<User> users = userService.selectUsersByUserNameAndRoles(userVo, page, limit);
        return Result.success(users,(int) users.getTotal());
    }

    @PostMapping
    public Result saveUser(@RequestBody User user){
        int result = userService.saveUser(user);
        return result > 0 ? Result.success() : Result.error("保存失败！");
    }

    @PutMapping("{id}")
    public Result updateUser(@PathVariable Integer id, @RequestBody User user){
        userService.updateUserInfo(user);
        return Result.success();
    }

    @PutMapping("/role")
    public Result updateUserRole(@RequestBody User user){
        userService.updateUserRole(user);
        return Result.success();
    }

    @DeleteMapping("{id}")
    public Result deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
        return Result.success();
    }

}
