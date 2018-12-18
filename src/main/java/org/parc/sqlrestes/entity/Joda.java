package org.parc.sqlrestes.entity;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.ReadablePartial;
import org.joda.time.field.DividedDateTimeField;
import org.joda.time.field.OffsetDateTimeField;
import org.joda.time.field.ScaledDurationField;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimeParserBucket;
import org.joda.time.format.DateTimePrinter;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Locale;

/**
 * Created by xusiao on 2018/5/4.
 */
class Joda {
    private static final DurationFieldType Quarters = new DurationFieldType("quarters") {
        public DurationField getField(Chronology chronology) {
            return new ScaledDurationField(chronology.months(), Joda.Quarters, 3);
        }
    };
    private static final DateTimeFieldType QuarterOfYear = new DateTimeFieldType("quarterOfYear") {
        public DurationFieldType getDurationType() {
            return Joda.Quarters;
        }

        public DurationFieldType getRangeDurationType() {
            return DurationFieldType.years();
        }

        public DateTimeField getField(Chronology chronology) {
            return new OffsetDateTimeField(new DividedDateTimeField(new OffsetDateTimeField(chronology.monthOfYear(), -1), Joda.QuarterOfYear, 3), 1);
        }
    };

    public Joda() {
    }

    public static FormatDateTimeFormatter forPattern(String input) {
//        return forPattern(input, Locale.ROOT);
        return null;
    }

//    public static FormatDateTimeFormatter forPattern(String input, Locale locale) {
//        if(input!=null||input.equals("")) {
//            input = input.trim();
//        }
//
//        if(input != null && input.length() != 0) {
//            DateTimeFormatter formatter;
//            if(!"basicDate".equals(input) && !"basic_date".equals(input)) {
//                if(!"basicDateTime".equals(input) && !"basic_date_time".equals(input)) {
//                    if(!"basicDateTimeNoMillis".equals(input) && !"basic_date_time_no_millis".equals(input)) {
//                        if(!"basicOrdinalDate".equals(input) && !"basic_ordinal_date".equals(input)) {
//                            if(!"basicOrdinalDateTime".equals(input) && !"basic_ordinal_date_time".equals(input)) {
//                                if(!"basicOrdinalDateTimeNoMillis".equals(input) && !"basic_ordinal_date_time_no_millis".equals(input)) {
//                                    if(!"basicTime".equals(input) && !"basic_time".equals(input)) {
//                                        if(!"basicTimeNoMillis".equals(input) && !"basic_time_no_millis".equals(input)) {
//                                            if(!"basicTTime".equals(input) && !"basic_t_time".equals(input)) {
//                                                if(!"basicTTimeNoMillis".equals(input) && !"basic_t_time_no_millis".equals(input)) {
//                                                    if(!"basicWeekDate".equals(input) && !"basic_week_date".equals(input)) {
//                                                        if(!"basicWeekDateTime".equals(input) && !"basic_week_date_time".equals(input)) {
//                                                            if(!"basicWeekDateTimeNoMillis".equals(input) && !"basic_week_date_time_no_millis".equals(input)) {
//                                                                if("date".equals(input)) {
//                                                                    formatter = ISODateTimeFormat.date();
//                                                                } else if(!"dateHour".equals(input) && !"date_hour".equals(input)) {
//                                                                    if(!"dateHourMinute".equals(input) && !"date_hour_minute".equals(input)) {
//                                                                        if(!"dateHourMinuteSecond".equals(input) && !"date_hour_minute_second".equals(input)) {
//                                                                            if(!"dateHourMinuteSecondFraction".equals(input) && !"date_hour_minute_second_fraction".equals(input)) {
//                                                                                if(!"dateHourMinuteSecondMillis".equals(input) && !"date_hour_minute_second_millis".equals(input)) {
//                                                                                    if("dateOptionalTime".equals(input) || "date_optional_time".equals(input)) {
//                                                                                        return new FormatDateTimeFormatter(input, ISODateTimeFormat.dateOptionalTimeParser().withZone(DateTimeZone.UTC), ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC), locale);
//                                                                                    }
//
//                                                                                    if(!"dateTime".equals(input) && !"date_time".equals(input)) {
//                                                                                        if(!"dateTimeNoMillis".equals(input) && !"date_time_no_millis".equals(input)) {
//                                                                                            if("hour".equals(input)) {
//                                                                                                formatter = ISODateTimeFormat.hour();
//                                                                                            } else if(!"hourMinute".equals(input) && !"hour_minute".equals(input)) {
//                                                                                                if(!"hourMinuteSecond".equals(input) && !"hour_minute_second".equals(input)) {
//                                                                                                    if(!"hourMinuteSecondFraction".equals(input) && !"hour_minute_second_fraction".equals(input)) {
//                                                                                                        if(!"hourMinuteSecondMillis".equals(input) && !"hour_minute_second_millis".equals(input)) {
//                                                                                                            if(!"ordinalDate".equals(input) && !"ordinal_date".equals(input)) {
//                                                                                                                if(!"ordinalDateTime".equals(input) && !"ordinal_date_time".equals(input)) {
//                                                                                                                    if(!"ordinalDateTimeNoMillis".equals(input) && !"ordinal_date_time_no_millis".equals(input)) {
//                                                                                                                        if("time".equals(input)) {
//                                                                                                                            formatter = ISODateTimeFormat.time();
//                                                                                                                        } else if(!"timeNoMillis".equals(input) && !"time_no_millis".equals(input)) {
//                                                                                                                            if(!"tTime".equals(input) && !"t_time".equals(input)) {
//                                                                                                                                if(!"tTimeNoMillis".equals(input) && !"t_time_no_millis".equals(input)) {
//                                                                                                                                    if(!"weekDate".equals(input) && !"week_date".equals(input)) {
//                                                                                                                                        if(!"weekDateTime".equals(input) && !"week_date_time".equals(input)) {
//                                                                                                                                            if(!"weekDateTimeNoMillis".equals(input) && !"week_date_time_no_millis".equals(input)) {
//                                                                                                                                                if(!"weekyear".equals(input) && !"week_year".equals(input)) {
//                                                                                                                                                    if(!"weekyearWeek".equals(input) && !"weekyear_week".equals(input)) {
//                                                                                                                                                        if(!"weekyearWeekDay".equals(input) && !"weekyear_week_day".equals(input)) {
//                                                                                                                                                            if("year".equals(input)) {
//                                                                                                                                                                formatter = ISODateTimeFormat.year();
//                                                                                                                                                            } else if(!"yearMonth".equals(input) && !"year_month".equals(input)) {
//                                                                                                                                                                if(!"yearMonthDay".equals(input) && !"year_month_day".equals(input)) {
//                                                                                                                                                                    if("epoch_second".equals(input)) {
//                                                                                                                                                                        formatter = (new DateTimeFormatterBuilder()).append(new Joda.EpochTimePrinter(false), new Joda.EpochTimeParser(false)).toFormatter();
//                                                                                                                                                                    } else if("epoch_millis".equals(input)) {
//                                                                                                                                                                        formatter = (new DateTimeFormatterBuilder()).append(new Joda.EpochTimePrinter(true), new Joda.EpochTimeParser(true)).toFormatter();
//                                                                                                                                                                    } else if(!"strictBasicWeekDate".equals(input) && !"strict_basic_week_date".equals(input)) {
//                                                                                                                                                                        if(!"strictBasicWeekDateTime".equals(input) && !"strict_basic_week_date_time".equals(input)) {
//                                                                                                                                                                            if(!"strictBasicWeekDateTimeNoMillis".equals(input) && !"strict_basic_week_date_time_no_millis".equals(input)) {
//                                                                                                                                                                                if(!"strictDate".equals(input) && !"strict_date".equals(input)) {
//                                                                                                                                                                                    if(!"strictDateHour".equals(input) && !"strict_date_hour".equals(input)) {
//                                                                                                                                                                                        if(!"strictDateHourMinute".equals(input) && !"strict_date_hour_minute".equals(input)) {
//                                                                                                                                                                                            if(!"strictDateHourMinuteSecond".equals(input) && !"strict_date_hour_minute_second".equals(input)) {
//                                                                                                                                                                                                if(!"strictDateHourMinuteSecondFraction".equals(input) && !"strict_date_hour_minute_second_fraction".equals(input)) {
//                                                                                                                                                                                                    if(!"strictDateHourMinuteSecondMillis".equals(input) && !"strict_date_hour_minute_second_millis".equals(input)) {
//                                                                                                                                                                                                        if("strictDateOptionalTime".equals(input) || "strict_date_optional_time".equals(input)) {
//                                                                                                                                                                                                            return new FormatDateTimeFormatter(input, StrictISODateTimeFormat.dateOptionalTimeParser().withZone(DateTimeZone.UTC), StrictISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC), locale);
//                                                                                                                                                                                                        }
//
//                                                                                                                                                                                                        if(!"strictDateTime".equals(input) && !"strict_date_time".equals(input)) {
//                                                                                                                                                                                                            if(!"strictDateTimeNoMillis".equals(input) && !"strict_date_time_no_millis".equals(input)) {
//                                                                                                                                                                                                                if(!"strictHour".equals(input) && !"strict_hour".equals(input)) {
//                                                                                                                                                                                                                    if(!"strictHourMinute".equals(input) && !"strict_hour_minute".equals(input)) {
//                                                                                                                                                                                                                        if(!"strictHourMinuteSecond".equals(input) && !"strict_hour_minute_second".equals(input)) {
//                                                                                                                                                                                                                            if(!"strictHourMinuteSecondFraction".equals(input) && !"strict_hour_minute_second_fraction".equals(input)) {
//                                                                                                                                                                                                                                if(!"strictHourMinuteSecondMillis".equals(input) && !"strict_hour_minute_second_millis".equals(input)) {
//                                                                                                                                                                                                                                    if(!"strictOrdinalDate".equals(input) && !"strict_ordinal_date".equals(input)) {
//                                                                                                                                                                                                                                        if(!"strictOrdinalDateTime".equals(input) && !"strict_ordinal_date_time".equals(input)) {
//                                                                                                                                                                                                                                            if(!"strictOrdinalDateTimeNoMillis".equals(input) && !"strict_ordinal_date_time_no_millis".equals(input)) {
//                                                                                                                                                                                                                                                if(!"strictTime".equals(input) && !"strict_time".equals(input)) {
//                                                                                                                                                                                                                                                    if(!"strictTimeNoMillis".equals(input) && !"strict_time_no_millis".equals(input)) {
//                                                                                                                                                                                                                                                        if(!"strictTTime".equals(input) && !"strict_t_time".equals(input)) {
//                                                                                                                                                                                                                                                            if(!"strictTTimeNoMillis".equals(input) && !"strict_t_time_no_millis".equals(input)) {
//                                                                                                                                                                                                                                                                if(!"strictWeekDate".equals(input) && !"strict_week_date".equals(input)) {
//                                                                                                                                                                                                                                                                    if(!"strictWeekDateTime".equals(input) && !"strict_week_date_time".equals(input)) {
//                                                                                                                                                                                                                                                                        if(!"strictWeekDateTimeNoMillis".equals(input) && !"strict_week_date_time_no_millis".equals(input)) {
//                                                                                                                                                                                                                                                                            if(!"strictWeekyear".equals(input) && !"strict_weekyear".equals(input)) {
//                                                                                                                                                                                                                                                                                if(!"strictWeekyearWeek".equals(input) && !"strict_weekyear_week".equals(input)) {
//                                                                                                                                                                                                                                                                                    if(!"strictWeekyearWeekDay".equals(input) && !"strict_weekyear_week_day".equals(input)) {
//                                                                                                                                                                                                                                                                                        if(!"strictYear".equals(input) && !"strict_year".equals(input)) {
//                                                                                                                                                                                                                                                                                            if(!"strictYearMonth".equals(input) && !"strict_year_month".equals(input)) {
//                                                                                                                                                                                                                                                                                                if(!"strictYearMonthDay".equals(input) && !"strict_year_month_day".equals(input)) {
//                                                                                                                                                                                                                                                                                                    if(Strings.hasLength(input) && input.contains("||")) {
//                                                                                                                                                                                                                                                                                                        String[] e = Strings.delimitedListToStringArray(input, "||");
//                                                                                                                                                                                                                                                                                                        DateTimeParser[] parsers = new DateTimeParser[e.length];
//                                                                                                                                                                                                                                                                                                        if(e.length == 1) {
//                                                                                                                                                                                                                                                                                                            formatter = forPattern(input, locale).parser();
//                                                                                                                                                                                                                                                                                                        } else {
//                                                                                                                                                                                                                                                                                                            DateTimeFormatter dateTimeFormatter = null;
//
//                                                                                                                                                                                                                                                                                                            for(int builder = 0; builder < e.length; ++builder) {
//                                                                                                                                                                                                                                                                                                                FormatDateTimeFormatter currentFormatter = forPattern(e[builder], locale);
//                                                                                                                                                                                                                                                                                                                DateTimeFormatter currentParser = currentFormatter.parser();
//                                                                                                                                                                                                                                                                                                                if(dateTimeFormatter == null) {
//                                                                                                                                                                                                                                                                                                                    dateTimeFormatter = currentFormatter.printer();
//                                                                                                                                                                                                                                                                                                                }
//
//                                                                                                                                                                                                                                                                                                                parsers[builder] = currentParser.getParser();
//                                                                                                                                                                                                                                                                                                            }
//
//                                                                                                                                                                                                                                                                                                            DateTimeFormatterBuilder var10 = (new DateTimeFormatterBuilder()).append(dateTimeFormatter.withZone(DateTimeZone.UTC).getPrinter(), parsers);
//                                                                                                                                                                                                                                                                                                            formatter = var10.toFormatter();
//                                                                                                                                                                                                                                                                                                        }
//                                                                                                                                                                                                                                                                                                    } else {
//                                                                                                                                                                                                                                                                                                        try {
//                                                                                                                                                                                                                                                                                                            formatter = DateTimeFormat.forPattern(input);
//                                                                                                                                                                                                                                                                                                        } catch (IllegalArgumentException var9) {
//                                                                                                                                                                                                                                                                                                            throw new IllegalArgumentException("Invalid format: [" + input + "]: " + var9.getMessage(), var9);
//                                                                                                                                                                                                                                                                                                        }
//                                                                                                                                                                                                                                                                                                    }
//                                                                                                                                                                                                                                                                                                } else {
//                                                                                                                                                                                                                                                                                                    formatter = StrictISODateTimeFormat.yearMonthDay();
//                                                                                                                                                                                                                                                                                                }
//                                                                                                                                                                                                                                                                                            } else {
//                                                                                                                                                                                                                                                                                                formatter = StrictISODateTimeFormat.yearMonth();
//                                                                                                                                                                                                                                                                                            }
//                                                                                                                                                                                                                                                                                        } else {
//                                                                                                                                                                                                                                                                                            formatter = StrictISODateTimeFormat.year();
//                                                                                                                                                                                                                                                                                        }
//                                                                                                                                                                                                                                                                                    } else {
//                                                                                                                                                                                                                                                                                        formatter = StrictISODateTimeFormat.weekyearWeekDay();
//                                                                                                                                                                                                                                                                                    }
//                                                                                                                                                                                                                                                                                } else {
//                                                                                                                                                                                                                                                                                    formatter = StrictISODateTimeFormat.weekyearWeek();
//                                                                                                                                                                                                                                                                                }
//                                                                                                                                                                                                                                                                            } else {
//                                                                                                                                                                                                                                                                                formatter = StrictISODateTimeFormat.weekyear();
//                                                                                                                                                                                                                                                                            }
//                                                                                                                                                                                                                                                                        } else {
//                                                                                                                                                                                                                                                                            formatter = StrictISODateTimeFormat.weekDateTimeNoMillis();
//                                                                                                                                                                                                                                                                        }
//                                                                                                                                                                                                                                                                    } else {
//                                                                                                                                                                                                                                                                        formatter = StrictISODateTimeFormat.weekDateTime();
//                                                                                                                                                                                                                                                                    }
//                                                                                                                                                                                                                                                                } else {
//                                                                                                                                                                                                                                                                    formatter = StrictISODateTimeFormat.weekDate();
//                                                                                                                                                                                                                                                                }
//                                                                                                                                                                                                                                                            } else {
//                                                                                                                                                                                                                                                                formatter = StrictISODateTimeFormat.tTimeNoMillis();
//                                                                                                                                                                                                                                                            }
//                                                                                                                                                                                                                                                        } else {
//                                                                                                                                                                                                                                                            formatter = StrictISODateTimeFormat.tTime();
//                                                                                                                                                                                                                                                        }
//                                                                                                                                                                                                                                                    } else {
//                                                                                                                                                                                                                                                        formatter = StrictISODateTimeFormat.timeNoMillis();
//                                                                                                                                                                                                                                                    }
//                                                                                                                                                                                                                                                } else {
//                                                                                                                                                                                                                                                    formatter = StrictISODateTimeFormat.time();
//                                                                                                                                                                                                                                                }
//                                                                                                                                                                                                                                            } else {
//                                                                                                                                                                                                                                                formatter = StrictISODateTimeFormat.ordinalDateTimeNoMillis();
//                                                                                                                                                                                                                                            }
//                                                                                                                                                                                                                                        } else {
//                                                                                                                                                                                                                                            formatter = StrictISODateTimeFormat.ordinalDateTime();
//                                                                                                                                                                                                                                        }
//                                                                                                                                                                                                                                    } else {
//                                                                                                                                                                                                                                        formatter = StrictISODateTimeFormat.ordinalDate();
//                                                                                                                                                                                                                                    }
//                                                                                                                                                                                                                                } else {
//                                                                                                                                                                                                                                    formatter = StrictISODateTimeFormat.hourMinuteSecondMillis();
//                                                                                                                                                                                                                                }
//                                                                                                                                                                                                                            } else {
//                                                                                                                                                                                                                                formatter = StrictISODateTimeFormat.hourMinuteSecondFraction();
//                                                                                                                                                                                                                            }
//                                                                                                                                                                                                                        } else {
//                                                                                                                                                                                                                            formatter = StrictISODateTimeFormat.hourMinuteSecond();
//                                                                                                                                                                                                                        }
//                                                                                                                                                                                                                    } else {
//                                                                                                                                                                                                                        formatter = StrictISODateTimeFormat.hourMinute();
//                                                                                                                                                                                                                    }
//                                                                                                                                                                                                                } else {
//                                                                                                                                                                                                                    formatter = StrictISODateTimeFormat.hour();
//                                                                                                                                                                                                                }
//                                                                                                                                                                                                            } else {
//                                                                                                                                                                                                                formatter = StrictISODateTimeFormat.dateTimeNoMillis();
//                                                                                                                                                                                                            }
//                                                                                                                                                                                                        } else {
//                                                                                                                                                                                                            formatter = StrictISODateTimeFormat.dateTime();
//                                                                                                                                                                                                        }
//                                                                                                                                                                                                    } else {
//                                                                                                                                                                                                        formatter = StrictISODateTimeFormat.dateHourMinuteSecondMillis();
//                                                                                                                                                                                                    }
//                                                                                                                                                                                                } else {
//                                                                                                                                                                                                    formatter = StrictISODateTimeFormat.dateHourMinuteSecondFraction();
//                                                                                                                                                                                                }
//                                                                                                                                                                                            } else {
//                                                                                                                                                                                                formatter = StrictISODateTimeFormat.dateHourMinuteSecond();
//                                                                                                                                                                                            }
//                                                                                                                                                                                        } else {
//                                                                                                                                                                                            formatter = StrictISODateTimeFormat.dateHourMinute();
//                                                                                                                                                                                        }
//                                                                                                                                                                                    } else {
//                                                                                                                                                                                        formatter = StrictISODateTimeFormat.dateHour();
//                                                                                                                                                                                    }
//                                                                                                                                                                                } else {
//                                                                                                                                                                                    formatter = StrictISODateTimeFormat.date();
//                                                                                                                                                                                }
//                                                                                                                                                                            } else {
//                                                                                                                                                                                formatter = StrictISODateTimeFormat.basicWeekDateTimeNoMillis();
//                                                                                                                                                                            }
//                                                                                                                                                                        } else {
//                                                                                                                                                                            formatter = StrictISODateTimeFormat.basicWeekDateTime();
//                                                                                                                                                                        }
//                                                                                                                                                                    } else {
//                                                                                                                                                                        formatter = StrictISODateTimeFormat.basicWeekDate();
//                                                                                                                                                                    }
//                                                                                                                                                                } else {
//                                                                                                                                                                    formatter = ISODateTimeFormat.yearMonthDay();
//                                                                                                                                                                }
//                                                                                                                                                            } else {
//                                                                                                                                                                formatter = ISODateTimeFormat.yearMonth();
//                                                                                                                                                            }
//                                                                                                                                                        } else {
//                                                                                                                                                            formatter = ISODateTimeFormat.weekyearWeekDay();
//                                                                                                                                                        }
//                                                                                                                                                    } else {
//                                                                                                                                                        formatter = ISODateTimeFormat.weekyearWeek();
//                                                                                                                                                    }
//                                                                                                                                                } else {
//                                                                                                                                                    formatter = ISODateTimeFormat.weekyear();
//                                                                                                                                                }
//                                                                                                                                            } else {
//                                                                                                                                                formatter = ISODateTimeFormat.weekDateTimeNoMillis();
//                                                                                                                                            }
//                                                                                                                                        } else {
//                                                                                                                                            formatter = ISODateTimeFormat.weekDateTime();
//                                                                                                                                        }
//                                                                                                                                    } else {
//                                                                                                                                        formatter = ISODateTimeFormat.weekDate();
//                                                                                                                                    }
//                                                                                                                                } else {
//                                                                                                                                    formatter = ISODateTimeFormat.tTimeNoMillis();
//                                                                                                                                }
//                                                                                                                            } else {
//                                                                                                                                formatter = ISODateTimeFormat.tTime();
//                                                                                                                            }
//                                                                                                                        } else {
//                                                                                                                            formatter = ISODateTimeFormat.timeNoMillis();
//                                                                                                                        }
//                                                                                                                    } else {
//                                                                                                                        formatter = ISODateTimeFormat.ordinalDateTimeNoMillis();
//                                                                                                                    }
//                                                                                                                } else {
//                                                                                                                    formatter = ISODateTimeFormat.ordinalDateTime();
//                                                                                                                }
//                                                                                                            } else {
//                                                                                                                formatter = ISODateTimeFormat.ordinalDate();
//                                                                                                            }
//                                                                                                        } else {
//                                                                                                            formatter = ISODateTimeFormat.hourMinuteSecondMillis();
//                                                                                                        }
//                                                                                                    } else {
//                                                                                                        formatter = ISODateTimeFormat.hourMinuteSecondFraction();
//                                                                                                    }
//                                                                                                } else {
//                                                                                                    formatter = ISODateTimeFormat.hourMinuteSecond();
//                                                                                                }
//                                                                                            } else {
//                                                                                                formatter = ISODateTimeFormat.hourMinute();
//                                                                                            }
//                                                                                        } else {
//                                                                                            formatter = ISODateTimeFormat.dateTimeNoMillis();
//                                                                                        }
//                                                                                    } else {
//                                                                                        formatter = ISODateTimeFormat.dateTime();
//                                                                                    }
//                                                                                } else {
//                                                                                    formatter = ISODateTimeFormat.dateHourMinuteSecondMillis();
//                                                                                }
//                                                                            } else {
//                                                                                formatter = ISODateTimeFormat.dateHourMinuteSecondFraction();
//                                                                            }
//                                                                        } else {
//                                                                            formatter = ISODateTimeFormat.dateHourMinuteSecond();
//                                                                        }
//                                                                    } else {
//                                                                        formatter = ISODateTimeFormat.dateHourMinute();
//                                                                    }
//                                                                } else {
//                                                                    formatter = ISODateTimeFormat.dateHour();
//                                                                }
//                                                            } else {
//                                                                formatter = ISODateTimeFormat.basicWeekDateTimeNoMillis();
//                                                            }
//                                                        } else {
//                                                            formatter = ISODateTimeFormat.basicWeekDateTime();
//                                                        }
//                                                    } else {
//                                                        formatter = ISODateTimeFormat.basicWeekDate();
//                                                    }
//                                                } else {
//                                                    formatter = ISODateTimeFormat.basicTTimeNoMillis();
//                                                }
//                                            } else {
//                                                formatter = ISODateTimeFormat.basicTTime();
//                                            }
//                                        } else {
//                                            formatter = ISODateTimeFormat.basicTimeNoMillis();
//                                        }
//                                    } else {
//                                        formatter = ISODateTimeFormat.basicTime();
//                                    }
//                                } else {
//                                    formatter = ISODateTimeFormat.basicOrdinalDateTimeNoMillis();
//                                }
//                            } else {
//                                formatter = ISODateTimeFormat.basicOrdinalDateTime();
//                            }
//                        } else {
//                            formatter = ISODateTimeFormat.basicOrdinalDate();
//                        }
//                    } else {
//                        formatter = ISODateTimeFormat.basicDateTimeNoMillis();
//                    }
//                } else {
//                    formatter = ISODateTimeFormat.basicDateTime();
//                }
//            } else {
//                formatter = ISODateTimeFormat.basicDate();
//            }
//
//            return new FormatDateTimeFormatter(input, formatter.withZone(DateTimeZone.UTC), locale);
//        } else {
//            throw new IllegalArgumentException("No date pattern provided");
//        }
//    }

