package org.parc.sqlrestes.entity;

import java.lang.annotation.*;

/**
 * Created by xusiao on 2018/5/4.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
public @interface Nullable {
}

