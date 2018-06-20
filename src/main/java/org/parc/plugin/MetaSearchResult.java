package org.parc.plugin;

/**
 * Created by xusiao on 2018/6/20.
 */
public class MetaSearchResult  {
    private long tookImMilli;
    private int totalNumOfShards;
    private int successfulShards;
    private int failedShards;
    private boolean isTimedOut;

    public MetaSearchResult() {
        totalNumOfShards = 0;
        failedShards = 0;
        successfulShards = 0;
        isTimedOut = false;
    }

    public int getTotalNumOfShards() {
        return totalNumOfShards;
    }

    public int getSuccessfulShards() {
        return successfulShards;
    }

    public int getFailedShards() {
        return failedShards;
    }

    public boolean isTimedOut() {
        return isTimedOut;
    }

    public long getTookImMilli() {
        return tookImMilli;
    }

    public void setTookImMilli(long tookImMilli) {
        this.tookImMilli = tookImMilli;
    }

    public void addFailedShards(int shards){
        this.failedShards+=shards;
    }

    public void addSuccessfulShards(int shards){
        this.successfulShards+=shards;
    }

    public void addTotalNumOfShards(int shards){
        this.totalNumOfShards+=shards;
    }

    public void updateTimeOut(boolean isTimedOut){
        this.isTimedOut = this.isTimedOut || isTimedOut;
    }

}
