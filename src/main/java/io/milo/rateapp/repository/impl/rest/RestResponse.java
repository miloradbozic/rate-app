package io.milo.rateapp.repository.impl.rest;

import com.jayway.jsonpath.JsonPath;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;

import java.io.IOException;

public class RestResponse {
    private String json;

    public RestResponse(Response response) throws IOException {
        this(EntityUtils.toString(response.getEntity()));
    }
    public RestResponse(String json) {
        this.json = json;
    }

    public <T> T extract(String pattern) {
        return JsonPath.read(this.json, pattern);
    }

    public String getBody() {
        return json;
    }
}
