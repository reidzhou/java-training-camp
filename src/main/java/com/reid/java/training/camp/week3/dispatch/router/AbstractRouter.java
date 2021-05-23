package com.reid.java.training.camp.week3.dispatch.router;

import java.util.Collections;
import java.util.List;

/**
 * 路由器抽象类
 * 定义基本数据结构
 */
public abstract class AbstractRouter implements Router {

    private final List<String> addresses;

    public AbstractRouter(List<String> addresses) {
        this.addresses = Collections.unmodifiableList(addresses);
    }

    @Override
    public String route() {
        String address = doRoute();
        System.out.println("[Route] - redirect address: " + address);
        return address;
    }

    protected abstract String doRoute();

    public List<String> getAddresses() {
        return addresses;
    }
}
