package io.milo.rateapp.voting.component;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.model.Vote;
import io.milo.rateapp.core.repository.UserRepository;
import io.milo.rateapp.shared.error.exception.ApiElasticResponseException;
import io.milo.rateapp.voting.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class VotingEngine {

    protected User votingUser;
    private List<User> votedUsers;
    private List<Vote> existingVotes;
    private List<VotingConstraint> constraints;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    public VotingEngine() {
        this.constraints = new ArrayList<>();
    }

    public void init(User votingUser, List<User> votedUsers, List<Vote> existingVotes) {
        this.votingUser = votingUser;
        this.votedUsers = votedUsers;
        this.existingVotes = existingVotes;
    }

    public void addConstraints(List<VotingConstraint> constraints) {
        for (VotingConstraint constraint : constraints) {
            this.addConstraint(constraint);
        }
    }

    public void addConstraint(VotingConstraint constraint) {
        constraint.setEngine(this);
        this.constraints.add(constraint);
    }

    public void vote() throws ValidationException {
        this.checkConstraints();
        this.getVotedUsers().forEach( votedUser -> {
            this.addVote(this.getVotingUser(), votedUser);
        });
    }

    public void checkConstraints() throws ValidationException {
        for (VotingConstraint constraint : this.constraints) {
            if (constraint.check() == false) {
                throw new ValidationException(constraint.getValidationMessage());
            }
        }
    }

    private void addVote(User voter, User voted) {
        try {
            this.voteRepository.addVote(voter, voted);
        } catch (IOException e) {
            throw new ApiElasticResponseException("Can not perform vote operation");
        }
    }

    public User getVotingUser() {
        return votingUser;
    }

    public List<User> getVotedUsers() {
        return votedUsers;
    }

    public List<Vote> getExistingVotes() {
        return existingVotes;
    }
}
