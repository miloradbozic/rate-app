package io.milo.rateapp.core.repository.implementation;

import io.milo.rateapp.core.repository.IndexRepository;
import io.milo.rateapp.shared.repository.elastic.rest.ElasticRestBaseRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Primary
@Repository
public class IndexRepositoryRest extends ElasticRestBaseRepository implements IndexRepository {

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
