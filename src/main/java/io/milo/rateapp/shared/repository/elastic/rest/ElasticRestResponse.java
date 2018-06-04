package io.milo.rateapp.shared.repository.elastic.rest;

import com.jayway.jsonpath.JsonPath;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

public class ElasticRestResponse {
    private String json;

    public ElasticRestResponse(Response response) throws IOException {
        this(EntityUtils.toString(response.getEntity()));
    }
    public ElasticRestResponse(String json) {
        this.json = json;
    }

    public <T> T extract(String pattern) {
        return JsonPath.read(this.json, pattern);
    }

    public String getBody() {
        return json;
    }
}
