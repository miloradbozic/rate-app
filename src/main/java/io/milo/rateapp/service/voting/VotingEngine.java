package io.milo.rateapp.service.voting;

import io.milo.rateapp.model.User;
import io.milo.rateapp.model.Vote;
import io.milo.rateapp.repository.user.UserRepository;
import io.milo.rateapp.repository.user.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void init(User votingUser, List<User> votedUsers, List<Vote> previousVotes) {
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
        this.check();
        this.getVotedUsers().forEach( votedUser -> {
            this.addVote(this.getVotingUser(), votedUser);
        });
    }

    public void check() throws ValidationException {
        for (VotingConstraint constraint : this.constraints) {
            if (constraint.check() == false) {
                throw new ValidationException(constraint.getValidationMessage());
            }
        }
    }

    private void addVote(User voter, User voted) {
        try {
            this.voteRepository.addVote(voter, voted);
        } catch (Exception e) {
            throw new RuntimeException("Can not perform vote operation");
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
