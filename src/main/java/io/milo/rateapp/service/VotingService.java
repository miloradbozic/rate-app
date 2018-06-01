package io.milo.rateapp.service;

import io.milo.rateapp.model.Vote;
import io.milo.rateapp.repository.user.UserRepository;
import io.milo.rateapp.repository.user.VoteRepository;
import io.milo.rateapp.service.voting.VotingConstraint;
import io.milo.rateapp.service.voting.VotingEngine;
import io.milo.rateapp.service.voting.constraint.CheckDuplicateVote;
import io.milo.rateapp.service.voting.constraint.CheckVoteForSelf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.milo.rateapp.model.User;

import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotingService {

    final static int MAXIMUM_VOTES = 10;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    VotingEngine votingEngine;

    public void vote(String votingUserId, String[] votedUserIds) {
        User votingUser = this.getUser(votingUserId);
        List<Vote> previousVotes = this.getVotes(votingUser);
        List<User> votedUsers = Arrays.stream(votedUserIds)
                .map(id -> this.getUser(id))
                .collect(Collectors.toList());

        this.votingEngine.init(votingUser, votedUsers, previousVotes);
        this.votingEngine.addConstraints(new ArrayList<VotingConstraint>() {{
                add(new CheckDuplicateVote());
                add(new CheckVoteForSelf());
                add(new CheckVoteForSelf());
                add(new CheckVoteForSelf());
                add(new CheckVoteForSelf());
            }}
        );

        try {
            this.votingEngine.vote();
        } catch (ValidationException e) {
            System.out.println("Voting transaction failed: " + e.getMessage());
            //@todo return appropriate response
        }
    }

    private User getUser(String id) {
        try {
            return this.userRepository.getById(id);
        } catch (IOException e) {
            throw new RuntimeException("User with id " + id + " doesn't exist.");
        }
    }

    private List<Vote> getVotes(User user) {
        try {
            return this.voteRepository.getVotes(user);
        } catch (Exception e) {
            throw new RuntimeException("Can not retrieve votes for user with id " + user.getId() + ". " + e.getMessage());
        }
    }

    /*
    protected void checkAllConstraints(User votingUser, List<User> votedUsers, List<Vote> previousVotes) {
        this.checkVoteForSelf(votingUser, votedUsers);
        this.checkDuplicateVote(votedUsers);
        this.checkMaxVotesLimitPassed(previousVotes, votedUserIds);
        this.checkDuplicateRegion(votedUsers);
        votedUsers.stream().forEach( user -> {
            this.checkAlreadyHasVote(previousVotes, user);
            this.checkGenderUnequality(previousVotes, user);
            this.checkSameRegionTimeConstraint(votingUser, previousVotes, user);
        });
    }
    */

    /*
    protected User checkAlreadyHasVote(List<Vote> votes, User votedUser) {
        if (votes.stream().anyMatch(v -> v.getVotedUserId().equals(votedUser.getId()))) {
            throw new RuntimeException("Voter already voted for user with id " + votedUser.getId() + ".");
        }
        return votedUser;
    }

    protected void checkMaxVotesLimitPassed(List<Vote> votes, String[] votedIds) {
        if (votes.size() + votedIds.length > MAXIMUM_VOTES) {
            throw new RuntimeException("Voter can not have more than " + MAXIMUM_VOTES + " votes.");
        }
    }

    protected User checkGenderUnequality(List<Vote> votes, User votedUser) {
        String[] genders = {"male", "female"};
        for (String gender: genders) {
            if (votes.size() == MAXIMUM_VOTES-1 &&
                    votedUser.getGender() == gender &&
                    votes.stream().allMatch(vote -> vote.getGender() == gender)) {
                throw new RuntimeException("Voter can not vote for same gender in all votes.");
            }
        }
        return votedUser;
    }

    // @todo
    protected User checkSameRegionTimeConstraint(User votingUser, List<Vote> votes, User votedUser) {
        return votedUser;
    }

    protected void checkDuplicateRegion(List<User> users) {
        Set<String> regions = new HashSet<>();
        users.stream().forEach(voted -> {
            if (!regions.add(voted.getRegion())) {
                System.out.println("Can not vote for users within the same region in less than 5 minutes timeframe.");
            }
        });
    }
    */
}
