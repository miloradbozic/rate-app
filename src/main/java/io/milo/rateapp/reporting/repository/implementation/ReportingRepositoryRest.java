package io.milo.rateapp.reporting.repository.implementation;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.repository.UserRepository;
import io.milo.rateapp.reporting.repository.ReportingRepository;
import io.milo.rateapp.shared.repository.elastic.rest.ElasticRestBaseRepository;
import io.milo.rateapp.shared.repository.elastic.rest.ElasticRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Primary
@Repository
public class ReportingRepositoryRest extends ElasticRestBaseRepository implements ReportingRepository {

    private static final String USER_SEARCH_ENDPOINT = "users/user/_search";

    @Autowired
    UserRepository userRepository;

    @Override
    public User getUserWithMostVotes() throws IOException {

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
                                    .field("field", "votes.votedUserId.keyword")
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject()
            .endObject()
        .string();

        ElasticRestResponse response = this.getRequest(USER_SEARCH_ENDPOINT, body);
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

        ElasticRestResponse response = this.getRequest(USER_SEARCH_ENDPOINT, body);
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

        ElasticRestResponse response = this.getRequest(USER_SEARCH_ENDPOINT, body);
        List timesVotes = response.extract("$.aggregations.regions.buckets[*].count.value");
        int maxVoted = timesVotes.stream().mapToInt(e -> ((Double)e).intValue()).max().getAsInt();
        List maxVotedRegions = response.extract(
                "$.aggregations.regions.buckets[?(@.count.value == " + maxVoted + " )].key"
        );
        return (String)maxVotedRegions.get(0);
    }

    @Override
    public Map<String, User> getLeadUsersForAllRegions() throws IOException {

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
                                            .field("field", "votes.votedUserId.keyword")
                                        .endObject()
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject()
            .endObject()
        .string();

        ElasticRestResponse response = this.getRequest(USER_SEARCH_ENDPOINT, body);
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
            throw new RuntimeException("User with id " + id + " can't be retrieved.");
        }
    }
}
