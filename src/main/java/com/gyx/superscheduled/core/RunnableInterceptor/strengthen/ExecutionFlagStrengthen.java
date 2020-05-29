package com.gyx.superscheduled.core.RunnableInterceptor.strengthen;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

public class ExecutionFlagStrengthen implements BaseStrengthen{
    private String superScheduledName;
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * 前置强化方法
     * @param bean bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args 方法参数
     */
    @Override
    public void before(Object bean, Method method, Object[] args){
        logger.info("定时任务" + superScheduledName + "开始执行");
    }
    /**
     * 后置强化方法
     * @param bean bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args 方法参数
     */
    @Override
    public void after(Object bean, Method method, Object[] args){
        logger.info("定时任务" + superScheduledName + "开始执行");
    }

    public ExecutionFlagStrengthen(String superScheduledName) {
        this.superScheduledName = superScheduledName;
    }

    public ExecutionFlagStrengthen() {
    }
}
