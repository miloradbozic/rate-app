package io.milo.rateapp.repository.impl.rest.user;

import com.google.gson.*;
import io.milo.rateapp.model.User;
import io.milo.rateapp.repository.impl.rest.AbstractRestRepository;
import io.milo.rateapp.repository.impl.rest.RestResponse;
import io.milo.rateapp.repository.user.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import static org.elasticsearch.common.xcontent.XContentFactory.*;

import java.io.IOException;
import java.util.*;

@Primary
@Repository
public class UserRepositoryRestImpl extends AbstractRestRepository implements UserRepository {

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
        RestResponse response = this.getRequest(endpoint);
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
