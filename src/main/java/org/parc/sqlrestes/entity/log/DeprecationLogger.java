package org.parc.sqlrestes.entity.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.jvm.hotspot.debugger.ThreadContext;

import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xusiao on 2018/5/4.
 */
public class DeprecationLogger {
    private static final CopyOnWriteArraySet<ThreadContext> THREAD_CONTEXT = new CopyOnWriteArraySet();
    private static final String WARNING_FORMAT;
    private static final DateTimeFormatter RFC_7231_DATE_TIME;
    private static final ZoneId GMT;
    private static final Charset UTF_8;
    private static Pattern WARNING_HEADER_PATTERN;
    private static BitSet doesNotNeedEncoding;

    static {
        WARNING_FORMAT = String.format(Locale.ROOT, "299 Elasticsearch-%s%s-%s ") + "\"%s\" \"%s\"";
        HashMap i = new HashMap();
        i.put(1L, "Mon");
        i.put(2L, "Tue");
        i.put(3L, "Wed");
        i.put(4L, "Thu");
        i.put(5L, "Fri");
        i.put(6L, "Sat");
        i.put(7L, "Sun");
        HashMap moy = new HashMap();
        moy.put(1L, "Jan");
        moy.put(2L, "Feb");
        moy.put(3L, "Mar");
        moy.put(4L, "Apr");
        moy.put(5L, "May");
        moy.put(6L, "Jun");
        moy.put(7L, "Jul");
        moy.put(8L, "Aug");
        moy.put(9L, "Sep");
        moy.put(10L, "Oct");
        moy.put(11L, "Nov");
        moy.put(12L, "Dec");
        RFC_7231_DATE_TIME = (new DateTimeFormatterBuilder()).parseCaseInsensitive().parseLenient().optionalStart().appendText(ChronoField.DAY_OF_WEEK, i).appendLiteral(", ").optionalEnd().appendValue(ChronoField.DAY_OF_MONTH, 2, 2, SignStyle.NOT_NEGATIVE).appendLiteral(' ').appendText(ChronoField.MONTH_OF_YEAR, moy).appendLiteral(' ').appendValue(ChronoField.YEAR, 4).appendLiteral(' ').appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral(':').appendValue(ChronoField.MINUTE_OF_HOUR, 2).optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalEnd().appendLiteral(' ').appendOffset("+HHMM", "GMT").toFormatter(Locale.getDefault(Locale.Category.FORMAT));
        GMT = ZoneId.of("GMT");
        WARNING_HEADER_PATTERN = Pattern.compile("299 Elasticsearch-\\d+\\.\\d+\\.\\d+(?:-(?:alpha|beta|rc)\\d+)?(?:-SNAPSHOT)?-(?:[a-f0-9]{7}|Unknown) \"((?:\t| |!|[\\x23-\\x5B]|[\\x5D-\\x7E]|[\\x80-\\xFF]|\\\\|\\\\\")*)\" \"(?:Mon|Tue|Wed|Thu|Fri|Sat|Sun), \\d{2} (?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec) \\d{4} \\d{2}:\\d{2}:\\d{2} GMT\"");
        doesNotNeedEncoding = new BitSet(256);
        doesNotNeedEncoding.set(9);
        doesNotNeedEncoding.set(32);
        doesNotNeedEncoding.set(33);
        doesNotNeedEncoding.set(92);
        doesNotNeedEncoding.set(34);

        int var2;
        for (var2 = 35; var2 <= 36; ++var2) {
            doesNotNeedEncoding.set(var2);
        }

        for (var2 = 38; var2 <= 91; ++var2) {
            doesNotNeedEncoding.set(var2);
        }

        for (var2 = 93; var2 <= 126; ++var2) {
            doesNotNeedEncoding.set(var2);
        }

        for (var2 = 128; var2 <= 255; ++var2) {
            doesNotNeedEncoding.set(var2);
        }

        assert !doesNotNeedEncoding.get(37);

        UTF_8 = Charset.forName("UTF-8");
    }

