package com.reid.java.training.camp.week3.dispatch;


import com.reid.java.training.camp.week3.config.Config;
import com.reid.java.training.camp.week3.dispatch.action.GetRedirectAction;
import com.reid.java.training.camp.week3.dispatch.action.RedirectAction;
import com.reid.java.training.camp.week3.dispatch.filter.Filter;
import com.reid.java.training.camp.week3.dispatch.filter.FilterProxyFactory;
import com.reid.java.training.camp.week3.dispatch.router.Router;
import com.reid.java.training.camp.week3.dispatch.router.RouterFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 网关请求转发器
 */
public class GatewayRequestDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(GatewayRequestDispatcher.class);

    private final Config config;
    private final Router router;
    private final ExecutorService executorService;
    private final CloseableHttpAsyncClient httpClient;

    public GatewayRequestDispatcher(Config config) {
        this.config = config;

        this.router = RouterFactory.router(config.getRouterType(), config.getProxyAddresses());

        int core = Runtime.getRuntime().availableProcessors();
        this.executorService = new ThreadPoolExecutor(
                core,
                core * 2,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1000),
                new NamedThreadFactory("gateway-redirect-action"),
                new ThreadPoolExecutor.AbortPolicy()
        );

        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(core)
                .setRcvBufSize(32 * 1024)
                .build();
        httpClient = HttpAsyncClients.custom().setMaxConnTotal(40)
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();
        httpClient.start();
    }

    public void dispatch(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        // 获取实际的远程地址
        String fullAddress = this.router.route() + fullHttpRequest.uri();

        this.executorService.submit(buildAction(fullAddress, fullHttpRequest, ctx));
    }

    private RedirectAction buildAction(String address, FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        RedirectAction redirectAction = new GetRedirectAction(address, fullHttpRequest, this.httpClient, ctx);

        List<String> filters = config.getFilters();
        if (filters != null && !filters.isEmpty()) {
            FilterProxyFactory filterProxyFactory = new FilterProxyFactory();
            try {
                Filter filter = filterProxyFactory.createProxy(filters);
                redirectAction.setFilter(filter);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                logger.error("could not create redirect action proxy", e);
            }
        }
        return redirectAction;
    }

}
