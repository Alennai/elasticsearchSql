package org.apache.lucene.index;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.automaton.CompiledAutomaton;

import java.io.IOException;

/**
 * Created by xusiao on 2018/5/8.
 */
public abstract class SortedDocValues  extends BinaryDocValues {

    /** Sole constructor. (For invocation by subclass
     * constructors, typically implicit.) */
    protected SortedDocValues() {}

    /**
     * Returns the ordinal for the specified docID.
     * @param  docID document ID to lookup
     * @return ordinal for the document: this is dense, starts at 0, then
     *         increments by 1 for the next value in sorted order. Note that
     *         missing values are indicated by -1.
     */
    public abstract int getOrd(int docID);

    /** Retrieves the value for the specified ordinal. The returned
     * {@link BytesRef} may be re-used across calls to {@link #lookupOrd(int)}
     * so make sure to {@link BytesRef#deepCopyOf(BytesRef) copy it} if you want
     * to keep it around.
     * @param ord ordinal to lookup (must be &gt;= 0 and &lt; {@link #getValueCount()})
     * @see #getOrd(int)
     */
    public abstract BytesRef lookupOrd(int ord);

    /**
     * Returns the number of unique values.
     * @return number of unique values in this SortedDocValues. This is
     *         also equivalent to one plus the maximum ordinal.
     */
    public abstract int getValueCount();

    private final BytesRef empty = new BytesRef();

    @Override
    public BytesRef get(int docID) {
        int ord = getOrd(docID);
        if (ord == -1) {
            return empty;
        } else {
            return lookupOrd(ord);
        }
    }

    /** If {@code key} exists, returns its ordinal, else
     *  returns {@code -insertionPoint-1}, like {@code
     *  Arrays.binarySearch}.
     *
     *  @param key Key to look up
     **/
    public int lookupTerm(BytesRef key) {
        int low = 0;
        int high = getValueCount()-1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            final BytesRef term = lookupOrd(mid);
            int cmp = term.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }

        return -(low + 1);  // key not found.
    }

    /**
     * Returns a {@link TermsEnum} over the values.
     * The enum supports {@link TermsEnum#ord()} and {@link TermsEnum#seekExact(long)}.
     */
    public TermsEnum termsEnum() {
        return new SortedDocValuesTermsEnum(this);
    }

    /**
     * Returns a {@link TermsEnum} over the values, filtered by a {@link CompiledAutomaton}
     * The enum supports {@link TermsEnum#ord()}.
     */
    public TermsEnum intersect(CompiledAutomaton automaton) throws IOException {
        TermsEnum in = termsEnum();
        switch (automaton.type) {
            case NONE:
                return TermsEnum.EMPTY;
            case ALL:
                return in;
            case SINGLE:
                return new SingleTermsEnum(in, automaton.term);
            case NORMAL:
                return new AutomatonTermsEnum(in, automaton);
            default:
                // unreachable
                throw new RuntimeException("unhandled case");
        }
    }

}
