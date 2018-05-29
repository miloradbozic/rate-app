package io.milo.rateapp.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.milo.rateapp.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Primary
@Repository
public class AbstractElasticRepository {

    protected Response getRequest(String endpoint) throws IOException {
        String method = "GET";
        return this.client().performRequest(method, endpoint, Collections.emptyMap());
    }

    protected Response putRequest(String endpoint, HttpEntity entity) throws IOException{
        String method = "PUT";
        Map params = Collections.emptyMap();
        return this.client().performRequest(method, endpoint, params, entity);
    }

    protected Response postRequest(String endpoint, HttpEntity entity) throws IOException {
        String method = "POST";
        Map params = Collections.emptyMap();
        return this.client().performRequest(method, endpoint, params, entity);
    }

    // todo parametize localhost from application.properties
    private RestClient client() {
        return RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();
    }
}
