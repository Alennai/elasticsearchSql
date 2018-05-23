package org.parc.restes.entity;

import com.dbapp.cpsysportal.entity.ElasticDateR;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElasticDate {
	private static final Logger logger = LoggerFactory.getLogger(ElasticDate.class);
	private static Pattern ldPattern = Pattern.compile("(\\d+)(\\w)");
	private static ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private static ThreadLocal<DateFormat> df2 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		}
	};

	private static ThreadLocal<DateFormat> df3 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	private static ThreadLocal<DateFormat> df4 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
	};

	private static ThreadLocal<DateFormat> df5 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		}
	};

	private static ThreadLocal<DateFormat> df6 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmmss");
		}
	};

	private static ThreadLocal<DateFormat> df8 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH");
		}
	};

	private static ThreadLocal<DateFormat> df9 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM");
		}
	};

	private static ThreadLocal<DateFormat> df7 = new ThreadLocal<DateFormat>() {
		@Override
		protected DateFormat initialValue() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			sdf.setTimeZone(TimeZone.getDefault());
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		}
	};

	public static String _7_2_1(String date) {
		try {
			long m2;
			if (StringUtils.isNumeric(date)){
				m2 = Long.valueOf(date);
			}else {
				m2 =  Instant.parse(date).getMillis();
			}
			return df.get().format(new Date(m2));
		} catch (Exception e) {
			return date;
		}
	}

	public static Date parse(String dateStr) {
		return parse(dateStr, df.get());
	}

	public static Date parse(String dateStr, boolean isDay) {
		return parse(dateStr, isDay ? df3.get() : df4.get());
	}

	public static Date parse2(String dateStr) {
		return parse(dateStr, df2.get());
	}

	public static Date parse(String dateStr, DateFormat _df) {
		try {
			return _df.parse(dateStr);
		} catch (ParseException e) {
			long mm = NumberUtils.toLong(dateStr, -1);
			if (mm > 0)
				return new Date(mm);
			logger.error("{} parse faild by  {}", dateStr, e.getMessage());

		}
		return null;
	}

	public static String format(Date date, DateFormat _df) {
		return _df.format(date);
	}

	public static String format(Date date) {
		return format(date, df.get());
	}

	public static String format4(Date date) {return format(date, df4.get()); }

	public static String format6(Date date) {
		return format(date, df6.get());
	}

	public static long N_Hour_ADD_epoch(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, n);
		return cal.getTimeInMillis();
	}

	public static String format(Date date, String timeStr) {
		DateFormat _df = df3.get();
		if ("0d".equals(timeStr)) {
			_df = df5.get();
		} else if ("1d".equals(timeStr)) {
			_df = df4.get();
		} else if("yyyy-MM-dd HH:mm:ss".equals(timeStr)){
			_df = df.get();
		} else if("yyyyMMdd".equals(timeStr)){
			_df = df2.get();
		} else if ("yyyy-MM-dd".equals(timeStr)){
			_df = df3.get();
		} else if("yyyy-MM-dd HH:mm".equals(timeStr)){
			_df = df4.get();
		} else if("HH:mm".equals(timeStr.trim())){
			_df = df5.get();
		} else if("yyyyMMddHHmmss".equals(timeStr)){
			_df = df6.get();
		} else if("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'".equals(timeStr)){
			_df = df7.get();
		} else if("yyyy-MM-dd HH".equals(timeStr)){
			_df = df8.get();
		} else if("yyyy-MM".equals(timeStr)){
			_df = df9.get();
		}
		return format(date, _df);
	}

	public static String format2(Date date) {
		return format(date, df2.get());
	}

	public static Date tenMinutes(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, 10);
		return cal.getTime();
	}

	public static Date tenMinutesBefore(Date date) {
		return N_MinutesBefore(date, 10);
	}

	public static Date N_MinutesBefore(Date date, int n) {
		if (n <= 0) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, -1 * n);
		return cal.getTime();
	}

	public static int getIntervalDays(Date sDate, Date eDate) {
		long intervalMilli = eDate.getTime() - sDate.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}

	public static ElasticDateR intervalByLd(String lastedays) {
		Matcher matcher = ldPattern.matcher(lastedays);
		int d = -1;
		if (matcher.find()) {
			int count = matcher.groupCount();
			if (count == 2) {
				d = NumberUtils.toInt(matcher.group(1));
				String symbol = matcher.group(2);
				String interval = "1h";
				Calendar startCal = Calendar.getInstance();
				Date startTime = startCal.getTime();
				Date endTime = startCal.getTime();
				if ("d".equals(symbol)) {
					if (d == 0) {
						interval = "1h";
						startCal.set(Calendar.HOUR_OF_DAY, 0);
						startCal.set(Calendar.MINUTE, 0);
						startCal.set(Calendar.SECOND, 0);
					} else if (d == 1) {
						interval = "1h";
						startCal.add(Calendar.HOUR_OF_DAY, 23 * -1);
						startCal.set(Calendar.MINUTE, 0);
						startCal.set(Calendar.SECOND, 0);
					} else {
						startCal.add(Calendar.DAY_OF_MONTH, (d-1) * -1);
						startCal.set(Calendar.HOUR_OF_DAY, 0);
						startCal.set(Calendar.MINUTE, 0);
						startCal.set(Calendar.SECOND, 0);
						interval = "1d";
					}
					// interval = caseDinterval(d);
					startTime = startCal.getTime();
				} else if ("w".equals(symbol)) {
					interval = "1d";
					int dayofweek = startCal.get(Calendar.DAY_OF_WEEK) - 1;
					startCal.set(Calendar.HOUR_OF_DAY, 0);
					startCal.set(Calendar.MINUTE, 0);
					startCal.set(Calendar.SECOND, 0);
					if (dayofweek > 0) {
						startCal.add(Calendar.DATE, -1 * (dayofweek - 1));
					}else{
						startCal.add(Calendar.DATE, -6);
					}
					startTime = startCal.getTime();
				} else if ("m".equals(symbol)) {
					// int tmpD = startCal.get(Calendar.DAY_OF_MONTH);
					// interval = caseDinterval(tmpD);
					interval = "1d";
					startCal.set(Calendar.DAY_OF_MONTH, 1);
					startCal.set(Calendar.HOUR_OF_DAY, 0);
					startCal.set(Calendar.MINUTE, 0);
					startCal.set(Calendar.SECOND, 0);
					startTime = startCal.getTime();
				}

				return new ElasticDateR(startTime, endTime, interval, d);
			}
		}
		return null;
	}

	public static String caseDinterval(int sub) {
		if (sub == 0) {
			int hour = Calendar.getInstance().get(Calendar.HOUR);
			if (hour < 12) {
				return "1h";
			} else {
				return "2h";
			}
		} else if (sub < 7) {
			return "14h";
		} else if (sub < 12) {
			return "1d";
		} else if (sub < 20) {
			return "2d";
		} else {
			return "3d";
		}
	}

}
