package io.milo.rateapp.service.voting;

public abstract class AbstractVotingConstraint implements VotingConstraint{
    private VotingEngine engine;

    public void setEngine(VotingEngine engine) {
        this.engine = engine;
    }

    public VotingEngine getEngine() {
        return this.engine;
    }

}
