package org.apache.lucene.search.similarities;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.util.Collections;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class Similarity {

    /**
     * Sole constructor. (For invocation by subclass
     * constructors, typically implicit.)
     */
    public Similarity() {}

    /** Hook to integrate coordinate-level matching.
     * <p>
     * By default this is disabled (returns <code>1</code>), as with
     * most modern models this will only skew performance, but some
     * implementations such as {@link } override this.
     *
     * @param overlap the number of query terms matched in the document
     * @param maxOverlap the total number of terms in the query
     * @return a score factor based on term overlap with the query
     */
    public float coord(int overlap, int maxOverlap) {
        return 1f;
    }

    /** Computes the normalization value for a query given the sum of the
     * normalized weights {@link SimWeight#getValueForNormalization()} of
     * each of the query terms.  This value is passed back to the
     * weight ({@link SimWeight#normalize(float, float)} of each query
     * term, to provide a hook to attempt to make scores from different
     * queries comparable.
     * <p>
     * By default this is disabled (returns <code>1</code>), but some
     * implementations such as {@link } override this.
     *
     * @param valueForNormalization the sum of the term normalization values
     * @return a normalization factor for query weights
     */
    public float queryNorm(float valueForNormalization) {
        return 1f;
    }

    /**
     * Computes the normalization value for a field, given the accumulated
     * state of term processing for this field (see {@link FieldInvertState}).
     *
     * <p>Matches in longer fields are less precise, so implementations of this
     * method usually set smaller values when <code>state.getLength()</code> is large,
     * and larger values when <code>state.getLength()</code> is small.
     *
     * @lucene.experimental
     *
     * @param state current processing state for this field
     * @return computed norm value
     */
    public abstract long computeNorm(FieldInvertState state);

    /**
     * Compute any collection-level weight (e.g. IDF, average document length, etc) needed for scoring a query.
     *
     * @param collectionStats collection-level statistics, such as the number of tokens in the collection.
     * @param termStats term-level statistics, such as the document frequency of a term across the collection.
     * @return SimWeight object with the information this Similarity needs to score a query.
     */
    public abstract SimWeight computeWeight(CollectionStatistics collectionStats, TermStatistics... termStats);

    /**
     * Creates a new {@link Similarity.SimScorer} to score matching documents from a segment of the inverted index.
     * @param weight collection information from {@link #computeWeight(CollectionStatistics, TermStatistics...)}
     * @param context segment of the inverted index to be scored.
     * @return SloppySimScorer for scoring documents across <code>context</code>
     * @throws IOException if there is a low-level I/O error
     */
    public abstract SimScorer simScorer(SimWeight weight, LeafReaderContext context) throws IOException;

    /**
     * API for scoring "sloppy" queries such as {@link },
     * {@link }, and {@link }.
     * <p>
     * Frequencies are floating-point values: an approximate
     * within-document frequency adjusted for "sloppiness" by
     * {@link SimScorer#computeSlopFactor(int)}.
     */
    public static abstract class SimScorer {

        /**
         * Sole constructor. (For invocation by subclass
         * constructors, typically implicit.)
         */
        public SimScorer() {}

        /**
         * Score a single document
         * @param doc document id within the inverted index segment
         * @param freq sloppy term frequency
         * @return document's score
         */
        public abstract float score(int doc, float freq);

        /** Computes the amount of a sloppy phrase match, based on an edit distance. */
        public abstract float computeSlopFactor(int distance);

        /** Calculate a scoring factor based on the data in the payload. */
        public abstract float computePayloadFactor(int doc, int start, int end, BytesRef payload);

        /**
         * Explain the score for a single document
         * @param doc document id within the inverted index segment
         * @param freq Explanation of how the sloppy term frequency was computed
         * @return document's score
         */
        public Explanation explain(int doc, Explanation freq) {
            return Explanation.match(
                    score(doc, freq.getValue()),
                    "score(doc=" + doc + ",freq=" + freq.getValue() +"), with freq of:",
                    Collections.singleton(freq));
        }
    }

    /** Stores the weight for a query across the indexed collection. This abstract
     * implementation is empty; descendants of {@code Similarity} should
     * subclass {@code SimWeight} and define the statistics they require in the
     * subclass. Examples include idf, average field length, etc.
     */
    public static abstract class SimWeight {

        /**
         * Sole constructor. (For invocation by subclass
         * constructors, typically implicit.)
         */
        public SimWeight() {}

        /** The value for normalization of contained query clauses (e.g. sum of squared weights).
         * <p>
         * NOTE: a Similarity implementation might not use any query normalization at all,
         * it's not required. However, if it wants to participate in query normalization,
         * it can return a value here.
         */
        public abstract float getValueForNormalization();

        /** Assigns the query normalization factor and boost from parent queries to this.
         * <p>
         * NOTE: a Similarity implementation might not use this normalized value at all,
         * it's not required. However, it's usually a good idea to at least incorporate
         * the boost into its score.
         * <p>
         * NOTE: If this method is called several times, it behaves as if only the
         * last call was performed.
         */
        public abstract void normalize(float queryNorm, float boost);
    }
}
