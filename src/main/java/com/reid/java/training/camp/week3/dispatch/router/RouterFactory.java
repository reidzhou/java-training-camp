package com.reid.java.training.camp.week3.dispatch.router;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由器构造工厂
 */
public class RouterFactory {

    private static final Map<RouterType, Class<? extends Router>> ROUTER_TYPE_CLASS_MAP = new HashMap<>();

    static {
        ROUTER_TYPE_CLASS_MAP.put(RouterType.POlLING, PollingRouter.class);
    }

    public static Router router(RouterType type, List<String> addresses) {
        Class<? extends Router> clazz =  ROUTER_TYPE_CLASS_MAP.get(type);
        if (clazz == null) {
            throw new IllegalArgumentException("unknown router type[" + type.name() + "]");
        }
        try {
            Constructor<? extends Router> constructor = clazz.getDeclaredConstructor(List.class);
            return constructor.newInstance(addresses);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("could not initialize router for type[" + type.name() + "]", e);
        }
    }
}
