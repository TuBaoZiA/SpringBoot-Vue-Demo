package com.ycj.demo.web.admin.controller;


import com.github.pagehelper.Page;
import com.ycj.demo.domain.TestInnoDB;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.TestInnoDBService;
import com.ycj.demo.web.admin.vo.TestVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "TestController", description = "测试接口")
@RestController
@RequestMapping("/admin/testInnoDB/")
public class TestInnoDBController {

    @Autowired
    TestInnoDBService testInnoDBService;

    @ApiOperation(value="获取用户列表", notes="***")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "limit", value = "用户详细实体user", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "test", value = "用户详细实体user", required = false, dataType = "TestVo")
    })
    @PostMapping("select")
    public Result select(@RequestParam Integer page, @RequestParam Integer limit,@RequestBody TestVo test){
        Page<TestInnoDB> tests = testInnoDBService.selectByExample(test, page, limit);
        return Result.success(tests, (int) tests.getTotal());
    }

    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id){
        int result = testInnoDBService.deleteByPrimaryKey(id);
        return result > 0 ? Result.success() : Result.error("删除失败！");
    }

    @PostMapping("{id}")
    public Result update(@RequestBody TestInnoDB user){
        int result = testInnoDBService.updateSelective(user);
        return result > 0 ? Result.success() : Result.error("修改失败！");
    }

    @PutMapping("insert")
    public Result insert(@RequestBody TestInnoDB user){
        int id = testInnoDBService.insert(user);
        return id > 0 ? Result.success(id) : Result.error("添加失败！");
    }

}
