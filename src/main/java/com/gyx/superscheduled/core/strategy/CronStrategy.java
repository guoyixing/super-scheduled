package com.gyx.superscheduled.core.strategy;

import com.gyx.superscheduled.model.ScheduledSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

public class CronStrategy implements ScheduleStrategy {
    @Override
    public ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler, ScheduledSource scheduledSource, Runnable runnable) {
        return threadPoolTaskScheduler.schedule(runnable, new CronTrigger(scheduledSource.getCron()));
    }
}
