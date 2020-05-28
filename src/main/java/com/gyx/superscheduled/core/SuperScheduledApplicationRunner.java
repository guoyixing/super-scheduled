package com.gyx.superscheduled.core;

import com.gyx.superscheduled.exception.SuperScheduledException;
import com.gyx.superscheduled.model.ScheduledSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
public class SuperScheduledApplicationRunner implements ApplicationRunner {
    protected final Log logger = LogFactory.getLog(getClass());
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private SuperScheduledConfig superScheduledConfig;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Override
    public void run(ApplicationArguments args){
        superScheduledConfig.setTaskScheduler(threadPoolTaskScheduler);
        Set<String> allSuperScheduledName = superScheduledConfig.getNameToRunnable().keySet();
        Map<String, ScheduledSource> nameToScheduledSource = superScheduledConfig.getNameToScheduledSource();
        Map<String, Runnable> nameToRunnable = superScheduledConfig.getNameToRunnable();
        for (String name : allSuperScheduledName) {
            try {
                ScheduledFuture<?> schedule = ScheduledFutureFactory.create(threadPoolTaskScheduler
                        ,nameToScheduledSource.get(name),nameToRunnable.get(name));
                superScheduledConfig.addScheduledFuture(name, schedule);
                logger.info(df.format(LocalDateTime.now()) + "任务" + name + "已经启动...");

            } catch (Exception e) {
                throw new SuperScheduledException("任务" + name + "启动失败，错误信息：" + e.getLocalizedMessage());
            }
        }
    }

}