    public static FormatDateTimeFormatter getStrictStandardDateFormatter() {
        DateTimeFormatter shortFormatter = (new DateTimeFormatterBuilder()).appendFixedDecimal(DateTimeFieldType.year(), 4).appendLiteral('/').appendFixedDecimal(DateTimeFieldType.monthOfYear(), 2).appendLiteral('/').appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 2).toFormatter().withZoneUTC();
        DateTimeFormatter longFormatter = (new DateTimeFormatterBuilder()).appendFixedDecimal(DateTimeFieldType.year(), 4).appendLiteral('/').appendFixedDecimal(DateTimeFieldType.monthOfYear(), 2).appendLiteral('/').appendFixedDecimal(DateTimeFieldType.dayOfMonth(), 2).appendLiteral(' ').appendFixedSignedDecimal(DateTimeFieldType.hourOfDay(), 2).appendLiteral(':').appendFixedSignedDecimal(DateTimeFieldType.minuteOfHour(), 2).appendLiteral(':').appendFixedSignedDecimal(DateTimeFieldType.secondOfMinute(), 2).toFormatter().withZoneUTC();
        DateTimeFormatterBuilder builder = (new DateTimeFormatterBuilder()).append(longFormatter.withZone(DateTimeZone.UTC).getPrinter(), new DateTimeParser[]{longFormatter.getParser(), shortFormatter.getParser(), new Joda.EpochTimeParser(true)});
        return new FormatDateTimeFormatter("yyyy/MM/dd HH:mm:ss||yyyy/MM/dd||epoch_millis", builder.toFormatter().withZone(DateTimeZone.UTC), Locale.ROOT);
    }

    static class EpochTimePrinter implements DateTimePrinter {
        private boolean hasMilliSecondPrecision;

        public EpochTimePrinter(boolean hasMilliSecondPrecision) {
            this.hasMilliSecondPrecision = hasMilliSecondPrecision;
        }

        public int estimatePrintedLength() {
            return this.hasMilliSecondPrecision ? 19 : 16;
        }

        public void printTo(StringBuffer buf, long instant, Chronology chrono, int displayOffset, DateTimeZone displayZone, Locale locale) {
            if (this.hasMilliSecondPrecision) {
                buf.append(instant - (long) displayOffset);
            } else {
                buf.append((instant - (long) displayOffset) / 1000L);
            }

        }

        public void printTo(Writer out, long instant, Chronology chrono, int displayOffset, DateTimeZone displayZone, Locale locale) throws IOException {
            if (this.hasMilliSecondPrecision) {
                out.write(String.valueOf(instant - (long) displayOffset));
            } else {
                out.append(String.valueOf((instant - (long) displayOffset) / 1000L));
            }

        }

        public void printTo(StringBuffer buf, ReadablePartial partial, Locale locale) {
            if (this.hasMilliSecondPrecision) {
                buf.append(this.getDateTimeMillis(partial));
            } else {
                buf.append(this.getDateTimeMillis(partial) / 1000L);
            }

        }

        public void printTo(Writer out, ReadablePartial partial, Locale locale) throws IOException {
            if (this.hasMilliSecondPrecision) {
                out.append(String.valueOf(this.getDateTimeMillis(partial)));
            } else {
                out.append(String.valueOf(this.getDateTimeMillis(partial) / 1000L));
            }

        }

        private long getDateTimeMillis(ReadablePartial partial) {
            int year = partial.get(DateTimeFieldType.year());
            int monthOfYear = partial.get(DateTimeFieldType.monthOfYear());
            int dayOfMonth = partial.get(DateTimeFieldType.dayOfMonth());
            int hourOfDay = partial.get(DateTimeFieldType.hourOfDay());
            int minuteOfHour = partial.get(DateTimeFieldType.minuteOfHour());
            int secondOfMinute = partial.get(DateTimeFieldType.secondOfMinute());
            int millisOfSecond = partial.get(DateTimeFieldType.millisOfSecond());
            return partial.getChronology().getDateTimeMillis(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
    }

    static class EpochTimeParser implements DateTimeParser {
        private final boolean hasMilliSecondPrecision;

        EpochTimeParser(boolean hasMilliSecondPrecision) {
            this.hasMilliSecondPrecision = hasMilliSecondPrecision;
        }

        public int estimateParsedLength() {
            return this.hasMilliSecondPrecision ? 19 : 16;
        }

        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            boolean isPositive = !text.startsWith("-");
            int firstDotIndex = text.indexOf(46);
            boolean isTooLong = (firstDotIndex == -1 ? text.length() : firstDotIndex) > this.estimateParsedLength();
            if (bucket.getZone() != DateTimeZone.UTC) {
                String factor1 = this.hasMilliSecondPrecision ? "epoch_millis" : "epoch_second";
                throw new IllegalArgumentException("time_zone must be UTC for format [" + factor1 + "]");
            } else if (isPositive && isTooLong) {
                return -1;
            } else {
                int factor = this.hasMilliSecondPrecision ? 1 : 1000;

                try {
                    long e = (new BigDecimal(text)).longValue() * (long) factor;
                    DateTime dt = new DateTime(e, DateTimeZone.UTC);
                    bucket.saveField(DateTimeFieldType.year(), dt.getYear());
                    bucket.saveField(DateTimeFieldType.monthOfYear(), dt.getMonthOfYear());
                    bucket.saveField(DateTimeFieldType.dayOfMonth(), dt.getDayOfMonth());
                    bucket.saveField(DateTimeFieldType.hourOfDay(), dt.getHourOfDay());
                    bucket.saveField(DateTimeFieldType.minuteOfHour(), dt.getMinuteOfHour());
                    bucket.saveField(DateTimeFieldType.secondOfMinute(), dt.getSecondOfMinute());
                    bucket.saveField(DateTimeFieldType.millisOfSecond(), dt.getMillisOfSecond());
                    bucket.setZone(DateTimeZone.UTC);
                } catch (Exception var11) {
                    return -1;
                }

                return text.length();
            }
        }
    }
}

