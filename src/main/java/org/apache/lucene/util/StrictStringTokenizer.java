package org.apache.lucene.util;

/**
 * Created by xusiao on 2018/5/8.
 */
final class StrictStringTokenizer  {

    public StrictStringTokenizer(String s, char delimiter) {
        this.s = s;
        this.delimiter = delimiter;
    }

    public final String nextToken() {
        if (pos < 0) {
            throw new IllegalStateException("no more tokens");
        }

        int pos1 = s.indexOf(delimiter, pos);
        String s1;
        if (pos1 >= 0) {
            s1 = s.substring(pos, pos1);
            pos = pos1+1;
        } else {
            s1 = s.substring(pos);
            pos=-1;
        }

        return s1;
    }

    public final boolean hasMoreTokens() {
        return pos >= 0;
    }

    private final String s;
    private final char delimiter;
    private int pos;
}
