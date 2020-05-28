package com.gyx.superscheduled.api.manager;


import com.gyx.superscheduled.core.SuperScheduledManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduled/fixedDelay")
public class ScheduledFixedDelayApi {

    @Autowired
    private SuperScheduledManager superScheduledManager;

    /**
     * 以FixedDelay模式启动定时任务
     * @param name 定时任务名称
     * @param fixedDelay 运行间隔时间
     * @param initialDelay 首次运行延迟时间
     */
    @PostMapping("/{name}/add/{fixedDelay}/{initialDelay}")
    public void addFixedRateScheduled(@PathVariable("name") String name,
                                      @PathVariable("fixedDelay") Long fixedDelay,
                                      @PathVariable("initialDelay") Long initialDelay) {
        superScheduledManager.addFixedDelayScheduled(name, fixedDelay, initialDelay);
    }

    /**
     * 以FixedDelay模式启动定时任务
     * @param name 定时任务名称
     * @param fixedDelay 运行间隔时间
     */
    @PostMapping("/{name}/add/{fixedDelay}")
    public void addFixedDelayScheduled(@PathVariable("name") String name,
                                      @PathVariable("fixedDelay") Long fixedDelay) {
        superScheduledManager.addFixedDelayScheduled(name, fixedDelay);
    }

    /**
     * 将定时任务转为FixedDelay模式运行，并修改执行间隔的参数值
     * @param name 定时任务名称
     * @param fixedDelay 运行间隔时间
     */
    @PostMapping("/{name}/set/{fixedDelay}")
    public void setScheduledFixedRate(@PathVariable("name") String name,
                                      @PathVariable("fixedDelay") Long fixedDelay) {
        superScheduledManager.setScheduledFixedDelay(name, fixedDelay);
    }
}