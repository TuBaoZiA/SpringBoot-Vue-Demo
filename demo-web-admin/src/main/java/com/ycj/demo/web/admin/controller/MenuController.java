package com.ycj.demo.web.admin.controller;


import com.github.pagehelper.Page;
import com.ycj.demo.domain.Menu;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/menus/")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("count/{name}")
    public Result selectCountByNameAndNotId(@PathVariable String name, @RequestParam(required = false) String id){
        return Result.success(menuService.selectCountByNameAndNotId(name, id));
    }

    @GetMapping
    public Result select(){
        Page<Menu> menus = menuService.selectAllMenu();
        return Result.success(menus, menus.size());
    }

    @PostMapping
    public Result saveMenu(@RequestBody Menu menu){
        menuService.saveMenu(menu);
        return Result.success();
    }

    @PutMapping("{id}")
    public Result updateMenu(@PathVariable int id,@RequestBody Menu menu){
        menuService.updateMenu(menu);
        return Result.success();
    }

    @DeleteMapping("{id}")
    public Result deleteMenu(@PathVariable int id){
        menuService.deleteMenu(id);
        return Result.success();
    }

}
