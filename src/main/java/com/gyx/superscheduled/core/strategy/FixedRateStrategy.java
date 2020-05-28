package com.gyx.superscheduled.core.strategy;

import com.gyx.superscheduled.model.ScheduledSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.FixedRateTask;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class FixedRateStrategy implements ScheduleStrategy {
    @Override
    public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler, ScheduledSource scheduledSource, Runnable runnable) {
        Long fixedRate = scheduledSource.getFixedRateString() == null ? scheduledSource.getFixedRate() : Long.valueOf(scheduledSource.getFixedRateString());
        Long initialDelay = scheduledSource.getInitialDelayString() == null ? scheduledSource.getInitialDelay() : Long.valueOf(scheduledSource.getInitialDelayString());
        if (initialDelay == null) {
            initialDelay = 0L;
        }
        FixedRateTask fixedRateTask = new FixedRateTask(runnable, fixedRate, initialDelay);
        Date startTime = new Date(System.currentTimeMillis() + fixedRateTask.getInitialDelay());
        return threadPoolTaskScheduler.scheduleAtFixedRate(runnable, startTime, fixedRateTask.getInterval());
    }
}
