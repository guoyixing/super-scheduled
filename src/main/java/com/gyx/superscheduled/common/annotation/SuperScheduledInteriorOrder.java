package com.gyx.superscheduled.common.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface SuperScheduledInteriorOrder {
    int value() default 0;
}