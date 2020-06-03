package com.gyx.superscheduled.common.utils.proxy;

import com.gyx.superscheduled.core.RunnableInterceptor.SuperScheduledRunnable;
import com.gyx.superscheduled.model.ScheduledSource;

public abstract class Point {
    /**
     * 定时任务名
     */
    private String superScheduledName;

    private ScheduledSource scheduledSource;

    /**
     * 抽象的执行方法，使用代理实现
     * @param runnable 定时任务执行器
     */
    public abstract Object invoke(SuperScheduledRunnable runnable);

    public String getSuperScheduledName() {
        return superScheduledName;
    }

    public void setSuperScheduledName(String superScheduledName) {
        this.superScheduledName = superScheduledName;
    }

    public ScheduledSource getScheduledSource() {
        return scheduledSource;
    }

    public void setScheduledSource(ScheduledSource scheduledSource) {
        this.scheduledSource = scheduledSource;
    }
}