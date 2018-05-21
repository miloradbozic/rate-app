package io.milo.rateapp.scheduler;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import com.google.gson.Gson;
import io.milo.rateapp.client.UserClient;
import io.milo.rateapp.model.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("users.scheduledTasks.run")
public class ImportUsers {

    private static final Logger log = LoggerFactory.getLogger(ImportUsers.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void importSingle() {
        log.info("Importing user from uinames, the time now is {}", dateFormat.format(new Date()));

        UserClient userClient = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(UserClient.class))
                .logLevel(feign.Logger.Level.FULL)
                .target(UserClient.class, "https://uinames.com/api");

        User user = userClient.getSingle();
        // insert into elasticsearch
        //todo check if same exists
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();

        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        HttpEntity entity = new NStringEntity(userJson, ContentType.APPLICATION_JSON);
        String id = UUID.randomUUID().toString(); //TODO explore possiblities

        try {
            Response indexResponse = restClient.performRequest(
                    "PUT",
                    "/users/user/" + id,
                    Collections.<String, String>emptyMap(),
                    entity);
        } catch (Exception e) {
            ;
        }

    }

}
