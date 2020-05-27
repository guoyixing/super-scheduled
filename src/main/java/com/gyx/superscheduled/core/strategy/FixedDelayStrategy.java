package com.gyx.superscheduled.core.strategy;

import com.gyx.superscheduled.model.ScheduledSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.FixedDelayTask;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class FixedDelayStrategy implements ScheduleStrategy {
    @Override
    public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler, ScheduledSource scheduledSource, Runnable runnable) {
        Long fixedDelay = scheduledSource.getFixedDelay() == null ? Long.valueOf(scheduledSource.getFixedDelayString()) : scheduledSource.getFixedDelay();
        Long initialDelay = scheduledSource.getInitialDelay() == null ? Long.valueOf(scheduledSource.getInitialDelayString()) : scheduledSource.getInitialDelay();
        if (initialDelay == null){
            initialDelay = 0L;
        }
        FixedDelayTask fixedDelayTask = new FixedDelayTask(runnable, fixedDelay, initialDelay);
        Date startTime = new Date(System.currentTimeMillis() + fixedDelayTask.getInitialDelay());
        return threadPoolTaskScheduler.scheduleWithFixedDelay(runnable, startTime, fixedDelayTask.getInterval());
    }
}
