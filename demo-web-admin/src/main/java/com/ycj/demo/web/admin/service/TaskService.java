package com.ycj.demo.web.admin.service;

import cn.hutool.core.date.DateUtil;
import com.ycj.demo.domain.TaskInfo;
import com.ycj.demo.exception.BusinessException;
import com.ycj.demo.result.CodeMsg;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 所有任务列表
     * 2016年10月9日上午11:16:59
     */
    public List<TaskInfo> list(){
        List<TaskInfo> list = new ArrayList<>();

        try {
            for(String groupJob: scheduler.getJobGroupNames()){
                for(JobKey jobKey: scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(groupJob))){
                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger: triggers) {
                        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                        JobDetail jobDetail = scheduler.getJobDetail(jobKey);

                        String cronExpression = "", createTime = "";

                        if (trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            cronExpression = cronTrigger.getCronExpression();
                            createTime = cronTrigger.getDescription();
                        }
                        TaskInfo info = new TaskInfo();
                        info.setJobName(jobKey.getName());
                        info.setJobGroup(jobKey.getGroup());
                        info.setJobDescription(jobDetail.getDescription());
                        info.setJobStatus(triggerState.name());
                        info.setCronExpression(cronExpression);
                        info.setCreateTime(createTime);
                        list.add(info);
                    }
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 保存定时任务
     * @param info
     * 2016年10月9日上午11:30:40
     */
    public void addJob(TaskInfo info) {
        String jobName = info.getJobName(),
                jobGroup = info.getJobGroup(),
                cronExpression = info.getCronExpression(),
                jobDescription = info.getJobDescription(),
                createTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        try {
            if (checkExists(jobName, jobGroup)) {
                log.info("===> AddJob fail, job already exist, jobGroup:{}, jobName:{}", jobGroup, jobName);
                throw new BusinessException(CodeMsg.JOB_EXIST.fillArgs(jobName, jobGroup));
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);

            CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withDescription(createTime).withSchedule(schedBuilder).build();


            Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(jobName);
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey).withDescription(jobDescription).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException | ClassNotFoundException e) {
            if(e instanceof SchedulerException){
                throw new BusinessException(CodeMsg.SCHEDULER_EXCEPTION);
            }else if(e instanceof ClassNotFoundException){
                throw new BusinessException(CodeMsg.JOBCLASS_NOTEXIST);
            }
        }
    }

    /**
     * 修改定时任务
     * @param info
     * 2016年10月9日下午2:20:07
     */
    public void edit(TaskInfo info) {
        String jobName = info.getJobName(),
                jobGroup = info.getJobGroup(),
                cronExpression = info.getCronExpression(),
                jobDescription = info.getJobDescription();
        try {
            if (!checkExists(jobName, jobGroup)) {
                throw new BusinessException(CodeMsg.JOB_NOTEXIST.fillArgs(jobName, jobGroup));
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            JobKey jobKey = new JobKey(jobName, jobGroup);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobBuilder().withDescription(jobDescription);
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);

            scheduler.scheduleJob(jobDetail, triggerSet, true);
        } catch (SchedulerException e) {
            throw new BusinessException(CodeMsg.SCHEDULER_EXCEPTION);
        }
    }

    /**
     * 删除定时任务
     * @param jobName
     * @param jobGroup
     * 2016年10月9日下午1:51:12
     */
    public void delete(String jobName, String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                log.info("===> delete, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            throw new BusinessException(CodeMsg.JOB_ERROR.fillArgs(e.getMessage()));
        }
    }

    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     * 2016年10月10日上午9:40:19
     */
    public void pause(String jobName, String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                log.info("===> Pause success, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            throw new BusinessException(CodeMsg.JOB_ERROR.fillArgs(e.getMessage()));
        }
    }

    /**
     * 重新开始任务
     * @param jobName
     * @param jobGroup
     * 2016年10月10日上午9:40:58
     */
    public void resume(String jobName, String jobGroup){
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.resumeTrigger(triggerKey);
                log.info("===> Resume success, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            throw new BusinessException(CodeMsg.JOB_ERROR.fillArgs(e.getMessage()));
        }
    }

    /**
     * 验证是否存在
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     * 2016年10月8日下午5:30:43
     */
    private boolean checkExists(String jobName, String jobGroup) throws SchedulerException{
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }
}
