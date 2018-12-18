package org.parc.sqlrestes.entity;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by xusiao on 2018/5/4.
 */
class FastStringReader extends Reader implements CharSequence {
    private String str;
    private int length;
    private int next = 0;
    private int mark = 0;
    private boolean closed = false;

    public FastStringReader(String s) {
        this.str = s;
        this.length = s.length();
    }

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public char charAt(int index) {
        return this.str.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.str.subSequence(start, end);
    }

    @Override
    public int read() throws IOException {
        this.ensureOpen();
        return this.next >= this.length ? -1 : this.str.charAt(this.next++);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        this.ensureOpen();
        if (len == 0) {
            return 0;
        } else if (this.next >= this.length) {
            return -1;
        } else {
            int n = Math.min(this.length - this.next, len);
            this.str.getChars(this.next, this.next + n, cbuf, off);
            this.next += n;
            return n;
        }
    }

    @Override
    public long skip(long ns) throws IOException {
        this.ensureOpen();
        if (this.next >= this.length) {
            return 0L;
        } else {
            long n = Math.min((long) (this.length - this.next), ns);
            n = Math.max((long) (-this.next), n);
            this.next = (int) ((long) this.next + n);
            return n;
        }
    }

    @Override
    public boolean ready() throws IOException {
        this.ensureOpen();
        return true;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        } else {
            this.ensureOpen();
            this.mark = this.next;
        }
    }

    @Override
    public void reset() throws IOException {
        this.ensureOpen();
        this.next = this.mark;
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public String toString() {
        return this.str;
    }
}

