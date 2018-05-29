package io.milo.rateapp.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.milo.rateapp.model.User;
import io.milo.rateapp.model.Vote;
import io.milo.rateapp.utility.ElasticJsonHelper;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Primary
@Repository
public class ElasticVoteRepository extends AbstractElasticRepository implements VoteRepository{

    @Override
    public void addVote(Vote vote) throws IOException {
        System.out.println(vote);
        ElasticJsonHelper jsonHelper = new ElasticJsonHelper();
        JsonObject params = new JsonObject();

        String inlineAction = "ctx._source.votes.add(params.vote)";
        params.addProperty("id", vote.getVotedUserId());
        params.addProperty("region", vote.getRegion());
        params.addProperty("gender", vote.getGender());
        params.addProperty("votedTime", vote.getVotedTime());

        String script = jsonHelper.getScriptJson(inlineAction, params);
        NStringEntity scriptEntity =  new NStringEntity(script, ContentType.APPLICATION_JSON);

        final String endpoint = "users/user/" + vote.getVotingUserId() + "/_update";
        this.postRequest(endpoint, scriptEntity);
    }

    @Override
    public void addVote(User votingUser, User votedUser) throws IOException {
        this.addVote(new Vote(
                votingUser.getId(),
                votedUser.getId(),
                votedUser.getGender(),
                votedUser.getRegion(),
                new DateTime().toString())
        );
    }

    @Override
    public List<Vote> getVotes(User user) throws IOException {
        final String endpoint = "users/user/" + user.getId();
        Response response = this.getRequest(endpoint);
        return this.votesFromJson(response.getEntity());
    }


    private List<Vote> votesFromJson(HttpEntity entity) throws IOException {
        Gson gson = new Gson();
        JsonObject jsonElement = gson.fromJson(EntityUtils.toString(entity), JsonObject.class);
        JsonArray votesJson = jsonElement.get("_source").getAsJsonObject().get("votes").getAsJsonArray();
        List<Vote> votes = new ArrayList<>();
        for(JsonElement voteJson : votesJson) {
            votes.add(gson.fromJson(voteJson, Vote.class));
        }
        return votes;
    }


}
