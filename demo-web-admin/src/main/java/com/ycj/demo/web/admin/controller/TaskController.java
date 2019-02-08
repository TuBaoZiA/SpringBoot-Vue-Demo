package com.ycj.demo.web.admin.controller;

import com.ycj.demo.domain.TaskInfo;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务管理
 * @author lance
 */
@RestController
@RequestMapping("/jobs")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 任务列表
     * @return
     * */
    @GetMapping
    public Result list(){
        List<TaskInfo> info = taskService.list();
        return Result.success(info, info.size());
    }

    /**
     * 保存定时任务
     * @param info
     */
    @PostMapping
    public Result save(@RequestBody TaskInfo info){
        taskService.addJob(info);
        return Result.success();
    }

    @PutMapping(value = "{id}")
    public Result edit(@PathVariable Integer id, @RequestBody TaskInfo info){
        taskService.edit(info);
        return Result.success();
    }

    /**
     * 删除定时任务
     * @param jobName
     * @param jobGroup
     */
    @DeleteMapping(value="/{jobName}/{jobGroup}")
    public Result delete(@PathVariable String jobName, @PathVariable String jobGroup){
        taskService.delete(jobName, jobGroup);
        return Result.success();
    }

    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     */
    @PutMapping(value="pause/{jobName}/{jobGroup}")
    public Result pause(@PathVariable String jobName, @PathVariable String jobGroup){
        taskService.pause(jobName, jobGroup);
        return Result.success();
    }

    /**
     * 重新开始定时任务
     * @param jobName
     * @param jobGroup
     */
    @PutMapping(value="resume/{jobName}/{jobGroup}")
    public Result resume(@PathVariable String jobName, @PathVariable String jobGroup){
        taskService.resume(jobName, jobGroup);
        return Result.success();
    }
}