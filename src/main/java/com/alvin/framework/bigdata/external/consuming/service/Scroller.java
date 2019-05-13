package com.alvin.framework.bigdata.external.consuming.service;

import com.alvin.framework.bigdata.external.consuming.model.ScrollResult;

/**
 * datetime 2019/5/13 15:42
 *
 * @author sin5
 */
public interface Scroller<T> {

    /**
     * scroll database
     *
     * @param scrollCursor idCursor or scrollId
     * @param scrollSize scroll size
     * @return scroll result
     */
    ScrollResult<T> scroll(String scrollCursor, int scrollSize);

    /**
     * check t if add to queue for consuming
     *
     * @param t t obj
     * @return true if to add
     */
    boolean addForConsuming(T t);

    /**
     * check t if stop scrolling now
     *
     * @param t t obj
     * @return true if stop scrolling now
     */
    boolean stopScrollingAt(T t);
}
