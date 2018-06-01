package io.milo.rateapp.repository.user;

import io.milo.rateapp.model.User;
import io.milo.rateapp.model.Vote;

import java.io.IOException;
import java.util.List;

public interface VoteRepository {
    void addVote(Vote vote) throws IOException;
    void addVote(User voter, User voted) throws IOException;
    List<Vote> getVotes(User user) throws IOException;
}
