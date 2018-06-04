package io.milo.rateapp.voting.repository.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.model.Vote;
import io.milo.rateapp.shared.repository.elastic.rest.ElasticRestBaseRepository;
import io.milo.rateapp.shared.repository.elastic.rest.ElasticRestResponse;
import io.milo.rateapp.voting.repository.VoteRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Primary
@Repository
public class VoteRepositoryRest extends ElasticRestBaseRepository implements VoteRepository {

    @Override
    public void addVote(Vote vote) throws IOException {

        final String endpoint = "users/user/" + vote.getVotingUserId() + "/_update";

        String body = jsonBuilder()
        .startObject()
            .startObject("script")
                .field("lang", "painless")
                .field("inline", "ctx._source.votes.add(params.vote)")
                .startObject("params")
                    .startObject("vote")
                        .field("id", vote.getVotedUserId())
                        .field("region", vote.getRegion())
                        .field("gender", vote.getGender())
                        .field("votedTime", vote.getVotedTime())
                    .endObject()
                .endObject()
            .endObject()
        .endObject()
        .string();

        this.postRequest(endpoint, body);
    }

    @Override
    public void addVote(User votingUser, User votedUser) throws IOException {
        this.addVote(new Vote(
                votingUser.getId(),
                votedUser.getId(),
                votedUser.getGender(),
                votedUser.getRegion(),
                new Date())
        );
    }

    @Override
    public List<Vote> getVotes(User user) throws IOException {
        final String endpoint = "users/user/" + user.getId();
        ElasticRestResponse response = this.getRequest(endpoint);
        return this.votesFromJson(response.getBody());
    }


    private List<Vote> votesFromJson(String entity) throws IOException {
        Gson gson = new Gson();
        JsonObject jsonElement = gson.fromJson(entity, JsonObject.class);
        JsonArray votesJson = jsonElement.get("_source").getAsJsonObject().get("votes").getAsJsonArray();
        List<Vote> votes = new ArrayList<>();
        for(JsonElement voteJson : votesJson) {
            votes.add(gson.fromJson(voteJson, Vote.class));
        }
        return votes;
    }


}
