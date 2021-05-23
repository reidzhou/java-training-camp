package com.reid.java.training.camp.week3.dispatch.filter;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

public abstract class AbstractFilter implements Filter {

    @Override
    public void preFilter(FullHttpRequest fullHttpRequest) {
        // do nothing
    }

    @Override
    public FullHttpResponse postFilter(FullHttpResponse fullHttpResponse) {
        // do nothing
        return fullHttpResponse;
    }
}
