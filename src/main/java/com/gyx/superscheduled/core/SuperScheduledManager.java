package com.gyx.superscheduled.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Component
public class SuperScheduledManager {
    @Autowired
    private SuperScheduledConfig superScheduledConfig;

    /**
     * 修改Scheduled的执行周期
     *
     * @param name scheduled的名称
     * @param cron cron表达式
     */
    public void setScheduledCron(String name, String cron) {
        //终止原先的任务
        cancelScheduled(name);
        //创建新的任务
        addScheduled(name, cron);
    }

    /**
     * 查询所有启动的Scheduled
     */
    public Set<String> getRunScheduledName() {
        return superScheduledConfig.getNameToScheduledFuture().keySet();
    }

    /**
     * 查询所有的Scheduled
     */
    public Set<String> getAllSuperScheduledName() {
        return superScheduledConfig.getNameToRunnable().keySet();
    }

    /**
     * 终止Scheduled
     *
     * @param name scheduled的名称
     */
    public void cancelScheduled(String name) {
        ScheduledFuture scheduledFuture = superScheduledConfig.getScheduledFuture(name);
        scheduledFuture.cancel(true);
        superScheduledConfig.removeScheduledFuture(name);
    }

    /**
     * 启动Scheduled
     *
     * @param name scheduled的名称
     * @param cron cron表达式
     */
    public void addScheduled(String name, String cron) {
        Runnable runnable = superScheduledConfig.getRunnable(name);
        ThreadPoolTaskScheduler taskScheduler = superScheduledConfig.getTaskScheduler();

        CronTrigger newCronTrigger = new CronTrigger(cron);
        ScheduledFuture<?> schedule = taskScheduler.schedule(runnable, newCronTrigger);
        superScheduledConfig.setCronTrigger(name, newCronTrigger);
        superScheduledConfig.setScheduledFuture(name, schedule);
    }

    /**
     * 手动执行异常Scheduled
     *
     * @param name scheduled的名称
     */
    public void runScheduled(String name) {
        Runnable runnable = superScheduledConfig.getRunnable(name);
        runnable.run();
    }
}
