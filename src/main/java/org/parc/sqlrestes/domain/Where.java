package org.parc.sqlrestes.domain;

import java.util.LinkedList;

public class Where implements Cloneable {

    CONN conn;
    private LinkedList<Where> wheres = new LinkedList<>();

    public Where(String connStr) {
        this.conn = CONN.valueOf(connStr.toUpperCase());
    }

    Where(CONN conn) {
        this.conn = conn;
    }

    public static Where newInstance() {
        return new Where(CONN.AND);
    }

    public void addWhere(Where where) {
        wheres.add(where);
    }

    public CONN getConn() {
        return this.conn;
    }

    public void setConn(CONN conn) {
        this.conn = conn;
    }

    public LinkedList<Where> getWheres() {
        return wheres;
    }

    @Override
    public String toString() {
        if (wheres.size() > 0) {
            String whereStr = wheres.toString();
            return this.conn + " ( " + whereStr.substring(1, whereStr.length() - 1) + " ) ";
        } else {
            return "";
        }

    }

    @Override
    public Object clone() {
        Where clonedWhere = new Where(this.getConn());
        for (Where innerWhere : this.getWheres()) {
            clonedWhere.addWhere((Where) innerWhere.clone());
        }
        return clonedWhere;
    }

    public enum CONN {
        AND, OR;

        public CONN negative() {
            return this == AND ? OR : AND;
        }
    }
}
