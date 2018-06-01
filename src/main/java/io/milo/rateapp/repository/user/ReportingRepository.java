package io.milo.rateapp.repository.user;

import io.milo.rateapp.model.User;

import java.io.IOException;
import java.util.Map;

public interface ReportingRepository {
    User getUserWithMostVotes() throws IOException;
    String getRegionWithMostVotes() throws IOException;
    String getRegionWhichVotedMost() throws IOException;
    Map<String, User> getLeadUsersForAllRegions() throws IOException;
}
