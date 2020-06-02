package com.gyx.superscheduled.core;

import com.gyx.superscheduled.common.utils.proxy.Chain;
import com.gyx.superscheduled.common.utils.proxy.Point;
import com.gyx.superscheduled.common.utils.proxy.ProxyUtils;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            //获取所有增强类
            String[] baseStrengthenBeanNames = applicationContext.getBeanNamesForType(BaseStrengthen.class);
            //创建执行控制器
            SuperScheduledRunnable runnable = new SuperScheduledRunnable();
            runnable.setMethod(scheduledSource.getMethod());
            runnable.setBean(scheduledSource.getBean());
            //将增强器代理成point
            List<Point> points = new ArrayList<>(baseStrengthenBeanNames.length);
            for (String baseStrengthenBeanName : baseStrengthenBeanNames) {
                Object baseStrengthenBean = applicationContext.getBean(baseStrengthenBeanName);
                //创建代理
                Point proxy = ProxyUtils.getInstance(Point.class, new RunnableBaseInterceptor(baseStrengthenBean, runnable));
                proxy.setSuperScheduledName(name);
                //所有的points连接起来
                points.add(proxy);
            }

            runnable.setChain(new Chain(points));
            //添加缓存中
            superScheduledConfig.addRunnable(name, runnable::invoke);
            //执行方法
            try {
                ScheduledFuture<?> schedule = ScheduledFutureFactory.create(threadPoolTaskScheduler
                        , scheduledSource, runnable::invoke);
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
