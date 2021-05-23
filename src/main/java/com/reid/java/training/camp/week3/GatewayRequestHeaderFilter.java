package com.reid.java.training.camp.week3;

import com.reid.java.training.camp.week3.dispatch.filter.AbstractFilter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 添加网关请求头的过滤器
 */
public class GatewayRequestHeaderFilter extends AbstractFilter {

    @Override
    public void preFilter(FullHttpRequest fullHttpRequest) {
        fullHttpRequest.headers().add("x-gateway-sign", "request");
    }

    @Override
    public FullHttpResponse postFilter(FullHttpResponse fullHttpResponse) {
        fullHttpResponse.headers().add("x-gateway-sign", "response");
        return super.postFilter(fullHttpResponse);
    }
}
