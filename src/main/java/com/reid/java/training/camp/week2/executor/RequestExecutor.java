package com.reid.java.training.camp.week2.executor;

import com.reid.java.training.camp.week2.request.HttpRequest;
import com.reid.java.training.camp.week2.response.HttpResponse;

import java.io.IOException;

public interface RequestExecutor {

    public HttpResponse execute(HttpRequest httpRequest) throws IOException;

}
