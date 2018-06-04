package io.milo.rateapp.reporting.service;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.reporting.repository.ReportingRepository;
import io.milo.rateapp.shared.error.exception.ApiElasticResponseException;
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
            throw new ApiElasticResponseException(e);
        }
    }

    public String getRegionWithMostVotes() {
        try {
            return reportingRepository.getRegionWithMostVotes();
        } catch (IOException e) {
            throw new ApiElasticResponseException(e);
        }
    }

    public String getRegionWhichVotedMost() {
        try {
            return reportingRepository.getRegionWhichVotedMost();
        } catch (IOException e) {
            throw new ApiElasticResponseException(e);
        }
    }

    public Map<String, User> getLeadsForAllRegions() {
        try {
            return reportingRepository.getLeadUsersForAllRegions();
        } catch (IOException e) {
            throw new ApiElasticResponseException(e);
        }
    }

    public User getLeadForRegion(String region) {
        return this.getLeadsForAllRegions().get(region);
    }
}
