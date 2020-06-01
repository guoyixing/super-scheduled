package com.gyx.superscheduled.core.RunnableInterceptor;

import com.gyx.superscheduled.exception.SuperScheduledException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SuperScheduledRunnable {
    private Method method;
    private Object bean;
    private String superScheduledName;


    public void invoke() {
        try {
            method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new SuperScheduledException(e.getLocalizedMessage());
        }
    }

    public SuperScheduledRunnable() {
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public String getSuperScheduledName() {
        return superScheduledName;
    }

    public void setSuperScheduledName(String superScheduledName) {
        this.superScheduledName = superScheduledName;
    }
}
