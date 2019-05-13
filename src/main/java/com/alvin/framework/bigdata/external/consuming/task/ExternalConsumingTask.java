package com.alvin.framework.bigdata.external.consuming.task;

/**
 * datetime 2019/5/13 15:38
 *
 * @author sin5
 */
public interface ExternalConsumingTask<T> {

    /**
     * start big data external consuming task
     *
     * @param initScrollCursor init scroll id cursor
     * @param scrollSize scroll size
     * @param batchConsumeSize size of obj for batch consuming
     */
    void start(String initScrollCursor, int scrollSize, int batchConsumeSize);
}
