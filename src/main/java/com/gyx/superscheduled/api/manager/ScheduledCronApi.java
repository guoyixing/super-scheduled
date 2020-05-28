package com.gyx.superscheduled.api.manager;


import com.gyx.superscheduled.core.SuperScheduledManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduled/cron")
public class ScheduledCronApi {

    @Autowired
    private SuperScheduledManager superScheduledManager;

    @PostMapping("/{name}/add")
    public void addCronScheduled(@PathVariable("name") String name,@RequestBody String cron) {
        superScheduledManager.addCronScheduled(name, cron);
    }

    @PostMapping("/{name}/set")
    public void setScheduledCron(@PathVariable("name") String name,@RequestBody String cron) {
        superScheduledManager.setScheduledCron(name, cron);
    }
}