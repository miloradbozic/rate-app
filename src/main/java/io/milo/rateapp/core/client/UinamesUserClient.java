package io.milo.rateapp.core.client;

import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.milo.rateapp.core.model.User;

import java.util.List;

public interface UinamesUserClient {

    @RequestLine("GET /")
    User getSingle();

    @RequestLine("GET /?region={region}")
    User getSingleForRegion(@Param(value = "region") String region);

    @RequestLine("GET /?amount={amount}")
    List<User> getMultiple(@Param(value = "amount") Integer amount);

    static UinamesUserClient getClient() {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(UinamesUserClient.class))
                .logLevel(feign.Logger.Level.FULL)
                .target(UinamesUserClient.class, "https://uinames.com/api");
    }

}