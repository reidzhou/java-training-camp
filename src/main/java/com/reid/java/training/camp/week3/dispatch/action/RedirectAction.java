package com.reid.java.training.camp.week3.dispatch.action;

import com.reid.java.training.camp.week3.dispatch.filter.Filter;

/**
 * 请求转发动作接口
 */
public interface RedirectAction extends Runnable {

    public void setFilter(Filter filter);
}
