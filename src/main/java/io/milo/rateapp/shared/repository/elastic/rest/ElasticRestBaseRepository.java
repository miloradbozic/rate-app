package io.milo.rateapp.shared.repository.elastic.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Repository
public class ElasticRestBaseRepository {

    @Value("${elasticsearch.hostname}")
    private String hostname;

    @Value("${elasticsearch.port}")
    private int port;

    public ElasticRestBaseRepository() {
    }

    protected ElasticRestResponse getRequest(String endpoint) throws IOException {
        String method = "GET";
        Response response = this.client().performRequest(method, endpoint, Collections.emptyMap());
        return restResponse(response);
    }

    protected ElasticRestResponse getRequest(String endpoint, String body) throws IOException {
        String method = "GET";
        HttpEntity entity = new NStringEntity(body, ContentType.APPLICATION_JSON);
        Response response = this.client().performRequest(method, endpoint, Collections.emptyMap(), entity);
        return restResponse(response);
    }

    protected ElasticRestResponse putRequest(String endpoint, String body) throws IOException{
        String method = "PUT";
        HttpEntity entity = new NStringEntity(body, ContentType.APPLICATION_JSON);
        Map params = Collections.emptyMap();
        Response response =  this.client().performRequest(method, endpoint, params, entity);
        return this.restResponse(response);
    }

    protected ElasticRestResponse postRequest(String endpoint, String body) throws IOException {
        String method = "POST";
        HttpEntity entity = new NStringEntity(body, ContentType.APPLICATION_JSON);
        Response response = this.client().performRequest(method, endpoint, Collections.emptyMap(), entity);
        return this.restResponse(response);
    }

    protected ElasticRestResponse deleteRequest(String endpoint) throws IOException {
        String method = "DELETE";
        Response response = this.client().performRequest(method, endpoint);
        return this.restResponse(response);
    }
    
    private RestClient client() {
        return RestClient.builder(
                new HttpHost(this.hostname, this.port, "http")).build();
    }

    private ElasticRestResponse restResponse(Response response) throws IOException {
        return new ElasticRestResponse(response);
    }

}
