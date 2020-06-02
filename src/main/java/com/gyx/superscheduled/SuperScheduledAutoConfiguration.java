package com.gyx.superscheduled;

import com.gyx.superscheduled.core.RunnableInterceptor.strengthen.ExecutionFlagStrengthen;
import com.gyx.superscheduled.properties.ThreadPoolTaskSchedulerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ComponentScan("com.gyx.superscheduled")
@EnableConfigurationProperties(value = ThreadPoolTaskSchedulerProperties.class)
public class SuperScheduledAutoConfiguration {
    @Autowired
    private ThreadPoolTaskSchedulerProperties threadPoolTaskSchedulerProperties;

    @Bean("threadPoolTaskScheduler")
    @ConditionalOnMissingBean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(threadPoolTaskSchedulerProperties.getPoolSize());
        taskScheduler.setThreadNamePrefix(threadPoolTaskSchedulerProperties.getThreadNamePrefix());
        taskScheduler.setWaitForTasksToCompleteOnShutdown(threadPoolTaskSchedulerProperties.getWaitForTasksToCompleteOnShutdown());
        taskScheduler.setAwaitTerminationSeconds(threadPoolTaskSchedulerProperties.getAwaitTerminationSeconds());
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.super.scheduled.plug-in", name = "executionFlag", havingValue = "true")
    public ExecutionFlagStrengthen executionFlagStrengthen() {
        return new ExecutionFlagStrengthen();
    }
}
