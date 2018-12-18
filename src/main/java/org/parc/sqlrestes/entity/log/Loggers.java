package org.parc.sqlrestes.entity.log;

import org.apache.logging.log4j.Logger;

/**
 * Created by xusiao on 2018/5/4.
 */
public class Loggers{
    public static final String SPACE = " ";

    public Loggers() {
    }

    public static Logger getLogger(Logger parentLogger, String s) {
        assert parentLogger instanceof PrefixLogger;

        return ESLoggerFactory.getLogger(((PrefixLogger)parentLogger).prefix(), parentLogger.getName() + s);
    }

    public static Logger getLogger(String s) {
        return ESLoggerFactory.getLogger(s);
    }

    public static Logger getLogger(Class<?> clazz) {
        return ESLoggerFactory.getLogger(clazz);
    }

    public static Logger getLogger(Class<?> clazz, String... prefixes) {
        return ESLoggerFactory.getLogger(formatPrefix(prefixes), clazz);
    }

    public static Logger getLogger(String name, String... prefixes) {
        return ESLoggerFactory.getLogger(formatPrefix(prefixes), name);
    }

    private static String formatPrefix(String... prefixes) {
        String prefix = null;
        if(prefixes != null && prefixes.length > 0) {
            StringBuilder sb = new StringBuilder();
            int var4 = prefixes.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String prefixX = prefixes[var5];
                if(prefixX != null) {
                    if(prefixX.equals(" ")) {
                        sb.append(" ");
                    } else {
                        sb.append("[").append(prefixX).append("]");
                    }
                }
            }

            if(sb.length() > 0) {
                sb.append(" ");
                prefix = sb.toString();
            }
        }

        return prefix;
    }
}
