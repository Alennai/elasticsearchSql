package org.parc.sqlrestes.entity;


import org.parc.sqlrestes.entity.log.DeprecationLogger;
import org.parc.sqlrestes.entity.log.Loggers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

/**
 * Created by xusiao on 2018/5/4.
 */
public class ParseField {
    private static final DeprecationLogger DEPRECATION_LOGGER = new DeprecationLogger(Loggers.getLogger(ParseField.class));
    private final String name;
    private final String[] deprecatedNames;
    private String allReplacedWith = null;
    private final String[] allNames;

    public ParseField(String name, String... deprecatedNames) {
        this.name = name;
        HashSet allNames;
        if(deprecatedNames != null && deprecatedNames.length != 0) {
            allNames = new HashSet();
            Collections.addAll(allNames, deprecatedNames);
            this.deprecatedNames = (String[])allNames.toArray(new String[allNames.size()]);
        } else {
            this.deprecatedNames = Strings.EMPTY_ARRAY;
        }

        allNames = new HashSet();
        allNames.add(name);
        Collections.addAll(allNames, this.deprecatedNames);
        this.allNames = (String[])allNames.toArray(new String[allNames.size()]);
    }

    public String getPreferredName() {
        return this.name;
    }

    public String[] getAllNamesIncludedDeprecated() {
        return this.allNames;
    }

    public ParseField withDeprecation(String... deprecatedNames) {
        return new ParseField(this.name, deprecatedNames);
    }

    public ParseField withAllDeprecated(String allReplacedWith) {
        ParseField parseField = this.withDeprecation(this.getAllNamesIncludedDeprecated());
        parseField.allReplacedWith = allReplacedWith;
        return parseField;
    }

    public boolean match(String fieldName) {
        Objects.requireNonNull(fieldName, "fieldName cannot be null");
        if(this.allReplacedWith == null && fieldName.equals(this.name)) {
            return true;
        } else {
            String[] var3 = this.deprecatedNames;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String depName = var3[var5];
                if(fieldName.equals(depName)) {
                    String msg = "Deprecated field [" + fieldName + "] used, expected [" + this.name + "] instead";
                    if(this.allReplacedWith != null) {
                        msg = "Deprecated field [" + fieldName + "] used, replaced by [" + this.allReplacedWith + "]";
                    }

                    DEPRECATION_LOGGER.deprecated(msg, new Object[0]);
                    return true;
                }
            }

            return false;
        }
    }

    public String toString() {
        return this.getPreferredName();
    }

    public String getAllReplacedWith() {
        return this.allReplacedWith;
    }

    public String[] getDeprecatedNames() {
        return this.deprecatedNames;
    }

    public static class CommonFields {
        public static final ParseField FIELD = new ParseField("field", new String[0]);
        public static final ParseField FIELDS = new ParseField("fields", new String[0]);
        public static final ParseField FORMAT = new ParseField("format", new String[0]);
        public static final ParseField MISSING = new ParseField("missing", new String[0]);
        public static final ParseField TIME_ZONE = new ParseField("time_zone", new String[0]);

        public CommonFields() {
        }
    }
}
