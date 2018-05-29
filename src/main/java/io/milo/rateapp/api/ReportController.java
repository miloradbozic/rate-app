package io.milo.rateapp.api;

import io.milo.rateapp.model.User;
import io.milo.rateapp.service.ReportingService;
import io.milo.rateapp.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api/report")
public class ReportController {

    @Autowired
    private ReportingService reportingService;
    
    @RequestMapping(value = "/user/lead", method = GET)
    public User lead() {
        User lead = reportingService.getOverallLead();
        return lead;
    }

    @RequestMapping(value = "/user/lead/{region}", method = GET)
    public User leadForRegion(@PathVariable String region) {
        User lead = reportingService.getLeadForRegion(region);
        return lead;
    }

    @RequestMapping(value = "/region/lead", method = GET)
    public String regionMostVotes() {
        String region = reportingService.getRegionWithMostVotes();
        return region;
    }

    @RequestMapping(value = "/region/voted-most", method = GET)
    public String regionVotedMost() {
        String region = reportingService.getRegionWhichVotedMost();
        return region;
    }

    @RequestMapping(value = "/region/leads", method = GET)
    public Map<String, User> leadsByRegion() {
        Map<String, User> result = reportingService.getLeadsForAllRegions();
        return result;
    }
}
