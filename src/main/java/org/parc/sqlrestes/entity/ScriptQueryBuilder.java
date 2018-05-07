package org.parc.sqlrestes.entity;


/**
 * Created by xusiao on 2018/5/4.
 */
public class ScriptQueryBuilder extends Query {
    public static final String NAME = "script";
    private final Script script;

    public ScriptQueryBuilder(Script script) {
        if(script == null) {
            throw new IllegalArgumentException("script cannot be null");
        } else {
            this.script = script;
        }
    }
    public String toString(String field) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("ScriptQuery(");
        buffer.append(this.script);
        buffer.append(")");
        return buffer.toString();
    }

    @Override
    public boolean equals(Object var1) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
