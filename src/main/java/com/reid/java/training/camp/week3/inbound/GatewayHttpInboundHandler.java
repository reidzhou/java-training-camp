package com.reid.java.training.camp.week3.inbound;

import com.reid.java.training.camp.week3.config.Config;
import com.reid.java.training.camp.week3.dispatch.GatewayRequestDispatcher;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GatewayHttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(GatewayHttpInboundHandler.class);

    private GatewayRequestDispatcher gatewayRequestDispatcher;

    public GatewayHttpInboundHandler(Config config) {
        init(config);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            gatewayRequestDispatcher.dispatch(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void init(Config config) {
        this.gatewayRequestDispatcher = new GatewayRequestDispatcher(config);
    }

}
