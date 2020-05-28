package com.gyx.superscheduled.core;

import com.gyx.superscheduled.exception.SuperScheduledException;
import com.gyx.superscheduled.model.ScheduledSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Component
public class SuperScheduledManager {
    protected final Log logger = LogFactory.getLog(getClass());
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        ScheduledSource scheduledSource = superScheduledConfig.getScheduledSource(name);
        scheduledSource.clear();
        scheduledSource.setCron(cron);
        addScheduled(name, scheduledSource);
    }

    /**
     * 修改Scheduled的fixedDelay
     *
     * @param name       scheduled的名称
     * @param fixedDelay 上一次执行完毕时间点之后多长时间再执行
     */
    public void setScheduledFixedDelay(String name, Long fixedDelay) {
        //终止原先的任务
        cancelScheduled(name);
        //创建新的任务
        ScheduledSource scheduledSource = superScheduledConfig.getScheduledSource(name);
        scheduledSource.clear();
        scheduledSource.setFixedDelay(fixedDelay);
        addScheduled(name, scheduledSource);
    }

    /**
     * 修改Scheduled的fixedRate
     *
     * @param name      scheduled的名称
     * @param fixedRate 上一次开始执行之后多长时间再执行
     */
    public void setScheduledFixedRate(String name, Long fixedRate) {
        //终止原先的任务
        cancelScheduled(name);
        //创建新的任务
        ScheduledSource scheduledSource = superScheduledConfig.getScheduledSource(name);
        scheduledSource.clear();
        scheduledSource.setFixedRate(fixedRate);
        addScheduled(name, scheduledSource);
    }

    /**
     * 查询所有启动的Scheduled
     */
    public List<String> getRunScheduledName() {
        Set<String> names = superScheduledConfig.getNameToScheduledFuture().keySet();
        return new ArrayList<>(names);
    }

    /**
     * 查询所有的Scheduled
     */
    public List<String> getAllSuperScheduledName() {
        Set<String> names = superScheduledConfig.getNameToRunnable().keySet();
        return new ArrayList<>(names);
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
        logger.info(df.format(LocalDateTime.now()) + "任务" + name + "已经终止...");
    }

    /**
     * 启动Scheduled
     *
     * @param name            scheduled的名称
     * @param scheduledSource 定时任务的源信息
     */
    public void addScheduled(String name, ScheduledSource scheduledSource) {
        if (getRunScheduledName().contains(name)) {
            throw new SuperScheduledException("定时任务" + name + "已经被启动过了");
        }
        if (!scheduledSource.check()) {
            throw new SuperScheduledException("定时任务" + name + "源数据内容错误");
        }

        scheduledSource.refreshType();

        Runnable runnable = superScheduledConfig.getRunnable(name);
        ThreadPoolTaskScheduler taskScheduler = superScheduledConfig.getTaskScheduler();


        ScheduledFuture<?> schedule = ScheduledFutureFactory.create(taskScheduler, scheduledSource, runnable);
        logger.info(df.format(LocalDateTime.now()) + "任务" + name + "已经启动...");

        superScheduledConfig.addScheduledSource(name, scheduledSource);
        superScheduledConfig.addScheduledFuture(name, schedule);
    }

    /**
     * 以cron类型启动Scheduled
     *
     * @param name scheduled的名称
     * @param cron cron表达式
     */
    public void addCronScheduled(String name, String cron) {
        ScheduledSource scheduledSource = new ScheduledSource();
        scheduledSource.setCron(cron);

        addScheduled(name, scheduledSource);
    }

    /**
     * 以fixedDelay类型启动Scheduled
     *
     * @param name         scheduled的名称
     * @param fixedDelay   上一次执行完毕时间点之后多长时间再执行
     * @param initialDelay 第一次执行的延迟时间
     */
    public void addFixedDelayScheduled(String name, Long fixedDelay, Long... initialDelay) {
        ScheduledSource scheduledSource = new ScheduledSource();
        scheduledSource.setFixedDelay(fixedDelay);
        if (initialDelay != null && initialDelay.length == 1) {
            scheduledSource.setInitialDelay(initialDelay[0]);
        } else if (initialDelay != null && initialDelay.length > 1) {
            throw new SuperScheduledException("第一次执行的延迟时间只能传入一个参数");
        }

        addScheduled(name, scheduledSource);
    }

    /**
     * 以fixedRate类型启动Scheduled
     *
     * @param name         scheduled的名称
     * @param fixedRate    上一次开始执行之后多长时间再执行
     * @param initialDelay 第一次执行的延迟时间
     */
    public void addFixedRateScheduled(String name, Long fixedRate, Long... initialDelay) {
        ScheduledSource scheduledSource = new ScheduledSource();
        scheduledSource.setFixedRate(fixedRate);
        if (initialDelay != null && initialDelay.length == 1) {
            scheduledSource.setInitialDelay(initialDelay[0]);
        } else if (initialDelay != null && initialDelay.length > 1) {
            throw new SuperScheduledException("第一次执行的延迟时间只能传入一个参数");
        }

        addScheduled(name, scheduledSource);
    }

    /**
     * 手动执行一次任务
     *
     * @param name scheduled的名称
     */
    public void runScheduled(String name) {
        Runnable runnable = superScheduledConfig.getRunnable(name);
        runnable.run();
    }
}
