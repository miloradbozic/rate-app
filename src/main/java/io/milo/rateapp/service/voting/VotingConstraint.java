package io.milo.rateapp.service.voting;

public interface VotingConstraint {
    boolean check();
    void setEngine(VotingEngine engine);
    String getValidationMessage();
    VotingEngine getEngine();
}
