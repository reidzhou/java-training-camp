package com.reid.java.training.camp.week3.dispatch;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface Handler {

    public void handler(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx);
}
