package com.reid.java.training.camp.week2.response;

import com.reid.java.training.camp.week2.request.HttpRequest;

import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private HttpResponse() {}

    private HttpRequest httpRequest;

    private byte[] data = new byte[0];

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public byte[] getData() {
        return data;
    }

    public String getDataAsString() {
        return new String(data, StandardCharsets.UTF_8);
    }

    private void setHttpRequest(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    private void setData(byte[] data) {
        this.data = data;
    }

    public static class Builder {

        private HttpResponse httpResponse;

        public Builder() {
            httpResponse = new HttpResponse();
        }

        public Builder request(HttpRequest httpRequest) {
            httpResponse.setHttpRequest(httpRequest);
            return this;
        }

        public Builder data(byte[] data) {
            httpResponse.setData(data);
            return this;
        }

        public HttpResponse build() {
            return httpResponse;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
