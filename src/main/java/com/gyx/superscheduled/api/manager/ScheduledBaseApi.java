package com.gyx.superscheduled.api.manager;


import com.gyx.superscheduled.core.SuperScheduledManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduled")
public class ScheduledBaseApi {

    @Autowired
    private SuperScheduledManager superScheduledManager;

    /**
     * 手动执行一次任务
     * @param name 定时任务名称
     */
    @PostMapping("/{name}/run")
    public void addFixedRateScheduled(@PathVariable("name") String name) {
        superScheduledManager.runScheduled(name);
    }

    /**
     * 终止定时任务
     * @param name 定时任务名称
     */
    @DeleteMapping("/{name}")
    public void cancelScheduled(@PathVariable("name") String name) {
        superScheduledManager.cancelScheduled(name);
    }

}