package com.reid.java.training.camp.week2.request;

import com.reid.java.training.camp.week2.response.HttpResponse;

public class PostHttpRequest extends HttpRequest {

    private byte[] body = new byte[0];

    public byte[] getBody() {
        return body;
    }

    @Override
    public Method getMethod() {
        return Method.POST;
    }

    public static class Builder {

        private PostHttpRequest postHttpRequest;

        public Builder() {
            postHttpRequest = new PostHttpRequest();
        }

        public Builder protocol(String protocol) {
            postHttpRequest.protocol = protocol;
            return this;
        }

        public Builder host(String host) {
            postHttpRequest.host = host;
            return this;
        }

        public Builder port(int port) {
            postHttpRequest.port = port;
            return this;
        }

        public Builder path(String path) {
            postHttpRequest.path = path;
            return this;
        }

        public Builder body(byte[] body) {
            postHttpRequest.body = body;
            return this;
        }

        public PostHttpRequest build() {
            return postHttpRequest;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

}
