package org.apache.lucene.util;

import java.io.Closeable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xusiao on 2018/5/8.
 */
public class CloseableThreadLocal <T> implements Closeable {

    private ThreadLocal<WeakReference<T>> t = new ThreadLocal<>();

    // Use a WeakHashMap so that if a Thread exits and is
    // GC'able, its entry may be removed:
    private Map<Thread,T> hardRefs = new WeakHashMap<>();

    // Increase this to decrease frequency of purging in get:
    private static int PURGE_MULTIPLIER = 20;

    // On each get or set we decrement this; when it hits 0 we
    // purge.  After purge, we set this to
    // PURGE_MULTIPLIER * stillAliveCount.  This keeps
    // amortized cost of purging linear.
    private final AtomicInteger countUntilPurge = new AtomicInteger(PURGE_MULTIPLIER);

    protected T initialValue() {
        return null;
    }

    public T get() {
        WeakReference<T> weakRef = t.get();
        if (weakRef == null) {
            T iv = initialValue();
            if (iv != null) {
                set(iv);
                return iv;
            } else {
                return null;
            }
        } else {
            maybePurge();
            return weakRef.get();
        }
    }

    public void set(T object) {

        t.set(new WeakReference<>(object));

        synchronized(hardRefs) {
            hardRefs.put(Thread.currentThread(), object);
            maybePurge();
        }
    }

    private void maybePurge() {
        if (countUntilPurge.getAndDecrement() == 0) {
            purge();
        }
    }

    // Purge dead threads
    private void purge() {
        synchronized(hardRefs) {
            int stillAliveCount = 0;
            for (Iterator<Thread> it = hardRefs.keySet().iterator(); it.hasNext();) {
                final Thread t = it.next();
                if (!t.isAlive()) {
                    it.remove();
                } else {
                    stillAliveCount++;
                }
            }
            int nextCount = (1+stillAliveCount) * PURGE_MULTIPLIER;
            if (nextCount <= 0) {
                // defensive: int overflow!
                nextCount = 1000000;
            }

            countUntilPurge.set(nextCount);
        }
    }

    @Override
    public void close() {
        // Clear the hard refs; then, the only remaining refs to
        // all values we were storing are weak (unless somewhere
        // else is still using them) and so GC may reclaim them:
        hardRefs = null;
        // Take care of the current thread right now; others will be
        // taken care of via the WeakReferences.
        if (t != null) {
            t.remove();
        }
        t = null;
    }
}

