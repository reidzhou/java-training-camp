package com.reid.java.training.camp.week2.request;

public abstract class HttpRequest {

    protected String protocol = "http";

    protected String host = "localhost";

    protected int port = 8080;

    protected String path = "";

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUrl() {
        return protocol + "://" + host + ":" + port + "/" + path;
    }

    public abstract HttpRequest.Method getMethod();

    public enum Method {
        GET,
        POST;
    }
}
