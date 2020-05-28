package com.gyx.superscheduled.core.strategy;

import com.gyx.superscheduled.model.ScheduledSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.FixedDelayTask;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class FixedDelayStrategy implements ScheduleStrategy {
    @Override
    public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler, ScheduledSource scheduledSource, Runnable runnable) {
        Long fixedDelay = scheduledSource.getFixedDelayString() == null ? scheduledSource.getFixedDelay() : Long.valueOf(scheduledSource.getFixedDelayString());
        Long initialDelay = scheduledSource.getInitialDelayString() == null ? scheduledSource.getInitialDelay() : Long.valueOf(scheduledSource.getInitialDelayString());
        if (initialDelay == null) {
            initialDelay = 0L;
        }
        FixedDelayTask fixedDelayTask = new FixedDelayTask(runnable, fixedDelay, initialDelay);
        Date startTime = new Date(System.currentTimeMillis() + fixedDelayTask.getInitialDelay());
        return threadPoolTaskScheduler.scheduleWithFixedDelay(runnable, startTime, fixedDelayTask.getInterval());
    }
}
