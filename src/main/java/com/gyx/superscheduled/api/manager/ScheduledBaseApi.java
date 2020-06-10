package com.gyx.superscheduled.api.manager;


import com.gyx.superscheduled.core.SuperScheduledManager;
import com.gyx.superscheduled.model.ScheduledLog;
import com.gyx.superscheduled.model.ScheduledLogFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scheduled")
public class ScheduledBaseApi {

    @Autowired
    private SuperScheduledManager superScheduledManager;

    /**
     * 手动执行一次任务
     *
     * @param name 定时任务名称
     */
    @PostMapping("/{name}/run")
    public void runScheduled(@PathVariable("name") String name) {
        superScheduledManager.runScheduled(name);
    }

    /**
     * 结束正在执行中的任务，跳过这次运行
     * 只有在每个前置增强器结束之后才会判断是否需要跳过此次运行
     *
     * @param name 定时任务名称
     */
    @PostMapping("/{name}/callOff")
    public void callOffScheduled(@PathVariable("name") String name) {
        superScheduledManager.callOffScheduled(name);
    }

    /**
     * 终止定时任务
     *
     * @param name 定时任务名称
     */
    @DeleteMapping("/{name}")
    public void cancelScheduled(@PathVariable("name") String name) {
        superScheduledManager.cancelScheduled(name);
    }

    /**
     * 获取日志文件信息
     */
    @GetMapping("/log/files")
    public List<ScheduledLogFile> logFiles() {
        return superScheduledManager.getScheduledLogFiles();
    }

    /**
     * 获取日志信息
     */
    @GetMapping("/log/{fileName}")
    public List<ScheduledLog> getLogs(@PathVariable("fileName") String fileName) {
        return superScheduledManager.getScheduledLogs(fileName);
    }
}