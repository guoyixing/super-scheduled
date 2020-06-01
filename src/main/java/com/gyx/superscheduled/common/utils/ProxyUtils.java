package com.gyx.superscheduled.common.utils;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class ProxyUtils {
    public static <T> T getInstance(Class<T> clazz, MethodInterceptor interceptor) {
        //字节码加强器：用来创建动态代理类
        Enhancer enhancer = new Enhancer();
        //代理的目标对象
        enhancer.setSuperclass(clazz);
        //回调类，在代理类方法调用时会回调Callback类的intercept方法
        enhancer.setCallback(interceptor);
        //创建代理类
        Object result = enhancer.create();
        return (T) result;
    }
}
