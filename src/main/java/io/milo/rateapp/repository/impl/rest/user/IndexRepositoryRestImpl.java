package io.milo.rateapp.repository.impl.rest.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.milo.rateapp.model.User;
import io.milo.rateapp.repository.impl.rest.AbstractRestRepository;
import io.milo.rateapp.repository.impl.rest.RestResponse;
import io.milo.rateapp.repository.user.IndexRepository;
import io.milo.rateapp.repository.user.UserRepository;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Primary
@Repository
public class IndexRepositoryRestImpl extends AbstractRestRepository implements IndexRepository {

    private static final String USER_ENDPOINT = "users";

    @Value("classpath:mapping.json")
    private Resource res;

    @Override
    public void create() throws IOException {
        String mapping = new String(Files.readAllBytes(Paths.get(res.getURI())));
        this.putRequest(USER_ENDPOINT, mapping);
    }

    @Override
    public void delete() throws IOException {
        this.deleteRequest(USER_ENDPOINT);
    }
}
