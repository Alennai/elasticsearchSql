package org.parc.sqlrestes.entity;

import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.Objects;
import java.util.function.LongSupplier;

/**
 * Created by xusiao on 2018/5/4.
 */
class DateMathParser{
    private final FormatDateTimeFormatter dateTimeFormatter;

    public DateMathParser(FormatDateTimeFormatter dateTimeFormatter) {
        Objects.requireNonNull(dateTimeFormatter);
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public long parse(String text, LongSupplier now) {
        return this.parse(text, now, false, (DateTimeZone)null);
    }

    private long parse(String text, LongSupplier now, boolean roundUp, DateTimeZone timeZone) {
        long time;
        String mathString;
        if(text.startsWith("now")) {
            try {
                time = now.getAsLong();
            } catch (Exception var9) {
//                throw new ElasticsearchParseException("could not read the current timestamp", var9, new Object[0]);
                throw null;
            }

            mathString = text.substring("now".length());
        } else {
            int index = text.indexOf("||");
            if(index == -1) {
                return this.parseDateTime(text, timeZone, roundUp);
            }

            time = this.parseDateTime(text.substring(0, index), timeZone, false);
            mathString = text.substring(index + 2);
        }

        return this.parseMath(mathString, time, roundUp, timeZone);
    }

    private long parseMath(String mathString, long time, boolean roundUp, DateTimeZone timeZone)  {
        if(timeZone == null) {
            timeZone = DateTimeZone.UTC;
        }

        MutableDateTime dateTime = new MutableDateTime(time, timeZone);
        int i = 0;

        while(i < mathString.length()) {
            char c = mathString.charAt(i++);
            boolean round;
            byte sign;
            if(c == 47) {
                round = true;
                sign = 1;
            } else {
                round = false;
                if(c == 43) {
                    sign = 1;
                } else {
                    if(c != 45) {
//                        throw new ElasticsearchParseException("operator not supported for date math [{}]", new Object[]{mathString});
                        throw null;
                    }

                    sign = -1;
                }
            }

            if(i >= mathString.length()) {
//                throw new ElasticsearchParseException("truncated date math [{}]", new Object[]{mathString});
                throw null;
            }

            int num;
            if(!Character.isDigit(mathString.charAt(i))) {
                num = 1;
            } else {
                int unit;
                for(unit = i; i < mathString.length() && Character.isDigit(mathString.charAt(i)); ++i) {
                }

                if(i >= mathString.length()) {
//                    throw new ElasticsearchParseException("truncated date math [{}]", new Object[]{mathString});
                    throw null;
                }

                num = Integer.parseInt(mathString.substring(unit, i));
            }

            if(round && num != 1) {
//                throw new ElasticsearchParseException("rounding `/` can only be used on single unit types [{}]", new Object[]{mathString});
                throw null;
            }

            char var14 = mathString.charAt(i++);
            MutableDateTime.Property propertyToRound = null;
            switch(var14) {
                case 'H':
                case 'h':
                    if(round) {
                        propertyToRound = dateTime.hourOfDay();
                    } else {
                        dateTime.addHours(sign * num);
                    }
                    break;
                case 'M':
                    if(round) {
                        propertyToRound = dateTime.monthOfYear();
                    } else {
                        dateTime.addMonths(sign * num);
                    }
                    break;
                case 'd':
                    if(round) {
                        propertyToRound = dateTime.dayOfMonth();
                    } else {
                        dateTime.addDays(sign * num);
                    }
                    break;
                case 'm':
                    if(round) {
                        propertyToRound = dateTime.minuteOfHour();
                    } else {
                        dateTime.addMinutes(sign * num);
                    }
                    break;
                case 's':
                    if(round) {
                        propertyToRound = dateTime.secondOfMinute();
                    } else {
                        dateTime.addSeconds(sign * num);
                    }
                    break;
                case 'w':
                    if(round) {
                        propertyToRound = dateTime.weekOfWeekyear();
                    } else {
                        dateTime.addWeeks(sign * num);
                    }
                    break;
                case 'y':
                    if(round) {
                        propertyToRound = dateTime.yearOfCentury();
                    } else {
                        dateTime.addYears(sign * num);
                    }
                    break;
                default:
                    throw null;
            }

            if(propertyToRound != null) {
                if(roundUp) {
                    propertyToRound.add(1);
                    propertyToRound.roundFloor();
                    dateTime.addMillis(-1);
                } else {
                    propertyToRound.roundFloor();
                }
            }
        }

        return dateTime.getMillis();
    }

    private long parseDateTime(String value, DateTimeZone timeZone, boolean roundUpIfNoTime) {
        DateTimeFormatter parser = this.dateTimeFormatter.parser();
        if(timeZone != null) {
            parser = parser.withZone(timeZone);
        }

        try {
            MutableDateTime e;
            if(roundUpIfNoTime) {
                e = new MutableDateTime(1970, 1, 1, 23, 59, 59, 999, DateTimeZone.UTC);
            } else {
                e = new MutableDateTime(1970, 1, 1, 0, 0, 0, 0, DateTimeZone.UTC);
            }

            int end = parser.parseInto(e, value, 0);
            if(end < 0) {
                int position = ~end;
                throw new IllegalArgumentException("Parse failure at index [" + position + "] of [" + value + "]");
            } else if(end != value.length()) {
                throw new IllegalArgumentException("Unrecognized chars at the end of [" + value + "]: [" + value.substring(end) + "]");
            } else {
                return e.getMillis();
            }
        } catch (IllegalArgumentException var8) {
            var8.printStackTrace();
            throw var8;
//            throw new ("failed to parse date field [{}] with format [{}]", var8, new Object[]{value, this.dateTimeFormatter.format()});
        }
    }
}

