package io.milo.rateapp.voting.component.constraint;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.voting.component.AbstractVotingConstraint;
import io.milo.rateapp.voting.component.VotingConstraint;

import javax.validation.ValidationException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CheckSameRegionTimeLimit extends AbstractVotingConstraint implements VotingConstraint {

    final static int MINUTES_BEFORE_ALLOW_SAME_REGION_VOTE = 5;

    @Override
    public boolean check() {
        Set<String> regions = new HashSet<>();

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -MINUTES_BEFORE_ALLOW_SAME_REGION_VOTE);
        Date timeLimit = now.getTime();

        this.getEngine().getExistingVotes().stream()
                .filter( v -> v.getVotedTime().compareTo(timeLimit) > 0).forEach( v -> {
            regions.add(v.getRegion());
        });

        return this.getEngine().getVotedUsers().stream().map(User::getRegion).allMatch(region -> {
            if (!regions.add(region)) {
                return false;
            }
            return true;
        });
    }

    @Override
    public String getValidationMessage() {
        return "Voter can not vote for the user in the same region within "
                + MINUTES_BEFORE_ALLOW_SAME_REGION_VOTE + " minutes";
    }
}
