package com.ycj.demo.web.admin.controller;


import com.github.pagehelper.Page;
import com.ycj.demo.domain.Permission;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/permissions/")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /* @GetMapping("count/{name}")
    public Result selectCountByNameAndNotId(@PathVariable String name, @RequestParam(required = false) String id){
        return Result.success(menuService.selectCountByNameAndNotId(name, id));
    }*/

    @GetMapping
    public Result select(@RequestParam Integer page, @RequestParam Integer limit){
        Page<Permission> permissions = (Page<Permission>) permissionService.selectAll(page, limit);
        return Result.success(permissions, permissions.size());
    }

    @PostMapping
    public Result savePermission(@RequestBody Permission permission){
        permissionService.insert(permission);
        return Result.success();
    }

    @PutMapping("{id}")
    public Result updatePermission(@PathVariable int id,@RequestBody Permission permission){
        permissionService.updateSelective(permission);
        return Result.success();
    }

    @DeleteMapping("{id}")
    public Result deletePermission(@PathVariable int id){
        permissionService.deletePermission(id);
        return Result.success();
    }

}
