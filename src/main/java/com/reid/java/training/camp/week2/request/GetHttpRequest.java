package com.reid.java.training.camp.week2.request;

public class GetHttpRequest extends HttpRequest {

    @Override
    public Method getMethod() {
        return Method.GET;
    }

    public static class Builder {

        private GetHttpRequest getHttpRequest;

        public Builder() {
            getHttpRequest = new GetHttpRequest();
        }

        public Builder protocol(String protocol) {
            getHttpRequest.protocol = protocol;
            return this;
        }

        public Builder host(String host) {
            getHttpRequest.host = host;
            return this;
        }

        public Builder port(int port) {
            getHttpRequest.port = port;
            return this;
        }

        public Builder path(String path) {
            getHttpRequest.path = path;
            return this;
        }

        public GetHttpRequest build() {
            return getHttpRequest;
        }

    }

    public static Builder builder() {
        return new Builder();
    }
}
