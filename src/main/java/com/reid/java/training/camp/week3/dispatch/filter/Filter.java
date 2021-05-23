package com.reid.java.training.camp.week3.dispatch.filter;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public interface Filter {

    public void preFilter(FullHttpRequest fullHttpRequest);

    public FullHttpResponse postFilter(FullHttpResponse fullHttpResponse);

}
