package com.ycj.demo.web.admin.controller;


import com.github.pagehelper.Page;
import com.ycj.demo.domain.Role;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/roles/")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("count/{name}")
    public Result selectCountByNameAndNotId(@PathVariable String name, @RequestParam(required = false) String id){
        return Result.success(roleService.selectCountByNameAndNotId(name, id));
    }

    @GetMapping
    public Result select(@RequestParam Integer page, @RequestParam Integer limit, @RequestParam String roleName){
        Page<Role> roles = roleService.selectRoleByName(page, limit, roleName);
        return Result.success(roles, (int) roles.getTotal());
    }

    @PostMapping
    public Result saveRole(@RequestBody Role role){
        roleService.saveRole(role);
        return Result.success();
    }

    @PutMapping("{id}")
    public Result updateRole(@PathVariable int id,@RequestBody Role role){
        roleService.updateRole(role);
        return Result.success();
    }

    @DeleteMapping("{id}")
    public Result deleteRole(@PathVariable int id){
        roleService.deleteRole(id);
        return Result.success();
    }

}
