package io.milo.rateapp.service.voting.constraint;

import io.milo.rateapp.model.User;
import io.milo.rateapp.service.voting.AbstractVotingConstraint;
import io.milo.rateapp.service.voting.VotingConstraint;

import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;

public class CheckVoteForSelf extends AbstractVotingConstraint implements VotingConstraint {

    @Override
    public boolean check() throws ValidationException {
        if (this.getEngine().getVotedUsers().stream()
                .map(User::getId)
                .anyMatch(id -> id.equals(this.getEngine().getVotingUser().getId()))) {
            return false;
        }

        return true;
    }

    @Override
    public String getValidationMessage() {
        return "Voter can not vote for himself";
    }
}
