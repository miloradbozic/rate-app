package io.milo.rateapp;

import io.milo.rateapp.repository.user.IndexRepository;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseException;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.core.io.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class RateAppApplication {

    @Autowired
    IndexRepository indexRepository;

    public static void main(String[] args) {
        SpringApplication.run(RateAppApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeElasticsearch() {
        try {
            indexRepository.create();
        } catch (ResponseException e2) {
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
