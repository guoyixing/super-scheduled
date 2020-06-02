package com.gyx.superscheduled.core.RunnableInterceptor;

import com.gyx.superscheduled.common.utils.proxy.Chain;
import com.gyx.superscheduled.common.utils.proxy.Point;
import com.gyx.superscheduled.exception.SuperScheduledException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SuperScheduledRunnable {
    private Method method;
    private Object bean;
    private Chain chain;


    public Object invoke() {
        Object result;
        if (chain.incIndex() == chain.getList().size()) {
            try {
                chain.resetIndex();
                result = method.invoke(bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new SuperScheduledException(e.getLocalizedMessage());
            }
        } else {
            Point point = chain.getList().get(chain.getIndex());
            result = point.invoke(this);
        }
        return result;
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

    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }
}
