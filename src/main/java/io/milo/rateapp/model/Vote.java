package io.milo.rateapp.model;

import org.joda.time.DateTime;

public class Vote {
    private String votingUserId;
    private String votedUserId;
    private String gender;
    private String region;
    private String votedTime;

    public Vote(String votingUserId, String votedUserId, String gender, String region, String votedTime) {
        this.votingUserId = votingUserId;
        this.votedUserId = votedUserId;
        this.gender = gender;
        this.region = region;
        this.votedTime = votedTime;
    }

    public String getVotingUserId() {
        return votingUserId;
    }

    public void setVotingUserId(String votingUserId) {
        this.votingUserId = votingUserId;
    }

    public String getVotedUserId() {
        return votedUserId;
    }

    public void setVotedUserId(String votedUserId) {
        this.votedUserId = votedUserId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getVotedTime() {
        return votedTime;
    }

    public void setVotedTime(String votedTime) {
        this.votedTime = votedTime;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "votingUserId='" + votingUserId + '\'' +
                ", votedUserId='" + votedUserId + '\'' +
                ", gender='" + gender + '\'' +
                ", region='" + region + '\'' +
                ", votedTime='" + votedTime + '\'' +
                '}';
    }
}