    private final Logger logger;
    private Set<String> keys = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap() {
        protected boolean removeEldestEntry() {
            return this.size() > 128;
        }
    }));

    public DeprecationLogger(Logger parentLogger) {
        String name = parentLogger.getName();
        if (name.startsWith("org.elasticsearch")) {
            name = name.replace("org.elasticsearch.", "org.elasticsearch.deprecation.");
        } else {
            name = "deprecation." + name;
        }

        this.logger = LogManager.getLogger(name);
    }

    public static void setThreadContext(ThreadContext threadContext) {
        Objects.requireNonNull(threadContext, "Cannot register a null ThreadContext");
        if (!THREAD_CONTEXT.add(threadContext)) {
            throw new IllegalStateException("Double-setting ThreadContext not allowed!");
        }
    }

    public static void removeThreadContext(ThreadContext threadContext) {
        assert threadContext != null;

        if (!THREAD_CONTEXT.remove(threadContext)) {
            throw new IllegalStateException("Removing unknown ThreadContext not allowed!");
        }
    }

    private static String extractWarningValueFromWarningHeader(String s) {
        int firstQuote = s.indexOf(34);
        int lastQuote = s.lastIndexOf(34);
        int penultimateQuote = s.lastIndexOf(34, lastQuote - 1);
        String warningValue = s.substring(firstQuote + 1, penultimateQuote - 2);

        assert assertWarningValue(s, warningValue);

        return warningValue;
    }

    private static boolean assertWarningValue(String s, String warningValue) {
        Matcher matcher = WARNING_HEADER_PATTERN.matcher(s);
        boolean matches = matcher.matches();

        assert matches;

        return matcher.group(1).equals(warningValue);
    }

    private static String formatWarning(String s) {
        return String.format(Locale.ROOT, WARNING_FORMAT, escapeAndEncode(s), RFC_7231_DATE_TIME.format(ZonedDateTime.now(GMT)));
    }

    private static String escapeAndEncode(String s) {
        return encode(escapeBackslashesAndQuotes(s));
    }

    private static String escapeBackslashesAndQuotes(String s) {
        return s.replaceAll("([\"\\\\])", "\\\\$1");
    }

    private static String encode(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        boolean encodingNeeded = false;
        int i = 0;

        while (true) {
            while (i < s.length()) {
                char current = s.charAt(i);
                if (doesNotNeedEncoding.get(current)) {
                    sb.append(current);
                    ++i;
                } else {
                    int startIndex = i;

                    do {
                        ++i;
                    } while (i < s.length() && !doesNotNeedEncoding.get(s.charAt(i)));

                    byte[] bytes = s.substring(startIndex, i).getBytes(UTF_8);

                    for (int j = 0; j < bytes.length; ++j) {
                        sb.append('%').append(hex(bytes[j] >> 4)).append(hex(bytes[j]));
                    }

                    encodingNeeded = true;
                }
            }

            return encodingNeeded ? sb.toString() : s;
        }
    }

    private static char hex(int b) {
        char ch = Character.forDigit(b & 15, 16);
        return Character.isLetter(ch) ? Character.toUpperCase(ch) : ch;
    }

    public void deprecated(String msg, Object... params) {
        this.deprecated(THREAD_CONTEXT, msg, params);
    }

    public void deprecatedAndMaybeLog(String key, String msg, Object... params) {
        this.deprecated(THREAD_CONTEXT, msg, this.keys.add(key), params);
    }

    private void deprecated(Set<ThreadContext> threadContexts, String message, Object... params) {
        this.deprecated(threadContexts, message, true, params);
    }

    @SuppressLoggerChecks(
            reason = "safely delegates to logger"
    )
    private void deprecated(Set<ThreadContext> threadContexts, String message, boolean log, Object... params) {
        Iterator iterator = threadContexts.iterator();
        if (iterator.hasNext()) {
            String formattedMessage = LoggerMessageFormat.format(message, params);
            String warningHeaderValue = formatWarning(formattedMessage);

            assert WARNING_HEADER_PATTERN.matcher(warningHeaderValue).matches();

            assert extractWarningValueFromWarningHeader(warningHeaderValue).equals(escapeAndEncode(formattedMessage));

            while (iterator.hasNext()) {
                try {
                    ThreadContext next = (ThreadContext) iterator.next();
//                    next.addResponseHeader("Warning", warningHeaderValue, DeprecationLogger::extractWarningValueFromWarningHeader);
                } catch (IllegalStateException ignored) {
                }
            }
        }

        if (log) {
            this.logger.warn(message, params);
        }

    }
}

