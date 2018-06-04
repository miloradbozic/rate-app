package io.milo.rateapp.voting.service;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.model.Vote;
import io.milo.rateapp.shared.error.exception.ApiEntityNotFoundException;
import io.milo.rateapp.shared.error.exception.ApiValidationException;
import io.milo.rateapp.voting.component.VotingConstraint;
import io.milo.rateapp.voting.component.VotingEngine;
import io.milo.rateapp.voting.component.constraint.*;
import io.milo.rateapp.voting.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VotingService {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    VotingEngine votingEngine;

    public List<Vote>  vote(User votingUser, List<User> votedUsers) {
        List<Vote> previousVotes = this.getVotes(votingUser);
        this.votingEngine.init(votingUser, votedUsers, previousVotes);
        this.votingEngine.addConstraints(new ArrayList<VotingConstraint>() {{
            add(new CheckVoteForSelf());
            add(new CheckDuplicateVote());
            add(new CheckMaxVotesLimit());
            add(new CheckBothGenders());
            add(new CheckSameRegionTimeLimit());
        }});

        try {
            this.votingEngine.vote();
        } catch (ValidationException e) {
            throw new ApiValidationException(e.getMessage());
        }

        return this.getVotes(votingUser);
    }

    private List<Vote> getVotes(User user) {
        try {
            return this.voteRepository.getVotes(user);
        } catch (IOException e) {
            throw new ApiEntityNotFoundException("Can not retrieve votes for user with id " + user.getId());
        }
    }
}
