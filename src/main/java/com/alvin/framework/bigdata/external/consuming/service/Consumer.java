package com.alvin.framework.bigdata.external.consuming.service;

import java.util.List;

/**
 * datetime 2019/5/13 16:05
 *
 * @author sin5
 */
public interface Consumer<T> {

    /**
     * batch consume
     * @param list list of t
     */
    void batchConsume(List<T> list);

    /**
     * on consuming finished. do some statistical work
     */
    void onEnd();
}
