package com.gyx.superscheduled.core.RunnableInterceptor.strengthen;

import com.gyx.superscheduled.common.utils.SerializableUtils;
import com.gyx.superscheduled.common.utils.proxy.Point;
import com.gyx.superscheduled.model.ScheduledLog;

import java.lang.reflect.Method;
import java.util.Date;

public class LogStrengthen implements BaseStrengthen {
    private ScheduledLog scheduledLog;

    private String logPath;

    /**
     * 前置强化方法
     *
     * @param bean   bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args   方法参数
     */
    @Override
    public void before(Object bean, Method method, Object[] args) {
        Point point = (Point) bean;
        scheduledLog = new ScheduledLog();
        scheduledLog.setScheduledSource(point.getScheduledSource());
        scheduledLog.setStatrDate(new Date());
        scheduledLog.setSuperScheduledName(point.getSuperScheduledName());
    }

    /**
     * 后置强化方法
     *
     * @param bean   bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args   方法参数
     */
    @Override
    public void after(Object bean, Method method, Object[] args) {
        scheduledLog.setEndDate(new Date());
        scheduledLog.setSuccess(Boolean.TRUE);
        scheduledLog.computingTime();
        SerializableUtils.toIncFile(scheduledLog, logPath, scheduledLog.getFileName());
    }

    /**
     * 异常强化方法
     *
     * @param bean   bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args   方法参数
     */
    @Override
    public void exception(Object bean, Method method, Object[] args) {
        scheduledLog.setSuccess(Boolean.FALSE);
    }

    /**
     * Finally强化方法，出现异常也会执行
     *
     * @param bean   bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args   方法参数
     */
    @Override
    public void afterFinally(Object bean, Method method, Object[] args) {
        scheduledLog.setEndDate(new Date());
        scheduledLog.computingTime();
        scheduledLog.generateFileName();
        if (scheduledLog.getSuccess() != null && !scheduledLog.getSuccess()) {
            SerializableUtils.toIncFile(scheduledLog, logPath, scheduledLog.getFileName());
        }
    }

    public LogStrengthen() {
    }

    public LogStrengthen(String logPath) {
        this.logPath = logPath;
    }
}
