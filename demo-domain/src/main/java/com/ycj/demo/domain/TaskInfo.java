package com.ycj.demo.domain;

import lombok.Data;

@Data
public class TaskInfo {

    private int id = 0;

    /**任务名称*/
    private String jobName;

    /**任务分组*/
    private String jobGroup;

    /**任务描述*/
    private String jobDescription;

    /**任务状态*/
    private String jobStatus;

    /**任务表达式*/
    private String cronExpression;

    private String createTime;

}
