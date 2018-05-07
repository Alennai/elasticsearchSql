package org.elasticsearch.common.unit;

import java.time.Period;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by xusiao on 2018/5/3.
 */
public class TimeValue {
    private static Map<TimeUnit, Byte> TIME_UNIT_BYTE_MAP;
    private static Map<Byte, TimeUnit> BYTE_TIME_UNIT_MAP;
    private TimeUnit timeUnit;
    private static final long C0 = 1L;
    private static final long C1 = 1000L;
    private static final long C2 = 1000000L;
    private static final long C3 = 1000000000L;
    private static final long C4 = 60000000000L;
    private static final long C5 = 3600000000000L;
    private static final long C6 = 86400000000000L;
    TimeUnit timeUnit() {
        return this.timeUnit;
    }

}
