package com.gyx.superscheduled.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component("superScheduledConfig")
public class SuperScheduledConfig {
    protected final Log logger = LogFactory.getLog(getClass());

    private ThreadPoolTaskScheduler taskScheduler;

    private Map<String, ScheduledFuture> nameToScheduledFuture = new ConcurrentHashMap<>();

    private Map<String, Runnable> nameToRunnable = new ConcurrentHashMap<>();

    private Map<String, CronTrigger> nameToCronTrigger = new ConcurrentHashMap<>();

    public CronTrigger getCronTrigger(String name){
        return nameToCronTrigger.get(name);
    }

    public void setScheduledFuture(String name,ScheduledFuture scheduledFuture){
        this.nameToScheduledFuture.put(name,scheduledFuture);
    }

    public void setCronTrigger(String name,CronTrigger cronTrigger){
        this.nameToCronTrigger.put(name,cronTrigger);
    }

    public Runnable getRunnable(String name){
        return nameToRunnable.get(name);
    }

    public ScheduledFuture getScheduledFuture(String name){
        return nameToScheduledFuture.get(name);
    }

    public void addScheduledFuture(String name, ScheduledFuture scheduledFuture) {
        this.nameToScheduledFuture.put(name, scheduledFuture);
    }

    public void addRunnable(String name, Runnable runnable) {
        this.nameToRunnable.put(name, runnable);
    }

    public void addCronTrigger(String name, CronTrigger cronTrigger) {
        this.nameToCronTrigger.put(name, cronTrigger);
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

    public Map<String, CronTrigger> getNameToCronTrigger() {
        return nameToCronTrigger;
    }

    public void setNameToCronTrigger(Map<String, CronTrigger> nameToCronTrigger) {
        this.nameToCronTrigger = nameToCronTrigger;
    }

    public void removeScheduledFuture(String name) {
        nameToScheduledFuture.remove(name);
    }
}
