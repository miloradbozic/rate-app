package io.milo.rateapp.voting.service;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.model.Vote;
import io.milo.rateapp.shared.error.exception.ApiValidationException;
import io.milo.rateapp.voting.component.VotingConstraint;
import io.milo.rateapp.voting.component.VotingEngine;
import io.milo.rateapp.voting.component.constraint.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VotingService {

    @Autowired
    VotingEngine votingEngine;

    public void vote(User votingUser, List<User> votedUsers, List<Vote> previousVotes) {
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
    }
}
