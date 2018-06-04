package io.milo.rateapp.core.repository.implementation;

import com.google.gson.*;
import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.repository.UserRepository;
import io.milo.rateapp.shared.repository.elastic.rest.ElasticRestBaseRepository;
import io.milo.rateapp.shared.repository.elastic.rest.ElasticRestResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Primary
@Repository
public class UserRepositoryRest extends ElasticRestBaseRepository implements UserRepository {

    private static final String USER_ENDPOINT = "users/user/";

    @Override
    public User create(User user) throws IOException {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        final String endpoint = USER_ENDPOINT + user.getId();
        final String body = this.userToJson(user);
        this.putRequest(endpoint, body);
        return user;
    }

    @Override
    public User getById(String id) throws IOException{
        final String endpoint = USER_ENDPOINT + id;
        ElasticRestResponse response = this.getRequest(endpoint);
        return this.userFromJson(response.getBody());
    }

    private String userToJson(User user) {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(user);
        jsonElement.getAsJsonObject().add("votes", new JsonArray());
        return gson.toJson(jsonElement);
    }

    private User userFromJson(String entity) {
        Gson gson = new Gson();
        JsonObject jsonElement = gson.fromJson(entity, JsonObject.class);
        String id = jsonElement.get("_id").getAsString();
        String source = jsonElement.get("_source").toString();
        return gson.fromJson(source, User.class).setId(id);
    }
}
