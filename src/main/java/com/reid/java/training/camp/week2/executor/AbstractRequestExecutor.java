package com.reid.java.training.camp.week2.executor;

import com.reid.java.training.camp.week2.request.HttpRequest;
import com.reid.java.training.camp.week2.response.HttpResponse;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public abstract class AbstractRequestExecutor implements RequestExecutor {

    private OkHttpClient okHttpClient;

    protected AbstractRequestExecutor(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @Override
    public HttpResponse execute(HttpRequest httpRequest) throws IOException {
        Request request = transferRequest(httpRequest);
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        return transferResponse(httpRequest, response);
    }

    protected abstract Request transferRequest(HttpRequest httpRequest);

    private HttpResponse transferResponse(HttpRequest httpRequest, Response response) throws IOException {
        HttpResponse.Builder builder = HttpResponse.builder().request(httpRequest);

        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            builder.data(responseBody.byteStream().readAllBytes());
            responseBody.source().close();
        }

        return builder.build();
    }
}
