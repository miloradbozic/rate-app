package io.milo.rateapp.service.voting.constraint;

import io.milo.rateapp.model.User;
import io.milo.rateapp.service.voting.AbstractVotingConstraint;
import io.milo.rateapp.service.voting.VotingConstraint;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;

public class CheckDuplicateVote extends AbstractVotingConstraint implements VotingConstraint {

    @Override
    public boolean check() throws ValidationException {
        Set<String> ids = new HashSet<>();
        return this.getEngine().getVotedUsers().stream().map(User::getId).allMatch(id -> {
            if (!ids.add(id)) {
                return false;
            }
            return true;
        });
    }

    @Override
    public String getValidationMessage() {
        return "Voter can not vote for the same user multiple times.";
    }
}
