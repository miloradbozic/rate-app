package io.milo.rateapp.repository.impl.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Primary
@Repository
public class AbstractRestRepository {

    protected RestResponse getRequest(String endpoint) throws IOException {
        String method = "GET";
        Response response = this.client().performRequest(method, endpoint, Collections.emptyMap());
        return restResponse(response);
    }

    protected RestResponse getRequest(String endpoint, String body) throws IOException {
        String method = "GET";
        HttpEntity entity = new NStringEntity(body, ContentType.APPLICATION_JSON);
        Response response = this.client().performRequest(method, endpoint, Collections.emptyMap(), entity);
        return restResponse(response);
    }

    protected RestResponse putRequest(String endpoint, String body) throws IOException{
        String method = "PUT";
        HttpEntity entity = new NStringEntity(body, ContentType.APPLICATION_JSON);
        Map params = Collections.emptyMap();
        Response response =  this.client().performRequest(method, endpoint, params, entity);
        return this.restResponse(response);
    }

    protected RestResponse postRequest(String endpoint, String body) throws IOException {
        String method = "POST";
        HttpEntity entity = new NStringEntity(body, ContentType.APPLICATION_JSON);
        Response response = this.client().performRequest(method, endpoint, Collections.emptyMap(), entity);
        return this.restResponse(response);
    }

    protected RestResponse deleteRequest(String endpoint) throws IOException {
        String method = "DELETE";
        Response response = this.client().performRequest(method, endpoint);
        return this.restResponse(response);
    }

    // @todo parametrize localhost from application.properties
    private RestClient client() {
        return RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();
    }

    private RestResponse restResponse(Response response) throws IOException {
        return new RestResponse(response);
    }

}
