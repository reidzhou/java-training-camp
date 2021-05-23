package com.reid.java.training.camp.week3.dispatch.router;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询路由器
 */
public class PollingRouter extends AbstractRouter {

    public PollingRouter(List<String> addresses) {
        super(addresses);
    }

    private final AtomicInteger lastAddressIndex = new AtomicInteger(0);

    @Override
    public String doRoute() {
        List<String> addresses = getAddresses();
        return addresses.get(lastAddressIndex.getAndIncrement() % addresses.size());
    }
}
