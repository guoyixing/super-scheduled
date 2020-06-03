package com.gyx.superscheduled.model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ScheduledLogFile implements Serializable {
    private String path;

    private String fileName;

    private String size;

    public ScheduledLogFile(File file) {
        this.path = file.getPath();
        this.fileName = file.getName();
        this.size = String.valueOf(file.length()/1024);
    }

    public ScheduledLogFile() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
