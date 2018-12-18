package org.parc.sqlrestes.entity.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

import java.util.WeakHashMap;

/**
 * Created by xusiao on 2018/5/4.
 */
class PrefixLogger extends ExtendedLoggerWrapper {
    private static final WeakHashMap<String, Marker> markers = new WeakHashMap();
    private final Marker marker;

    PrefixLogger(ExtendedLogger logger, String name, String prefix) {
        super(logger, name, null);
        String actualPrefix = (prefix == null ? "" : prefix).intern();
        WeakHashMap var6 = markers;
        Object actualMarker;
        synchronized (markers) {
            Marker maybeMarker = markers.get(actualPrefix);
            if (maybeMarker == null) {
                actualMarker = new MarkerManager.Log4jMarker(actualPrefix);
                markers.put(actualPrefix, (Marker) actualMarker);
            } else {
                actualMarker = maybeMarker;
            }
        }

        this.marker = (Marker) actualMarker;
    }

    static int markersSize() {
        return markers.size();
    }

    public String prefix() {
        return this.marker.getName();
    }

    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        assert marker == null;

        super.logMessage(fqcn, level, this.marker, message, t);
    }
}

