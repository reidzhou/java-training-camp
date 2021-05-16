package com.reid.java.training.camp.week2.executor;

import com.reid.java.training.camp.week2.request.HttpRequest;
import com.reid.java.training.camp.week2.request.PostHttpRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostRequestExecutor extends AbstractRequestExecutor {

    public PostRequestExecutor(OkHttpClient okHttpClient) {
        super(okHttpClient);
    }

    @Override
    protected Request transferRequest(HttpRequest httpRequest) {
        if (!(httpRequest instanceof PostHttpRequest)) {
            throw new IllegalArgumentException("httpRequest is not a post request");
        }
        PostHttpRequest postHttpRequest = (PostHttpRequest) httpRequest;
        return new Request.Builder()
                .url(postHttpRequest.getUrl())
                .post(RequestBody.create(postHttpRequest.getBody()))
                .build();
    }
}
