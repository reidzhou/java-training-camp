package com.reid.java.training.camp.week2.executor;

import com.reid.java.training.camp.week2.request.HttpRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GetRequestExecutor extends AbstractRequestExecutor {

    public GetRequestExecutor(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    protected Request transferRequest(HttpRequest httpRequest) {
        return new Request.Builder()
                .url(httpRequest.getUrl())
                .get()
                .build();
    }
}
