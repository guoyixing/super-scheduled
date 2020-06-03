package com.gyx.superscheduled.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.super.scheduled.plug-in")
public class PlugInProperties {
    private Boolean executionFlag = false;
    private Boolean executionLog = false;
    private String logPath = "";

    public Boolean getExecutionFlag() {
        return executionFlag;
    }

    public void setExecutionFlag(Boolean executionFlag) {
        this.executionFlag = executionFlag;
    }

    public Boolean getExecutionLog() {
        return executionLog;
    }

    public void setExecutionLog(Boolean executionLog) {
        this.executionLog = executionLog;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }
}