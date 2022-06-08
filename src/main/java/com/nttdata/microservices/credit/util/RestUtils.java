package com.nttdata.microservices.credit.util;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestUtils {

    public static ReactorClientHttpConnector getDefaultClientConnector() {
        return getClientConnector(5000, 5000, 5000);
    }

    public static ReactorClientHttpConnector getClientConnector(int connectionTimeout,
                                                                int readTimeout, int writeTimeout) {

        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        connectionTimeout)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(
                                readTimeout, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(
                                writeTimeout, TimeUnit.MILLISECONDS)));

        return new ReactorClientHttpConnector(httpClient);
    }

}
