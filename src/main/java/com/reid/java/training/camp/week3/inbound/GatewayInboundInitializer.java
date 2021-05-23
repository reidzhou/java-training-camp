package com.reid.java.training.camp.week3.inbound;

import com.reid.java.training.camp.week3.config.Config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class GatewayInboundInitializer extends ChannelInitializer<SocketChannel> {

	private Config config;

	public GatewayInboundInitializer(Config config) {
		this.config = config;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
//		if (sslCtx != null) {
//			p.addLast(sslCtx.newHandler(ch.alloc()));
//		}
		p.addLast(new HttpServerCodec());
		//p.addLast(new HttpServerExpectContinueHandler());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		p.addLast(new GatewayHttpInboundHandler(config));
	}
}
