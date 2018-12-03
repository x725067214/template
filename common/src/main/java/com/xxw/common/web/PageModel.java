package com.xxw.common.web;

import java.util.List;

/**
 * @author xxw
 * @date 2018/12/3
 */
public class PageModel {

    private List list;

    private long total;

    public PageModel(List list, long total) {
        this.list = list;
        this.total = total;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
