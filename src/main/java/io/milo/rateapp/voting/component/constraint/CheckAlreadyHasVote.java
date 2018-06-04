package io.milo.rateapp.voting.component.constraint;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.model.Vote;
import io.milo.rateapp.voting.component.AbstractVotingConstraint;
import io.milo.rateapp.voting.component.VotingConstraint;

import javax.validation.ValidationException;
import java.util.List;

public class CheckAlreadyHasVote extends AbstractVotingConstraint implements VotingConstraint {

    @Override
    public boolean check() {
        if (this.getEngine().getExistingVotes().stream()
                .anyMatch(v -> v.getVotedUserId().equals(this.getEngine().getVotingUser().getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String getValidationMessage() {
        return "Voter already voted for the user";
    }
}
