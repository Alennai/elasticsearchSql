package org.apache.lucene.util.automaton;

/**
 * Created by xusiao on 2018/5/8.
 */
public class Transition  {

    /** Sole constructor. */
    public Transition() {
    }

    /** Source state. */
    public int source;

    /** Destination state. */
    public int dest;

    /** Minimum accepted label (inclusive). */
    public int min;

    /** Maximum accepted label (inclusive). */
    public int max;

    /** Remembers where we are in the iteration; init to -1 to provoke
     *  exception if nextTransition is called without first initTransition. */
    int transitionUpto = -1;

    @Override
    public String toString() {
        return source + " --> " + dest + " " + (char) min + "-" + (char) max;
    }
}

