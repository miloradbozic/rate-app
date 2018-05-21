package io.milo.rateapp;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
public class RateAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RateAppApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeElasticsearch() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();

        try {
            Response response = restClient.performRequest("GET", "/",
                    Collections.singletonMap("pretty", "true"));
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            ; //todo log error
        }

        //check if Index exists

        try {
            Response response = restClient.performRequest("GET", "/users");
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (org.elasticsearch.client.ResponseException e) {
            //TODO handle:
            // {"error":{"root_cause":[{"type":"index_not_found_exception","reason":"no such index","resource.type":"index_or_alias","resource.id":"user","index_uuid":"_na_","index":"user"}],"type":"index_not_found_exception","reason":"no such index","resource.type":"index_or_alias","resource.id":"user","index_uuid":"_na_","index":"user"},"status":404}
            //System.out.println(e.toString());

            //create an index
            try {
                Response response = restClient.performRequest("PUT", "/users");

            } catch (Exception e2) {
                ; //TODO handle
            }

            ; //todo log error
        } catch (Exception e) {
            ;
        }
    }
}
