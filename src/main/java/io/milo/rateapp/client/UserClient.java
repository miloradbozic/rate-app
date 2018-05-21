package io.milo.rateapp.client;

import feign.Param;
import feign.RequestLine;
import io.milo.rateapp.model.User;

import java.util.List;

public interface UserClient {

    @RequestLine("GET /?region=england")
    User getSingle();

    @RequestLine("GET /?region={region}")
    User getSingleForRegion(@Param(value = "region") String region);

    @RequestLine("GET /?amount={amount}")
    List<User> getMultiple(@Param(value = "amount") Integer amount);

}