package io.milo.rateapp;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.model.Vote;
import io.milo.rateapp.voting.component.VotingEngine;
import io.milo.rateapp.voting.component.constraint.CheckSameRegionTimeLimit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckSameRegionTimeLimitTests {

    @Test
    public void testCanNotVoteInTheSameRegionIfTimeLimitNotPassed() {
        VotingEngine engine = this.getVotingEngine(4);
        CheckSameRegionTimeLimit constraint = new CheckSameRegionTimeLimit();
        constraint.setEngine(engine);
        assertThat(constraint.check()).isEqualTo(false);
    }

    @Test
    public void testCanVoteInTheSameRegionIfTimeLimitHasPassed() {
        VotingEngine engine = this.getVotingEngine(6);
        CheckSameRegionTimeLimit constraint = new CheckSameRegionTimeLimit();
        constraint.setEngine(engine);
        assertThat(constraint.check()).isEqualTo(true);
    }

    private VotingEngine getVotingEngine(int minutePassedSinceVote) {
        User votingUser = new User("Marko", "Markovic", "male", "serbia");
        User previouslyVotedUser = new User("Peter", "Peterson", "male", "england");

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -minutePassedSinceVote);
        Date previousVoteTime = now.getTime();
        List<Vote> previousVotes = new ArrayList<>();
        previousVotes.add(new Vote(votingUser, previouslyVotedUser, previousVoteTime));

        List<User> votedUsers = new ArrayList<>();
        votedUsers.add(new User("John", "Johnson", "male", "england"));

        VotingEngine engine = new VotingEngine();
        engine.init(votingUser, votedUsers, previousVotes);
        return engine;
    }

}