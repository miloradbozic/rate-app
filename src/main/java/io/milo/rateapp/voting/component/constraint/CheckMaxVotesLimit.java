package io.milo.rateapp.voting.component.constraint;

import io.milo.rateapp.voting.component.AbstractVotingConstraint;
import io.milo.rateapp.voting.component.VotingConstraint;

import javax.validation.ValidationException;

public class CheckMaxVotesLimit extends AbstractVotingConstraint implements VotingConstraint {

    final static int MAXIMUM_VOTES = 10;
    @Override
    public boolean check() {
        if (this.getEngine().getExistingVotes().size() + this.getEngine().getVotedUsers().size() > MAXIMUM_VOTES) {
            return false;
        }
        return true;
    }

    @Override
    public String getValidationMessage() {
        return "Voter can not have more than " + MAXIMUM_VOTES + " votes";
    }
}
