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