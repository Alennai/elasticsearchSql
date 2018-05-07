package org.parc.sqlrestes.entity.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.ExtendedLogger;

/**
 * Created by xusiao on 2018/5/4.
 */
public class ESLoggerFactory  {
    private ESLoggerFactory() {
    }

    public static Logger getLogger(String prefix, String name) {
        return getLogger(prefix, LogManager.getLogger(name));
    }

    public static Logger getLogger(String prefix, Class<?> clazz) {
        return getLogger(prefix, LogManager.getLogger(clazz.getName()));
    }

    public static Logger getLogger(String prefix, Logger logger) {
        return new PrefixLogger((ExtendedLogger)logger, logger.getName(), prefix);
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger((String)null, (Class)clazz);
    }

    public static Logger getLogger(String name) {
        return getLogger((String)null, (String)name);
    }

    public static Logger getRootLogger() {
        return LogManager.getRootLogger();
    }
}
