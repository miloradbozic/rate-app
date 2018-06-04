package io.milo.rateapp;

import io.milo.rateapp.core.repository.IndexRepository;
import org.elasticsearch.client.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

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
            ; // skip in case already created
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
