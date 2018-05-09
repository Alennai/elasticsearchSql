package org.apache.lucene.search;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class ScorerSupplier{

    /**
     * Get the {@link Scorer}. This may not return {@code null} and must be called
     * at most once.
     * @param randomAccess A hint about the expected usage of the {@link Scorer}.
     * If {@link DocIdSetIterator#advance} or {@link TwoPhaseIterator} will be
     * used to check whether given doc ids match, then pass {@code true}.
     * Otherwise if the {@link Scorer} will be mostly used to lead the iteration
     * using {@link DocIdSetIterator#nextDoc()}, then {@code false} should be
     * passed. Under doubt, pass {@code false} which usually has a better
     * worst-case.
     */
    public abstract Scorer get(boolean randomAccess) throws IOException;

    /**
     * Get an estimate of the {@link Scorer} that would be returned by {@link #get}.
     * This may be a costly operation, so it should only be called if necessary.
     * @see DocIdSetIterator#cost
     */
    public abstract long cost();

}
