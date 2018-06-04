package io.milo.rateapp.voting.component;

public interface VotingConstraint {
    boolean check();
    void setEngine(VotingEngine engine);
    String getValidationMessage();
    VotingEngine getEngine();
}
