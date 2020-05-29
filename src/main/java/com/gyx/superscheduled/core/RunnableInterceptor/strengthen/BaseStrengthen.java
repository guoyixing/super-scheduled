package com.gyx.superscheduled.core.RunnableInterceptor.strengthen;

import java.lang.reflect.Method;

public interface BaseStrengthen {
    /**
     * 前置强化方法
     * @param bean bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args 方法参数
     */
    void before(Object bean, Method method, Object[] args);

    /**
     * 后置强化方法
     * @param bean bean实例（或者是被代理的bean）
     * @param method 执行的方法对象
     * @param args 方法参数
     */
    void after(Object bean, Method method, Object[] args);
}
