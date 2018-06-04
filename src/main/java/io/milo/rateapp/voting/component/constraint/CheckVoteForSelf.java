package io.milo.rateapp.voting.component.constraint;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.voting.component.AbstractVotingConstraint;
import io.milo.rateapp.voting.component.VotingConstraint;

import javax.validation.ValidationException;

public class CheckVoteForSelf extends AbstractVotingConstraint implements VotingConstraint {

    @Override
    public boolean check() {
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
