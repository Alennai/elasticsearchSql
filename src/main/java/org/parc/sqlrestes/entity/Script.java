package org.parc.sqlrestes.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by xusiao on 2018/5/4.
 */
public class Script {
    public static  String DEFAULT_SCRIPT_LANG = "painless";
    public static  String DEFAULT_TEMPLATE_LANG = "mustache";
    public static  String CONTENT_TYPE_OPTION = "content_type";
    private static  ScriptType DEFAULT_SCRIPT_TYPE;
    private static  ParseField SCRIPT_PARSE_FIELD;
    private static  ParseField SOURCE_PARSE_FIELD;
    private static  ParseField LANG_PARSE_FIELD;
    private static  ParseField OPTIONS_PARSE_FIELD;
    private static  ParseField PARAMS_PARSE_FIELD;
//    private static final ObjectParser<Script.Builder, Void> PARSER;
    private  ScriptType type=null;
    private  String lang=null;
    private  String idOrCode=null;
    private  Map<String, String> options=new HashMap<>();
    private Map<String, Object> params=new HashMap<>();



    static {
        DEFAULT_SCRIPT_TYPE = ScriptType.INLINE;
        SCRIPT_PARSE_FIELD = new ParseField("script");
        SOURCE_PARSE_FIELD = new ParseField("source");
        LANG_PARSE_FIELD = new ParseField("lang");
        OPTIONS_PARSE_FIELD = new ParseField("options");
        PARAMS_PARSE_FIELD = new ParseField("params");

//        PARSER = new ObjectParser("script", () -> {
//            return new Script.Builder();
//        });
//        PARSER.declareField((rec$, x$0) -> {
//            ((Script.Builder)rec$).setInline(x$0);
//        }, (parser) -> {
//            return parser;
//        }, ScriptType.INLINE.getParseField(), ValueType.OBJECT_OR_STRING);
//        PARSER.declareString((rec$, x$0) -> {
//            ((Script.Builder)rec$).setStored(x$0);
//        }, ScriptType.STORED.getParseField());
//        PARSER.declareString((rec$, x$0) -> {
//            ((Script.Builder)rec$).setLang(x$0);
//        }, LANG_PARSE_FIELD);
//        PARSER.declareField((rec$, x$0) -> {
//            ((Script.Builder)rec$).setOptions(x$0);
//        }, XContentParser::mapStrings, OPTIONS_PARSE_FIELD, ValueType.OBJECT);
//        PARSER.declareField((rec$, x$0) -> {
//            ((Script.Builder)rec$).setParams(x$0);
//        }, XContentParser::map, PARAMS_PARSE_FIELD, ValueType.OBJECT);
    }

    public Script(String idOrCode) {
        this(DEFAULT_SCRIPT_TYPE, "painless", idOrCode, Collections.emptyMap(), Collections.emptyMap());
    }

    private Script(ScriptType type, String lang, String idOrCode, Map<String, String> options, Map<String, Object> params) {
        this.type = (ScriptType) Objects.requireNonNull(type);
        this.idOrCode = (String)Objects.requireNonNull(idOrCode);
        this.params = Collections.unmodifiableMap((Map)Objects.requireNonNull(params));
        if(type == ScriptType.INLINE) {
            this.lang = (String)Objects.requireNonNull(lang);
            this.options = Collections.unmodifiableMap((Map)Objects.requireNonNull(options));
        } else {
            if(type != ScriptType.STORED) {
//                throw new IllegalStateException("unknown script type [" + type.getName() + "]");
                throw new IllegalStateException("unknown script type [ ]");
            }

            if(lang != null) {
                throw new IllegalArgumentException("lang cannot be specified for stored scripts");
            }

            this.lang = null;
            if(options != null) {
                throw new IllegalStateException("options cannot be specified for stored scripts");
            }

            this.options = null;
        }

    }


    public String toString() {
        return "Script{type=" + this.type + ", lang=\'" + this.lang + '\'' + ", idOrCode=\'" + this.idOrCode + '\'' + ", options=" + this.options + ", params=" + this.params + '}';
    }
    private static final class Builder {
        private ScriptType type;
        private String lang;
        private String idOrCode;
        private Map<String, String> options;
        private Map<String, Object> params;

        private Builder() {
            this.options = new HashMap();
            this.params = Collections.emptyMap();
        }

//        private void setInline(XContentParser parser) {
//            try {
//                if(this.type != null) {
//                    this.throwOnlyOneOfType();
//                }
//
//                this.type = ScriptType.INLINE;
//                if(parser.currentToken() == Token.START_OBJECT) {
//                    XContentBuilder exception = XContentFactory.jsonBuilder();
//                    this.idOrCode = exception.copyCurrentStructure(parser).string();
//                    this.options.put("content_type", XContentType.JSON.mediaType());
//                } else {
//                    this.idOrCode = parser.text();
//                }
//
//            } catch (IOException var3) {
//                throw new UncheckedIOException(var3);
//            }
//        }

        private void setStored(String idOrCode) {
            if(this.type != null) {
                this.throwOnlyOneOfType();
            }

            this.type = ScriptType.STORED;
            this.idOrCode = idOrCode;
        }

        private void throwOnlyOneOfType() {
            throw new IllegalArgumentException("must only use one of [" + ScriptType.INLINE.getParseField().getPreferredName() + ", " + ScriptType.STORED.getParseField().getPreferredName() + "] when specifying a script");
        }

        private void setLang(String lang) {
            this.lang = lang;
        }

        private void setOptions(Map<String, String> options) {
            this.options.putAll(options);
        }

        private void setParams(Map<String, Object> params) {
            this.params = params;
        }

        private Script build(String defaultLang) {
            if(this.type == null) {
                throw new IllegalArgumentException("must specify either [source] for an inline script or [id] for a stored script");
            } else {
                if(this.type == ScriptType.INLINE) {
                    if(this.lang == null) {
                        this.lang = defaultLang;
                    }

                    if(this.idOrCode == null) {
                        throw new IllegalArgumentException("must specify <id> for an inline script");
                    }

                    if(this.options.size() > 1 || this.options.size() == 1 && this.options.get("content_type") == null) {
                        this.options.remove("content_type");
                        throw new IllegalArgumentException("illegal compiler options [" + this.options + "] specified");
                    }
                } else if(this.type == ScriptType.STORED) {
                    if(this.lang != null) {
                        throw new IllegalArgumentException("illegally specified <lang> for a stored script");
                    }

                    if(this.idOrCode == null) {
                        throw new IllegalArgumentException("must specify <code> for a stored script");
                    }

                    if(!this.options.isEmpty()) {
                        throw new IllegalArgumentException("field [" + Script.OPTIONS_PARSE_FIELD.getPreferredName() + "] cannot be specified using a stored script");
                    }

                    this.options = null;
                }

                return new Script(this.type, this.lang, this.idOrCode, this.options, this.params);
            }
        }
    }

}
