package io.milo.rateapp.service;

import io.milo.rateapp.model.Vote;
import io.milo.rateapp.repository.user.UserRepository;
import io.milo.rateapp.repository.user.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.milo.rateapp.model.User;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VotingService {

    final static int MAXIMUM_VOTES = 10;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VoteRepository voteRepository;

    public void vote(String votingUserId, String[] votedUserIds) {

        User votingUser = this.getUser(votingUserId);
        List<Vote> previousVotes = this.getVotes(votingUser);
        try {
            this.checkVoteForSelf(votingUser, votedUserIds);
            this.checkDuplicateVote(votedUserIds);
            this.checkMaxVotesLimitPassed(previousVotes, votedUserIds);

            List<User> votedUsers = Arrays.stream(votedUserIds)
                    .map(id -> this.getUser(id))
                    //.map(votedUser -> this.checkAlreadyHasVote(previousVotes, votedUser))
                    //.map(votedUser -> this.checkGenderUnequality(previousVotes, votedUser))
                    //.map(votedUser -> this.checkSameRegionTimeConstraint(votingUser, previousVotes, votedUser))
                    .collect(Collectors.toList());

            //this.checkDuplicateRegion(votedUsers);

            votedUsers.forEach( votedUser -> this.addVote(votingUser, votedUser));
        } catch (RuntimeException e) {
            //@ todo error handling
            System.out.println("Voting transaction failed: " + e.getMessage());
            e.printStackTrace();
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

    private void addVote(User voter, User voted) {
        try {
            this.voteRepository.addVote(voter, voted);
        } catch (Exception e) {
            throw new RuntimeException("Can not perform vote operation");
        }
    }

    protected User checkVoteForSelf(User voter, String[] votedIds) {
        if (Arrays.stream(votedIds).anyMatch(id -> id.equals(voter.getId()))) {
            throw new RuntimeException("Voter can not vote for himself.");
        }
        return voter;
    }

    protected void checkDuplicateVote(String[] votedIds) {
        Set<String> ids = new HashSet<>();
        Arrays.stream(votedIds).forEach(id -> {
            if (!ids.add(id)) {
                throw new RuntimeException("Voter can not vote for the same user multiple times (id " + id + " repeated).");
            }
        });
    }

    protected User checkAlreadyHasVote(List<Vote> votes, User votedUser) {
        if (votes.stream().anyMatch(v -> v.getVotedUserId().equals(votedUser.getId()))) {
            throw new RuntimeException("Voter already voted for user with id " + votedUser.getId() + ".");
        }
        return votedUser;
    }

    protected void checkMaxVotesLimitPassed(List<Vote> votes, String[] votedIds) {
        if (votes.size() + votedIds.length > MAXIMUM_VOTES) {
            throw new RuntimeException("Voter can not have more than " + MAXIMUM_VOTES + " number of votes.");
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
        /*
        if (voter.getRegion().equals(voted.getRegion()) &&
                votes.stream().filter(v -> v.getRegion().equals(voter.getRegion()))
                        .max(v -> v.getVotedTime()).
                        */
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
}
