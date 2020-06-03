package com.gyx.superscheduled.model;

import com.gyx.superscheduled.enums.ScheduledType;
import com.gyx.superscheduled.exception.SuperScheduledException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ScheduledLog implements Serializable {
    private static final long serialVersionUID = 2525367910036678105L;

    private String superScheduledName;

    private Date statrDate;

    private Date endDate;

    private Exception exception;

    private Long executionTime;

    private Boolean isSuccess;

    private ScheduledSource scheduledSource;

    private String fileName;

    public String getSuperScheduledName() {
        return superScheduledName;
    }

    public void setSuperScheduledName(String superScheduledName) {
        this.superScheduledName = superScheduledName;
    }

    public Date getStatrDate() {
        return statrDate;
    }

    public void setStatrDate(Date statrDate) {
        this.statrDate = statrDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public ScheduledSource getScheduledSource() {
        return scheduledSource;
    }

    public void setScheduledSource(ScheduledSource scheduledSource) {
        this.scheduledSource = scheduledSource;
    }

    public String getFileName() {
        return fileName;
    }

    public void generateFileName() {
        this.fileName = superScheduledName+ DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
    }

    public void computingTime(){
        this.executionTime = this.getEndDate().getTime() - this.statrDate.getTime();
    }
}
