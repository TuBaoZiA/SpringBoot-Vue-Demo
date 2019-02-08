package com.ycj.demo.web.admin.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

@Slf4j
public class Job2 implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        log.info("JobName2: {}", context.getJobDetail().getKey().getName());
    }
}
