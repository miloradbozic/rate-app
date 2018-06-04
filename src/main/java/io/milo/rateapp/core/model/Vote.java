package io.milo.rateapp.core.model;

import java.util.Date;

public class Vote {
    private String votingUserId;
    private String votedUserId;
    private String gender;
    private String region;
    private Date votedTime;

    public Vote(User votingUser, User votedUser, Date votedTime) {
        this(votingUser.getId(), votedUser.getId(), votedUser.getGender(), votedUser.getRegion(), votedTime);
    }

    public Vote(String votingUserId, String votedUserId, String gender, String region, Date votedTime) {
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

    public Date getVotedTime() {
        return votedTime;
    }

    public void setVotedTime(Date votedTime) {
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

