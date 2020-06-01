package com.gyx.superscheduled.core;

import com.gyx.superscheduled.common.utils.ProxyUtils;
import com.gyx.superscheduled.core.RunnableInterceptor.RunnableBaseInterceptor;
import com.gyx.superscheduled.core.RunnableInterceptor.SuperScheduledRunnable;
import com.gyx.superscheduled.core.RunnableInterceptor.strengthen.BaseStrengthen;
import com.gyx.superscheduled.exception.SuperScheduledException;
import com.gyx.superscheduled.model.ScheduledSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@DependsOn("threadPoolTaskScheduler")
@Component
public class SuperScheduledApplicationRunner implements ApplicationRunner, ApplicationContextAware {
    protected final Log logger = LogFactory.getLog(getClass());
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ApplicationContext applicationContext;

    @Autowired
    private SuperScheduledConfig superScheduledConfig;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void run(ApplicationArguments args) {
        superScheduledConfig.setTaskScheduler(threadPoolTaskScheduler);
        Map<String, ScheduledSource> nameToScheduledSource = superScheduledConfig.getNameToScheduledSource();
        for (String name : nameToScheduledSource.keySet()) {
            //获取定时任务源数据
            ScheduledSource scheduledSource = nameToScheduledSource.get(name);
            //实现对象代理
            String[] baseStrengthenBeanNames = applicationContext.getBeanNamesForType(BaseStrengthen.class);
            //实际执行方法的对象，如果存在增强类，会创建代理替换这个对象
            SuperScheduledRunnable proxy = new SuperScheduledRunnable();
            //实际执行对象的类型，如果存在增强类，会创建代理替换这个类型
            Class<? extends SuperScheduledRunnable> proxyClass = SuperScheduledRunnable.class;
            for (String baseStrengthenBeanName : baseStrengthenBeanNames) {
                Object baseStrengthenBean = applicationContext.getBean(baseStrengthenBeanName);
                //创建代理
                proxy = ProxyUtils.getInstance(proxyClass, new RunnableBaseInterceptor(baseStrengthenBean));
                proxyClass = proxy.getClass();
            }
            proxy.setMethod(scheduledSource.getMethod());
            proxy.setBean(scheduledSource.getBean());
            proxy.setSuperScheduledName(name);
            //添加缓存中
            superScheduledConfig.addRunnable(name, proxy::invoke);
            //执行方法
            try {
                ScheduledFuture<?> schedule = ScheduledFutureFactory.create(threadPoolTaskScheduler
                        , scheduledSource, proxy::invoke);
                superScheduledConfig.addScheduledFuture(name, schedule);
                logger.info(df.format(LocalDateTime.now()) + "任务" + name + "已经启动...");

            } catch (Exception e) {
                throw new SuperScheduledException("任务" + name + "启动失败，错误信息：" + e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
