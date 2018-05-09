package org.apache.lucene.util.automaton;

/**
 * Created by xusiao on 2018/5/8.
 */
public class ByteRunAutomaton extends RunAutomaton {

    /** Converts incoming automaton to byte-based (UTF32ToUTF8) first */
    public ByteRunAutomaton(Automaton a) {
        this(a, false, Operations.DEFAULT_MAX_DETERMINIZED_STATES);
    }

    /** expert: if isBinary is true, the input is already byte-based */
    public ByteRunAutomaton(Automaton a, boolean isBinary, int maxDeterminizedStates) {
        super(isBinary ? a : new UTF32ToUTF8().convert(a), 256, maxDeterminizedStates);
    }

    /**
     * Returns true if the given byte array is accepted by this automaton
     */
    public boolean run(byte[] s, int offset, int length) {
        int p = 0;
        int l = offset + length;
        for (int i = offset; i < l; i++) {
            p = step(p, s[i] & 0xFF);
            if (p == -1) return false;
        }
        return accept[p];
    }
}

