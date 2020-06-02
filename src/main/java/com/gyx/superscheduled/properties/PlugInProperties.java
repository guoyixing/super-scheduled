package com.gyx.superscheduled.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.super.scheduled.plug-in")
public class PlugInProperties {
    private Boolean executionFlag = false;

    public Boolean getExecutionFlag() {
        return executionFlag;
    }

    public void setExecutionFlag(Boolean executionFlag) {
        this.executionFlag = executionFlag;
    }
}