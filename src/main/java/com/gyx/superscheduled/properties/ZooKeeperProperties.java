package com.gyx.superscheduled.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.super.scheduled.zookeeper")
public class ZooKeeperProperties {
    private Integer sessionTimeout = 60000;

    private Integer connectionTimeout = 60000;

    private String url;

    private String zkPath;

    private String zkParentNodePath;

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath;
    }

    public String getZkParentNodePath() {
        return zkParentNodePath;
    }

    public void setZkParentNodePath(String zkParentNodePath) {
        this.zkParentNodePath = zkParentNodePath;
    }
}