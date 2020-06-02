package com.gyx.superscheduled.common.utils.proxy;

import com.gyx.superscheduled.core.RunnableInterceptor.SuperScheduledRunnable;

public abstract class Point {
    private String superScheduledName;

    public abstract Object invoke(SuperScheduledRunnable runnable);

    public String getSuperScheduledName() {
        return superScheduledName;
    }

    public void setSuperScheduledName(String superScheduledName) {
        this.superScheduledName = superScheduledName;
    }
}