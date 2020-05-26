package com.gyx.superscheduled.exception;

public class SuperScheduledException extends RuntimeException {
    public SuperScheduledException() {
        super("未知错误");
    }

    public SuperScheduledException(String message) {
        super(message);
    }

    public SuperScheduledException(String message, Throwable cause) {
        super(message, cause);
    }
}
