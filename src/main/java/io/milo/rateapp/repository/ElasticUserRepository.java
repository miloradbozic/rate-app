package io.milo.rateapp.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.milo.rateapp.model.User;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Primary
@Repository
public class ElasticUserRepository extends AbstractElasticRepository implements UserRepository {

    private static final String USER_ENDPOINT = "users/user/";

    @Override
    public void create(User user) throws IOException {
        String id = UUID.randomUUID().toString(); // @todo explore options
        final String endpoint = USER_ENDPOINT + id;
        final HttpEntity entity = this.userToJson(user);
        this.putRequest(endpoint, entity);
    }

    @Override
    public User getById(String id) throws IOException{
        final String endpoint = USER_ENDPOINT + id;
        Response response = this.getRequest(endpoint);
        return this.userFromJson(response.getEntity());
    }

    // @todo: check if we always need to add 'votes'
    private HttpEntity userToJson(User user) {
        Gson gson = new Gson();
        //String userJson = gson.toJson(user);

        JsonElement jsonElement = gson.toJsonTree(user);
        jsonElement.getAsJsonObject().add("votes", new JsonArray());
        String userJson = gson.toJson(jsonElement);

        return new NStringEntity(userJson, ContentType.APPLICATION_JSON);
    }

    private User userFromJson(HttpEntity entity) throws IOException {
        Gson gson = new Gson();
        JsonObject jsonElement = gson.fromJson(EntityUtils.toString(entity), JsonObject.class);
        String id = jsonElement.get("_id").getAsString();
        String source = jsonElement.get("_source").toString();
        return gson.fromJson(source, User.class).setId(id);
    }
}
