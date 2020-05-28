package com.gyx.superscheduled.api.manager;


import com.gyx.superscheduled.core.SuperScheduledManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduled/fixedRate")
public class ScheduledFixedRateApi {

    @Autowired
    private SuperScheduledManager superScheduledManager;

    /**
     * 以FixedRate模式启动定时任务
     * @param name 定时任务名称
     * @param fixedRate 运行间隔时间
     * @param initialDelay 首次运行延迟时间
     */
    @PostMapping("/{name}/add/{fixedRate}/{initialDelay}")
    public void addFixedRateScheduled(@PathVariable("name") String name,
                                      @PathVariable("fixedRate") Long fixedRate,
                                      @PathVariable("initialDelay") Long initialDelay) {
        superScheduledManager.addFixedRateScheduled(name, fixedRate, initialDelay);
    }

    /**
     * 以FixedRate模式启动定时任务
     * @param name 定时任务名称
     * @param fixedRate 运行间隔时间
     */
    @PostMapping("/{name}/add/{fixedRate}")
    public void addFixedRateScheduled(@PathVariable("name") String name,
                                      @PathVariable("fixedRate") Long fixedRate) {
        superScheduledManager.addFixedRateScheduled(name, fixedRate);
    }

    /**
     * 将定时任务转为FixedRate模式运行，并修改执行间隔的参数值
     * @param name 定时任务名称
     * @param fixedRate 运行间隔时间
     */
    @PostMapping("/{name}/set/{fixedRate}")
    public void setScheduledFixedRate(@PathVariable("name") String name,
                                      @PathVariable("fixedRate") Long fixedRate) {
        superScheduledManager.setScheduledFixedRate(name, fixedRate);
    }
}