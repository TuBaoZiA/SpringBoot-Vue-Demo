package com.ycj.demo.web.admin.component.job;

import cn.hutool.core.util.RandomUtil;
import com.ycj.demo.result.Result;
import com.ycj.demo.web.admin.component.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@Slf4j
public class Job1 implements Job {

    @Override
    public void execute(JobExecutionContext context){
        MyWebSocket.sendInfo("admin", Result.success(RandomUtil.getRandom().nextInt(1, 100)));
    }
}
