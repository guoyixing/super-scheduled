package com.gyx.superscheduled.core;

import com.gyx.superscheduled.model.ScheduledSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component("superScheduledConfig")
public class SuperScheduledConfig {
    /**
     * 执行定时任务的线程池
     */
    private ThreadPoolTaskScheduler taskScheduler;

    /**
     * 定时任务名称与定时任务回调钩子  的关联关系容器
     */
    private Map<String, ScheduledFuture> nameToScheduledFuture = new ConcurrentHashMap<>();

    /**
     * 定时任务名称与定时任务需要执行的逻辑  的关联关系容器
     */
    private Map<String, Runnable> nameToRunnable = new ConcurrentHashMap<>();

    /**
     * 定时任务名称与定时任务的源信息  的关联关系容器
     */
    private Map<String, ScheduledSource> nameToScheduledSource = new ConcurrentHashMap<>();


    public void addScheduledSource(String name, ScheduledSource scheduledSource) {
        this.nameToScheduledSource.put(name, scheduledSource);
    }

    public ScheduledSource getScheduledSource(String name) {
        return nameToScheduledSource.get(name);
    }

    public Runnable getRunnable(String name) {
        return nameToRunnable.get(name);
    }

    public ScheduledFuture getScheduledFuture(String name) {
        return nameToScheduledFuture.get(name);
    }

    public void addScheduledFuture(String name, ScheduledFuture scheduledFuture) {
        this.nameToScheduledFuture.put(name, scheduledFuture);
    }

    public void addRunnable(String name, Runnable runnable) {
        this.nameToRunnable.put(name, runnable);
    }

    public ThreadPoolTaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public void setTaskScheduler(ThreadPoolTaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public Map<String, ScheduledFuture> getNameToScheduledFuture() {
        return nameToScheduledFuture;
    }

    public void setNameToScheduledFuture(Map<String, ScheduledFuture> nameToScheduledFuture) {
        this.nameToScheduledFuture = nameToScheduledFuture;
    }

    public Map<String, Runnable> getNameToRunnable() {
        return nameToRunnable;
    }

    public void setNameToRunnable(Map<String, Runnable> nameToRunnable) {
        this.nameToRunnable = nameToRunnable;
    }

    public void removeScheduledFuture(String name) {
        nameToScheduledFuture.remove(name);
    }

    public Map<String, ScheduledSource> getNameToScheduledSource() {
        return nameToScheduledSource;
    }

    public void setNameToScheduledSource(Map<String, ScheduledSource> nameToScheduledSource) {
        this.nameToScheduledSource = nameToScheduledSource;
    }
}
