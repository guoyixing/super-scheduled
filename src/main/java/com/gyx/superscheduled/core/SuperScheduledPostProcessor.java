package com.gyx.superscheduled.core;

import com.gyx.superscheduled.exception.SuperScheduledException;
import com.gyx.superscheduled.model.ScheduledSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.gyx.superscheduled.common.utils.AnnotationUtils.changeAnnotationValue;

@DependsOn({"superScheduledConfig"})
@Component
@Order
public class SuperScheduledPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    protected final Log logger = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;

    /**
     * 实例化bean之前的操作
     * @param bean bean实例
     * @param beanName bean的Name
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 实例化bean之后的操作
     * @param bean bean实例
     * @param beanName bean的Name
     */
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
                ScheduledSource scheduledSource = new ScheduledSource(annotation, method, bean);
                if (!scheduledSource.check()) {
                    throw new SuperScheduledException("在" + beanName + "Bean中" + method.getName() + "方法的注解参数错误");
                }
                String name = beanName + "." + method.getName();
                superScheduledConfig.addScheduledSource(name, scheduledSource);
                try {
                    clearOriginalScheduled(annotation);
                } catch (Exception e) {
                    throw new SuperScheduledException("在关闭原始方法" + beanName + method.getName() + "时出现错误");
                }
            }
        }
        return bean;
    }

    /**
     * 修改注解原先的属性
     * @param annotation 注解实例对象
     * @throws Exception
     */
    private void clearOriginalScheduled(Scheduled annotation) throws Exception {
        changeAnnotationValue(annotation, "cron", Scheduled.CRON_DISABLED);
        changeAnnotationValue(annotation, "fixedDelay", -1L);
        changeAnnotationValue(annotation, "fixedDelayString", "");
        changeAnnotationValue(annotation, "fixedRate", -1L);
        changeAnnotationValue(annotation, "fixedRateString", "");
        changeAnnotationValue(annotation, "initialDelay", -1L);
        changeAnnotationValue(annotation, "initialDelayString", "");
    }


    /**
     * 获取SpringBoot的上下文
     * @param applicationContext SpringBoot的上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}