/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import static com.google.common.base.Preconditions.checkState;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class CronTrigMod {

    private static final Logger log = LoggerFactory.getLogger(CronTrigMod.class);

    public Map changedJobKey;

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    private Scheduler getScheduler() throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        if (scheduler == null || !scheduler.isStarted()) {
            throw new SchedulerException("Scheduler is not started");
        }
        return scheduler;
    }

    public List<JobKey> getJobKeys() throws SchedulerException {
        return new ArrayList(getScheduler().getJobKeys(GroupMatcher.anyJobGroup()));
    }

    public List<JobKey> getJobKeys(String jobGroup) throws SchedulerException {
        return new ArrayList(getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(jobGroup)));
    }

    public List<TriggerKey> getTriggerKeys(String jobGroup) throws SchedulerException {
        return new ArrayList(getScheduler().getTriggerKeys(GroupMatcher.triggerGroupEquals(jobGroup)));
    }

    public JobKey createJobKey(String jobName) {
        return new JobKey(jobName);
    }

    public JobKey createJobKey(String jobName, String groupName) {
        return new JobKey(jobName, groupName);
    }

    public TriggerKey createTriggerKey(String triggerName) {
        return new TriggerKey(triggerName);
    }

    public TriggerKey createTriggerKey(String triggerName, String groupName) {
        return new TriggerKey(triggerName, groupName);
    }

    public JobDetail createJobDetail(JobKey jobKey, String jobGroup, Class jobClass, Map<String, Object> jobData) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .usingJobData(new JobDataMap(jobData))
                .build();
    }

    public String getCronTriggerByJobKey(String jobKeyName) {
        try {
            Trigger t = getScheduler().getTrigger(new TriggerKey(jobKeyName));
            return ((CronTrigger) t).getCronExpression();
        } catch (SchedulerException ex) {
            log.error(ex.toString());
            return null;
        }
    }

    public void triggerJob(JobKey jobKey) {
        try {
            getScheduler().triggerJob(jobKey);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    public boolean updateCronExpression(String triggerKey, String cronExpression, Integer executeNow) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        if (StringUtils.isBlank(triggerKey) || StringUtils.isBlank(cronExpression)) {
            log.error("参数错误");
            return false;
        }
        if (executeNow == null) {
            executeNow = 0;
        }
        triggerKey = triggerKey.trim();
        cronExpression = cronExpression.trim();
        TriggerKey key = new TriggerKey(triggerKey);//动态条件  
        try {
            Trigger oldTrigger = scheduler.getTrigger(key);
            if (oldTrigger instanceof CronTriggerImpl) {
                CronTriggerImpl trigger = (CronTriggerImpl) oldTrigger;
                trigger.setCronExpression(cronExpression);//动态传入的条件  
                //不立即执行  
                if (executeNow == 0) {
                    trigger.setStartTime(new Date());//防止立即生效  
                }
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }
        } catch (SchedulerException | ParseException e) {
            log.error("更新cron定时任务运行时间失败[triggerKey=" + triggerKey + "]:", e);
            return false;
        }
        return true;
    }

    public void scheduleJob(Class jobClass, String jobName, String crontrigger) throws SchedulerException {
        this.scheduleJob(jobClass, this.createJobKey(jobName), this.createTriggerKey(jobName), crontrigger);
    }

    public void scheduleJob(Class jobClass, JobKey jobKey, TriggerKey trigKey, String crontrigger) throws SchedulerException {

//        System.out.println("jobClass: " + jobClass);
//        System.out.println("jobKey: " + jobKey);
//        System.out.println("trigKey: " + trigKey);
//        System.out.println("crontrigger: " + crontrigger);
        if (!isKeyInScheduleExist(jobKey)) {
            JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(trigKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(crontrigger)).build();
            getScheduler().scheduleJob(job, trigger);
            log.info("The job with key name " + jobKey + " ,TriggerKey " + trigKey + " is sched");
        } else {
            log.info("The job with key name " + jobKey + " is exist.");
        }

        System.out.println("ScheduleJob finish");
    }

    public void scheduleJob(JobDetail job, TriggerKey trigKey, String crontrigger) throws SchedulerException {
        JobKey jobKey = job.getKey();
        if (!isKeyInScheduleExist(jobKey)) {
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(trigKey)
                    .withSchedule(CronScheduleBuilder.cronSchedule(crontrigger)).build();
            getScheduler().scheduleJob(job, trigger);
            log.info("The job with key name " + jobKey + " ,TriggerKey " + trigKey + " is sched");
        } else {
            log.info("The job with key name " + jobKey + " is already exist.");
            throw new SchedulerException("The job with key name " + jobKey + " is already exist.");
        }
    }

    public void scheduleJob(String triggerName) throws SchedulerException {
        JobDetail jobDetail;
        Trigger trigger;
        try {
            jobDetail = (JobDetail) ApplicationContextHelper.getBean(triggerName);
            trigger = (Trigger) ApplicationContextHelper.getBean(triggerName + "-Trig");
            checkState(jobDetail != null, "Can't find jobDetail name " + triggerName);
            checkState(trigger != null, "Can't find trigger name " + triggerName + "-Trig");
            if (!this.isJobInScheduleExist(jobDetail.getKey())) {
                this.getScheduler().scheduleJob(jobDetail, trigger);
                log.info("The job with name " + triggerName + " is sched.");
            }else{
                log.info("The job with name " + triggerName + "  is already exist.");
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void jobPause(JobKey jobKey) throws SchedulerException {
        getScheduler().pauseJob(jobKey);
        log.info("The job with key name " + jobKey + "is already pause.");
    }

    public void interruptJob(JobKey jobKey) throws SchedulerException {
        getScheduler().interrupt(jobKey);
        log.info("Job " + jobKey + "  -- INTERRUPTING --");
    }

    public void removeJob(String jobName) throws SchedulerException {
        Scheduler scheduler = this.getScheduler();
        JobKey jobKey = this.createJobKey(jobName); 
        scheduler.deleteJob(jobKey);
        log.info("The job with name " + jobName + (scheduler.checkExists(jobKey) ? " remove fail." : " remove success."));
    }

    public void removeJob(JobKey jobKey) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        scheduler.deleteJob(jobKey);
        log.info("The job with key name " + jobKey + (scheduler.checkExists(jobKey) ? " remove fail." : " remove success."));
    }

    public void removeJobs(String jobGroupName) throws SchedulerException {
        Set<JobKey> jobs = getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName));
        this.removeJobs(new ArrayList(jobs));
        log.info("Job group " + jobGroupName + " remove " + (this.getJobKeys(jobGroupName).isEmpty() ? "success" : "fail"));
    }

    public void removeJobs(List<JobKey> l) throws SchedulerException {
        getScheduler().deleteJobs(l);
    }

    public void removeTriggers(String jobGroupName) throws SchedulerException {
        Set<TriggerKey> triggers = getScheduler().getTriggerKeys(GroupMatcher.triggerGroupEquals(jobGroupName));
        this.removeTriggers(new ArrayList(triggers));
        log.info("Trigger group " + jobGroupName + " remove " + (this.getTriggerKeys(jobGroupName).isEmpty() ? "success" : "fail"));
    }

    public void removeTrigger(TriggerKey triggerKey) throws SchedulerException {
        getScheduler().unscheduleJob(triggerKey);
    }

    public void removeTriggers(List<TriggerKey> l) throws SchedulerException {
        getScheduler().unscheduleJobs(l);
    }

    public boolean isJobInScheduleExist(JobKey jobKey) throws SchedulerException {
        return this.isKeyInScheduleExist(jobKey);
    }

    public boolean isKeyInScheduleExist(Object key) throws SchedulerException {
        Scheduler scheduler = getScheduler();
        if (key instanceof JobKey) {
            return scheduler.checkExists((JobKey) key);
        } else if (key instanceof TriggerKey) {
            return scheduler.checkExists((TriggerKey) key);
        } else if (key instanceof String) {
            return scheduler.checkExists(new JobKey((String) key));
        } else {
            return false;
        }
    }

    public void unScheduleAllJob() throws SchedulerException {
        this.getScheduler().clear();
    }

}
