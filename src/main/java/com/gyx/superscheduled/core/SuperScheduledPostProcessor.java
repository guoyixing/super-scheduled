package com.gyx.superscheduled.core;

import com.gyx.superscheduled.core.RunnableInterceptor.RunnableBaseInterceptor;
import com.gyx.superscheduled.core.RunnableInterceptor.SuperScheduledRunnable;
import com.gyx.superscheduled.core.RunnableInterceptor.strengthen.ExecutionFlagStrengthen;
import com.gyx.superscheduled.exception.SuperScheduledException;
import com.gyx.superscheduled.model.ScheduledSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.gyx.superscheduled.common.utils.AnnotationUtils.changeAnnotationValue;

@DependsOn("superScheduledConfig")
@Component
public class SuperScheduledPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    protected final Log logger = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean,
                                                  String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean,
                                                 String beanName) throws BeansException {
        SuperScheduledConfig superScheduledConfig = applicationContext.getBean(SuperScheduledConfig.class);

        //获取bean的方法
        Method[] methods = bean.getClass().getDeclaredMethods();
        if (methods.length > 0) {
            for (Method method : methods) {
                Scheduled annotation = method.getAnnotation(Scheduled.class);
                if (annotation == null) {
                    continue;
                }
                ScheduledSource scheduledSource = new ScheduledSource(annotation);
                if (!scheduledSource.check()) {
                    throw new SuperScheduledException("在" + beanName + "Bean中" + method.getName() + "方法的注解参数错误");
                }
                String name = beanName + "." + method.getName();
                //TODO 这里不够优雅
                ExecutionFlagStrengthen executionFlagStrengthen = new ExecutionFlagStrengthen(name);
                //创建代理
                SuperScheduledRunnable proxy = getInstance(SuperScheduledRunnable.class,new RunnableBaseInterceptor(executionFlagStrengthen));
                proxy.setMethod(method);
                proxy.setBean(bean);
                superScheduledConfig.addScheduledSource(name, scheduledSource);
                try {
                    clearOriginalScheduled(annotation);
                } catch (Exception e) {
                    throw new SuperScheduledException("在关闭原始方法" + beanName + method.getName() + "时出现错误");
                }
                superScheduledConfig.addRunnable(name, proxy::invoke);
            }
        }
        return bean;
    }

    private void clearOriginalScheduled(Scheduled annotation) throws Exception {
        changeAnnotationValue(annotation, "cron", Scheduled.CRON_DISABLED);
        changeAnnotationValue(annotation, "fixedDelay", -1L);
        changeAnnotationValue(annotation, "fixedDelayString", "");
        changeAnnotationValue(annotation, "fixedRate", -1L);
        changeAnnotationValue(annotation, "fixedRateString", "");
        changeAnnotationValue(annotation, "initialDelay", -1L);
        changeAnnotationValue(annotation, "initialDelayString", "");
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T getInstance(Class<T> clazz, MethodInterceptor interceptor) {
        //字节码加强器：用来创建动态代理类
        Enhancer enhancer = new Enhancer();
        //代理的目标对象
        enhancer.setSuperclass(clazz);
        //回调类，在代理类方法调用时会回调Callback类的intercept方法
        enhancer.setCallback(interceptor);
        //创建代理类
        Object result = enhancer.create();
        return (T)result;
    }
}