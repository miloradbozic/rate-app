package io.milo.rateapp.voting.component.constraint;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.voting.component.AbstractVotingConstraint;
import io.milo.rateapp.voting.component.VotingConstraint;

import javax.validation.ValidationException;
import java.util.HashSet;
import java.util.Set;

public class CheckDuplicateVote extends AbstractVotingConstraint implements VotingConstraint {

    @Override
    public boolean check() {
        Set<String> ids = new HashSet<>();

        this.getEngine().getExistingVotes().stream().forEach( v -> {
            ids.add(v.getVotedUserId());
        });

        return this.getEngine().getVotedUsers().stream().map(User::getId).allMatch(id -> {
            if (!ids.add(id)) {
                return false;
            }
            return true;
        });
    }

    @Override
    public String getValidationMessage() {
        return "Voter can not vote for the same user multiple times";
    }
}
