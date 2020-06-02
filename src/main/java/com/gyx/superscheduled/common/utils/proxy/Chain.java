package com.gyx.superscheduled.common.utils.proxy;

import java.util.List;

public class Chain {
    private List<Point> list;
    private int index = -1;

    public List<Point> getList() {
        return list;
    }

    public void setList(List<Point> list) {
        this.list = list;
    }

    public int getIndex() {
        return index;
    }

    public int incIndex() {
        return ++index;
    }

    public void resetIndex() {
        this.index = -1;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Chain() {
    }

    public Chain(List<Point> list) {
        this.list = list;
    }
}
