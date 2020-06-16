package com.gyx.superscheduled.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.super.scheduled.plug-interfaces")
public class PlugInProperties {
    private Boolean executionFlag = false;
    private Boolean executionLog = false;
    private Boolean colony = false;
    private String logPath = "";
    private String colonyName = "colony";

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

    public Boolean getColony() {
        return colony;
    }

    public void setColony(Boolean colony) {
        this.colony = colony;
    }

    public String getColonyName() {
        return colonyName;
    }

    public void setColonyName(String colonyName) {
        this.colonyName = colonyName;
    }
}