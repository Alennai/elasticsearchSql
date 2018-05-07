package org.parc.sqlrestes.entity;

/**
 * Created by xusiao on 2018/5/4.
 */
public class ScriptType  {

    public static ScriptType INLINE=new ScriptType(0, new ParseField("source", new String[]{"inline"}), false);
    public static ScriptType STORED=new ScriptType(1, new ParseField("id", new String[]{"stored"}), false);
    private final int id;
    private final ParseField parseField;
    private final boolean defaultEnabled;
//    public static ScriptType readFrom(StreamInput in) throws IOException {
//        int id = in.readVInt();
//        if(STORED.id == id) {
//            return STORED;
//        } else if(INLINE.id == id) {
//            return INLINE;
//        } else {
//            throw new IllegalStateException("Error reading ScriptType id [" + id + "] from stream, expected one of [" + STORED.id + " [" + STORED.parseField.getPreferredName() + "], " + INLINE.id + " [" + INLINE.parseField.getPreferredName() + "]]");
//        }
//    }

    private ScriptType(int id, ParseField parseField, boolean defaultEnabled) {
        this.id = id;
        this.parseField = parseField;
        this.defaultEnabled = defaultEnabled;
    }



    public int getId() {
        return this.id;
    }

//    public String getName() {
//        return this.name().toLowerCase(Locale.ROOT);
//    }

    public ParseField getParseField() {
        return this.parseField;
    }

    public boolean isDefaultEnabled() {
        return this.defaultEnabled;
    }

//    public String toString() {
//        return this.getName();
//    }
}
