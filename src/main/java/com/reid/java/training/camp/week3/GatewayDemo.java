package com.reid.java.training.camp.week3;

import com.reid.java.training.camp.week3.config.Config;
import com.reid.java.training.camp.week3.dispatch.router.RouterType;
import com.reid.java.training.camp.week3.server.GatewayServer;

import java.util.ArrayList;

/**
 * 监听9000端口的网关服务
 * 使用轮询的路由策略
 * 添加了一个自定义请求头和响应头的过滤器
 *    请求头添加 x-gateway-sign:request
 *    响应头添加 x-gateway-sign:response
 * 网关代理两个后端服务
 *    http://localhost:8801 返回字符串 hello, nio 8801
 *    http://localhost:8802 返回字符串 hello, nio 8802
 */
public class GatewayDemo {

    public static void main(String[] args) {
        Config config = Config.builder()
                .port(9000)
                .routerType(RouterType.POlLING)
                .filters(new ArrayList<>() {{
                    add("com.reid.java.training.camp.week3.GatewayRequestHeaderFilter");
                }})
                .proxyAddresses(new ArrayList<>() {{
                    add("http://localhost:8801");
                    add("http://localhost:8802");
                }})
                .build();
        GatewayServer gatewayServer = new GatewayServer(config);
        gatewayServer.start();
    }

}
