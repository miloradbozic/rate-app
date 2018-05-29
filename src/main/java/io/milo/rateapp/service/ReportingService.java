package io.milo.rateapp.service;

import io.milo.rateapp.model.User;
import io.milo.rateapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ReportingService {

    @Autowired
    UserRepository userRepository;

    public User getOverallLead() {
        User overallLead = null;
        try {
            overallLead = userRepository.getById("ab0981db-3b30-4bfc-b4bc-107c01e15791");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return overallLead;
    }

    public User getLeadForRegion(String region) {
        User overallLead = null;
        try {
            overallLead = userRepository.getById("468b8ebe-2e74-42ed-8349-3951ba1719ec");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return overallLead;
    }

    public String getRegionWithMostVotes() {
        return "england";
    }

    public String getRegionWhichVotedMost() {
        return "england";
    }

    public Map<String, User> getLeadsForAllRegions() {
        return null;
    }
}
