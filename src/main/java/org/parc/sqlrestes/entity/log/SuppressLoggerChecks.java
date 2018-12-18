package org.parc.sqlrestes.entity.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xusiao on 2018/5/4.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@interface SuppressLoggerChecks {
    String reason();
}

