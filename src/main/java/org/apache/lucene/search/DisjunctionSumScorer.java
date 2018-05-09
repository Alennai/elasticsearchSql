package org.apache.lucene.search;

import java.io.IOException;
import java.util.List;

/**
 * Created by xusiao on 2018/5/8.
 */
final class DisjunctionSumScorer  extends DisjunctionScorer {
    private final float[] coord;

    /** Construct a <code>DisjunctionScorer</code>.
     * @param weight The weight to be used.
     * @param subScorers Array of at least two subscorers.
     * @param coord Table of coordination factors
     */
    DisjunctionSumScorer(Weight weight, List<Scorer> subScorers, float[] coord, boolean needsScores) {
        super(weight, subScorers, needsScores);
        this.coord = coord;
    }

    @Override
    protected float score(DisiWrapper topList) throws IOException {
        double score = 0;
        int freq = 0;
        for (DisiWrapper w = topList; w != null; w = w.next) {
            score += w.scorer.score();
            freq += 1;
        }
        return (float)score * coord[freq];
    }
}

