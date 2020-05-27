package com.gyx.superscheduled.core.strategy;

import com.gyx.superscheduled.model.ScheduledSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.FixedRateTask;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class FixedRateStrategy implements ScheduleStrategy {
    @Override
    public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler, ScheduledSource scheduledSource, Runnable runnable) {
        Long fixedRate = scheduledSource.getFixedRate() == null ? Long.valueOf(scheduledSource.getFixedRateString()) : scheduledSource.getFixedRate();
        Long initialDelay = scheduledSource.getInitialDelay() == null ? Long.valueOf(scheduledSource.getInitialDelayString()) : scheduledSource.getInitialDelay();
        if (initialDelay == null) {
            initialDelay = 0L;
        }
        FixedRateTask fixedRateTask = new FixedRateTask(runnable, fixedRate, initialDelay);
        Date startTime = new Date(System.currentTimeMillis() + fixedRateTask.getInitialDelay());
        return threadPoolTaskScheduler.scheduleAtFixedRate(runnable, startTime, fixedRateTask.getInterval());
    }
}
