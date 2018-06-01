package io.milo.rateapp.repository.impl.rest.user;

import io.milo.rateapp.model.User;
import io.milo.rateapp.repository.impl.rest.AbstractRestRepository;
import io.milo.rateapp.repository.impl.rest.RestResponse;
import io.milo.rateapp.repository.user.ReportingRepository;
import io.milo.rateapp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Primary
@Repository
public class ReportingRepositoryRestImpl extends AbstractRestRepository implements ReportingRepository {

    private static final String USER_SEARCH_ENDPOINT = "users/user/_search";

    @Autowired UserRepository userRepository;

    @Override
    public User getUserWithMostVotes() throws IOException {

        //@todo: size 0
        String body = jsonBuilder()
                .startObject()
                    .startObject("aggregations")
                        .startObject("votes")
                            .startObject("nested")
                                .field("path", "votes")
                            .endObject()
                            .startObject("aggs")
                                .startObject("users")
                                    .startObject("terms")
                                        .field("field", "votes.id.keyword")
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject()
                .string();

        RestResponse response = this.getRequest(USER_SEARCH_ENDPOINT, body);
        String leadUserId = response.extract("$.aggregations.votes.users.buckets[0].key");
        return this.userRepository.getById(leadUserId);
    }

    @Override
    public String getRegionWithMostVotes() throws IOException {

        String body = jsonBuilder()
                .startObject()
                    .startObject("aggregations")
                        .startObject("votes")
                            .startObject("nested")
                                .field("path", "votes")
                            .endObject()
                            .startObject("aggs")
                                .startObject("regions")
                                    .startObject("terms")
                                        .field("field", "votes.region.keyword")
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject()
                .string();

        RestResponse response = this.getRequest(USER_SEARCH_ENDPOINT, body);
        String region = response.extract("$.aggregations.votes.regions.buckets[0].key");
        return region;
    }

    @Override
    public String getRegionWhichVotedMost() throws IOException {

        String body = jsonBuilder()
                .startObject()
                    .startObject("aggregations")
                        .startObject("regions")
                            .startObject("terms")
                                .field("field", "region.keyword")
                            .endObject()
                            .startObject("aggs")
                                .startObject("count")
                                    .startObject("sum")
                                        .startObject("script")
                                            .field("inline", "params._source.votes.size()")
                                        .endObject()
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject()
                .string();

        RestResponse response = this.getRequest(USER_SEARCH_ENDPOINT, body);
        List timesVotes = response.extract("$.aggregations.regions.buckets[*].count.value");
        int maxVoted = timesVotes.stream().mapToInt(e -> ((Double)e).intValue()).max().getAsInt();
        List maxVotedRegions = response.extract(
                "$.aggregations.regions.buckets[?(@.count.value == " + maxVoted + " )].key"
        );
        return (String)maxVotedRegions.get(0);
    }

    @Override
    public Map<String, User> getLeadUsersForAllRegions() throws IOException {
        final String endpoint = USER_SEARCH_ENDPOINT;

        String body = jsonBuilder()
                .startObject()
                    .startObject("aggregations")
                        .startObject("votes")
                            .startObject("nested")
                                .field("path", "votes")
                            .endObject()
                            .startObject("aggs")
                                .startObject("regions")
                                    .startObject("terms")
                                        .field("field", "votes.region.keyword")
                                    .endObject()
                                    .startObject("aggs")
                                        .startObject("users")
                                            .startObject("terms")
                                                .field("field", "votes.id.keyword")
                                            .endObject()
                                        .endObject()
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject()
                .string();

        RestResponse response = this.getRequest(endpoint, body);
        List<String> regions = response.extract("$.aggregations.votes.regions.buckets[*].key");
        List<String> leadIds = response.extract("$.aggregations.votes.regions.buckets[*].users.buckets[0].key");

        Map<String, User> result = IntStream
                .range(0, regions.size())
                .boxed()
                .collect(Collectors.toMap( i -> regions.get(i), i -> this.getUser(leadIds.get(i))));

        return result;
    }

    private User getUser(String id) {
        try {
            return this.userRepository.getById(id);
        } catch (IOException e) {
            throw new RuntimeException("User with id " + id + " doesn't exist.");
        }
    }
}
