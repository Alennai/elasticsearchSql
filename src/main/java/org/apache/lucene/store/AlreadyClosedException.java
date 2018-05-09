package org.apache.lucene.store;

/**
 * Created by xusiao on 2018/5/8.
 */
public class AlreadyClosedException extends IllegalStateException {
    public AlreadyClosedException(String message) {
        super(message);
    }

    public AlreadyClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
