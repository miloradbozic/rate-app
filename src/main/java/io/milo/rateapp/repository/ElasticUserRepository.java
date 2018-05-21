package io.milo.rateapp.repository;

import com.google.gson.Gson;
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
import java.util.UUID;

@Primary
@Repository
public class ElasticUserRepository implements UserRepository{

    @Override
    public void create(User user) throws IOException {
        String id = UUID.randomUUID().toString(); // @todo explore options

        this.client().performRequest(
                "PUT",
                "users/user/" + id,
                Collections.emptyMap(),
                this.getEntity(user));
    }

    @Override
    public User getById(String id) throws IOException{
        Response response = this.client().performRequest("GET",
                "users/user/" + id,
                Collections.emptyMap());
        return this.getUser(response.getEntity());
    }

    private RestClient client() {
        return RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();
    }

    private HttpEntity getEntity(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        return new NStringEntity(userJson, ContentType.APPLICATION_JSON);
    }

    private User getUser(HttpEntity entity) throws IOException{
        Gson gson = new Gson();
        JsonObject jsonElement = gson.fromJson(EntityUtils.toString(entity), JsonObject.class);
        String id = jsonElement.get("_id").getAsString();
        String source = jsonElement.get("_source").toString();
        return gson.fromJson(source, User.class).setId(id);
    }
}
