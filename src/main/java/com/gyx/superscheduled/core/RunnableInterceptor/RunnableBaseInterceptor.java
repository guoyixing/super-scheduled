package com.gyx.superscheduled.core.RunnableInterceptor;

import com.gyx.superscheduled.core.RunnableInterceptor.strengthen.BaseStrengthen;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class RunnableBaseInterceptor implements MethodInterceptor {


    private BaseStrengthen strengthen;


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        Object result;
        if ("invoke".equals(method.getName())) {
            strengthen.before(obj,method,args);
            result = methodProxy.invokeSuper(obj, args);
            strengthen.after(obj,method,args);
        }else {
            result = methodProxy.invokeSuper(obj, args);
        }
        return result;
    }

    public RunnableBaseInterceptor(BaseStrengthen strengthen) {
        this.strengthen = strengthen;
    }

    public RunnableBaseInterceptor() {
    }
}
