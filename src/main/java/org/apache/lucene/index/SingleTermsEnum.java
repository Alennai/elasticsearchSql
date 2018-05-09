package org.apache.lucene.index;

import org.apache.lucene.util.BytesRef;

/**
 * Created by xusiao on 2018/5/8.
 */
public final class SingleTermsEnum  extends FilteredTermsEnum {
    private final BytesRef singleRef;

    /**
     * Creates a new <code>SingleTermsEnum</code>.
     * <p>
     * After calling the constructor the enumeration is already pointing to the term,
     * if it exists.
     */
    public SingleTermsEnum(TermsEnum tenum, BytesRef termText) {
        super(tenum);
        singleRef = termText;
        setInitialSeekTerm(termText);
    }

    @Override
    protected AcceptStatus accept(BytesRef term) {
        return term.equals(singleRef) ? AcceptStatus.YES : AcceptStatus.END;
    }

}
