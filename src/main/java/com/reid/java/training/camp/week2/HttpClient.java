package com.reid.java.training.camp.week2;

import com.reid.java.training.camp.week2.executor.GetRequestExecutor;
import com.reid.java.training.camp.week2.executor.PostRequestExecutor;
import com.reid.java.training.camp.week2.executor.RequestExecutor;
import com.reid.java.training.camp.week2.request.GetHttpRequest;
import com.reid.java.training.camp.week2.request.HttpRequest;
import com.reid.java.training.camp.week2.response.HttpResponse;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    private final OkHttpClient okHttpClient;
    private final Map<HttpRequest.Method, RequestExecutor> requestExecutorMap = new HashMap<>();

    {
        okHttpClient = new OkHttpClient().newBuilder().build();
        requestExecutorMap.put(HttpRequest.Method.GET, new GetRequestExecutor(okHttpClient));
        requestExecutorMap.put(HttpRequest.Method.POST, new PostRequestExecutor(okHttpClient));
    }

    public String execute(HttpRequest httpRequest) throws IOException {
        RequestExecutor executor = requestExecutorMap.get(httpRequest.getMethod());
        if (executor == null) {
            throw new IllegalArgumentException("unknown request method");
        }
        HttpResponse response = executor.execute(httpRequest);
        return response.getDataAsString();
    }

//    public <T> T execute(HttpRequest httpRequest, Class<T> clazz) {
//
//    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        String responseStr = client.execute(GetHttpRequest.builder().host("localhost").port(8801).build());
        System.out.println(responseStr);
    }
}
