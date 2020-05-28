package com.gyx.superscheduled.api.manager;


import com.gyx.superscheduled.core.SuperScheduledManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/scheduled/name")
public class ScheduledNameApi {

    @Autowired
    private SuperScheduledManager superScheduledManager;

    @GetMapping("/all")
    public List<String> getAllSuperScheduledName() {
        return superScheduledManager.getAllSuperScheduledName();
    }

    @GetMapping("/run")
    public List<String> getRunScheduledName() {
        return superScheduledManager.getRunScheduledName();
    }
}