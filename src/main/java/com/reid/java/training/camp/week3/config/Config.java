package com.reid.java.training.camp.week3.config;


import com.reid.java.training.camp.week3.dispatch.router.RouterType;

import java.util.List;

public class Config {

    private Config() {}

    /**
     * 端口
     */
    private int port;

    /**
     * 路由类型
     */
    private RouterType routerType;

    /**
     * 过滤器的全类名列表
     */
    private List<String> filters;

    /**
     * 网关代理的远程地址列表
     */
    private List<String> proxyAddresses;

    public int getPort() {
        return port;
    }

    public List<String> getFilters() {
        return filters;
    }

    public List<String> getProxyAddresses() {
        return proxyAddresses;
    }

    public RouterType getRouterType() {
        return routerType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Config config;

        public Builder() {
            this.config = new Config();
        }

        public Builder port(int port) {
            this.config.port = port;
            return this;
        }

        public Builder filters(List<String> filters) {
            this.config.filters = filters;
            return this;
        }

        public Builder routerType(RouterType routerType) {
            this.config.routerType = routerType;
            return this;
        }

        public Builder proxyAddresses(List<String> proxyAddresses) {
            this.config.proxyAddresses = proxyAddresses;
            return this;
        }

        public Config build() {
            return this.config;
        }
    }
}
