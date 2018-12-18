package org.parc.restes.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by xusiao on 2018/6/23.
 */
public class DateUtil {

    public static Date dayStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date monthStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.MONTH, 0);
        return c.getTime();
    }

    public static Date minInc(Date start, int offset) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.MINUTE, offset);
        return c.getTime();
    }

    public static Date nextDay(Date start) {
        Calendar c = Calendar.getInstance();
        c.setTime(start);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    public static String dateFormat(long timeStamp, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(timeStamp);
    }

    public static Date toDate(String dateStr, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Date utc2local(String utcTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return df.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间戳转换成标准时间
     *
     * @param timestamp
     * @return
     */
    private static String timestamp2localStr(Long timestamp) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date(timestamp));
    }

    public static String utc2localStr(String utcTime) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if (pattern.matcher(utcTime).matches()) {
            return timestamp2localStr(Long.parseLong(utcTime));
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate = utc2local(utcTime);
        return df.format(localDate);
    }

    public static String utc2Str(String utcTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate = utc2cst(utcTime);
        return df.format(localDate);
    }

    private static Date utc2cst(String utcTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return df.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long str2Long(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date localDate = null;
        try {
            localDate = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localDate.getTime();
    }

    public static String getDateTime(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }
}

