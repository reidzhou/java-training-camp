package com.reid.java.training.camp.week3.dispatch.action;

import com.reid.java.training.camp.week3.dispatch.filter.Filter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Get请求转发对象
 */
public class GetRedirectAction implements RedirectAction {

    private static final Logger logger = LoggerFactory.getLogger(RedirectAction.class);

    private String url;
    private FullHttpRequest fullHttpRequest;
    private CloseableHttpAsyncClient httpClient;
    private ChannelHandlerContext ctx;

    private Filter filter;

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public GetRedirectAction(String url, FullHttpRequest fullHttpRequest, CloseableHttpAsyncClient httpClient, ChannelHandlerContext ctx) {
        this.url = url;
        this.fullHttpRequest = fullHttpRequest;
        this.httpClient = httpClient;
        this.ctx = ctx;
    }

    private void doAction(FullHttpRequest fullHttpRequest) {
        // 请求拦截
        filter.preFilter(fullHttpRequest);

        final HttpGet httpGet = new HttpGet(url);
        fullHttpRequest.headers().entries().forEach(stringStringEntry -> {
            System.out.println("[request header] - " + stringStringEntry.getKey() + ": " + stringStringEntry.getValue());
            httpGet.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
        });
        httpClient.execute(httpGet, callback(httpGet));
    }

    private FutureCallback<HttpResponse> callback(HttpGet httpGet) {
        return new FutureCallback<>() {
            @Override
            public void completed(final HttpResponse httpResponse) {
                onResponse(httpResponse);
            }

            @Override
            public void failed(final Exception ex) {
                httpGet.abort();
            }

            @Override
            public void cancelled() {
                httpGet.abort();
            }
        };
    }

    private FullHttpResponse buildFullHttpResponse(HttpResponse httpResponse) {
        try {
            byte[] body = EntityUtils.toByteArray(httpResponse.getEntity());
            final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK, Unpooled.wrappedBuffer(body));

            Stream.of(httpResponse.getAllHeaders()).forEach(header -> {
                response.headers().add(header.getName(), header.getValue());
            });
            return response;
        } catch (Exception e) {
            logger.error("something going wrong when invoking request callback", e);
        }
        return new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
    }

    private void onResponse(HttpResponse httpResponse) {
        FullHttpResponse fullHttpResponse = buildFullHttpResponse(httpResponse);

        // 响应拦截
        fullHttpResponse = filter.postFilter(fullHttpResponse);

        fullHttpResponse.headers().entries().forEach(stringStringEntry -> {
            System.out.println("[response header] - " + stringStringEntry.getKey() + ": " + stringStringEntry.getValue());
        });

        this.ctx.write(fullHttpResponse);
        this.ctx.flush();
    }

    @Override
    public void run() {
        doAction(fullHttpRequest);
    }
}
