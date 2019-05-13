package com.alvin.framework.bigdata.external.consuming.model;

import java.util.List;

/**
 * datetime 2019/5/13 15:44
 *
 * @author sin5
 */
public class ScrollResult<T> {

    /**
     * next scroll id cursor
     */
    private String idCursor;
    /**
     * data
     */
    private List<T> data;

    public ScrollResult(String idCursor, List<T> data) {
        this.idCursor = idCursor;
        this.data = data;
    }

    public String getIdCursor() {
        return idCursor;
    }

    public List<T> getData() {
        return data;
    }

}
