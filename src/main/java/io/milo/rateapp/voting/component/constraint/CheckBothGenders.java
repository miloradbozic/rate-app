package io.milo.rateapp.voting.component.constraint;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.model.Vote;
import io.milo.rateapp.voting.component.AbstractVotingConstraint;
import io.milo.rateapp.voting.component.VotingConstraint;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckBothGenders extends AbstractVotingConstraint implements VotingConstraint {

    final static int MAXIMUM_VOTES = 10;

    @Override
    public boolean check() {
        String[] genders = {"male", "female"};

        List<String> gendersOldVotes = this.getEngine().getExistingVotes().stream().map(Vote::getGender)
                .collect(Collectors.toList());

        List<String> genderNewVotes = this.getEngine().getVotedUsers().stream().map(User::getGender).collect(Collectors.toList());

        List<String> genderVotes = Stream.concat(gendersOldVotes.stream(), genderNewVotes.stream())
                .collect(Collectors.toList());

        if (genderVotes.size() >= MAXIMUM_VOTES) {
            for (String gender : genders) {
                if (genderVotes.stream().allMatch(voteGender -> voteGender.equals(gender))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public String getValidationMessage() {
        return "Voter can not vote for same gender in all votes";
    }
}
