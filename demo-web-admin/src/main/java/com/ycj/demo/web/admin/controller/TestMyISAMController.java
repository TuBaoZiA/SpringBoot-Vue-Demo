package com.ycj.demo.web.admin.controller;


import com.github.pagehelper.Page;
import com.ycj.demo.domain.TestMyISAM;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.TestMyISAMService;
import com.ycj.demo.web.admin.vo.TestVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/testMyISAM/")
public class TestMyISAMController {

    @Autowired
    TestMyISAMService testService;

    @PostMapping("select")
    public Result select(@RequestParam Integer page, @RequestParam Integer limit,@RequestBody(required = false) TestVo test){
        Page<TestMyISAM> tests = testService.selectByExample(test, page, limit);
        return Result.success(tests, (int) tests.getTotal());
    }

    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id){
        int result = testService.deleteByPrimaryKey(id);
        return result > 0 ? Result.success() : Result.error("删除失败！");
    }

    @PostMapping("{id}")
    public Result update(@RequestBody TestMyISAM user){
        int result = testService.updateSelective(user);
        return result > 0 ? Result.success() : Result.error("修改失败！");
    }

    @PutMapping("insert")
    public Result insert(@RequestBody TestMyISAM user){
        int id = testService.insert(user);
        return id > 0 ? Result.success(id) : Result.error("添加失败！");
    }

}
