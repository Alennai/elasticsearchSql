package org.apache.lucene.analysis.tokenattributes;

import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.BytesRef;

/**
 * Created by xusiao on 2018/5/8.
 */
public interface TermToBytesRefAttribute extends Attribute {

    /**
     * Retrieve this attribute's BytesRef. The bytes are updated from the current term.
     * The implementation may return a new instance or keep the previous one.
     * @return a BytesRef to be indexed (only stays valid until token stream gets incremented)
     */
    public BytesRef getBytesRef();
}
