package io.milo.rateapp.service;

import io.milo.rateapp.model.User;
import io.milo.rateapp.repository.user.ReportingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ReportingService {

    @Autowired
    ReportingRepository reportingRepository;

    public User getOverallLead() {
        try {
            return reportingRepository.getUserWithMostVotes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // @todo
    }

    public String getRegionWithMostVotes() {
        try {
            return reportingRepository.getRegionWithMostVotes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRegionWhichVotedMost() {
        try {
            return reportingRepository.getRegionWhichVotedMost();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, User> getLeadsForAllRegions() {
        try {
            return reportingRepository.getLeadUsersForAllRegions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getLeadForRegion(String region) {
        return this.getLeadsForAllRegions().get(region);
    }
}
