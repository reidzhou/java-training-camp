package com.reid.java.training.camp.week3.dispatch.filter;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 过滤器的代理对象构造工厂
 * 基于动态代理实现
 * 支持多重代理
 */
public class FilterProxyFactory {

    public Filter createProxy(List<String> filters) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Filter proxy = new AbstractFilter() {};
        for (String filterName : filters) {
            Filter filter = createObject(filterName);
            proxy = newProxy(proxy, filter);
        }

        return proxy;
    }

    private Filter newProxy(Filter target, Filter overlapFilter) {
        Class<?>[] interfaces = target.getClass().getInterfaces();
        if (interfaces.length == 0) {
            interfaces = target.getClass().getSuperclass().getInterfaces();
        }
        HandlerProxyInvocationHandler handler = new HandlerProxyInvocationHandler(target, overlapFilter);
        return (Filter) Proxy.newProxyInstance(target.getClass().getClassLoader(), interfaces, handler);
    }

    private <T> T createObject(String name) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(name);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return (T) constructor.newInstance();
    }

    public static class HandlerProxyInvocationHandler implements InvocationHandler {

        private Filter action;
        private Filter filter;

        public HandlerProxyInvocationHandler(Filter action, Filter filter) {
            this.action = action;
            this.filter = filter;
        }

        @Override
        public Object invoke(Object target, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Object result = null;
            if ("preFilter".equals(methodName)) {
                FullHttpRequest fullHttpRequest = (FullHttpRequest) args[0];
                filter.preFilter(fullHttpRequest);
            }

            result = method.invoke(action, args);

            if ("postFilter".equals(methodName)) {
                FullHttpResponse fullHttpResponse = (FullHttpResponse) result;
                fullHttpResponse = filter.postFilter(fullHttpResponse);
                return fullHttpResponse;
            }
            return result;
        }
    }
}
