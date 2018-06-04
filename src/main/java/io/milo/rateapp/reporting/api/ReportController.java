package io.milo.rateapp.reporting.api;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.reporting.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = "/api/report")
public class ReportController {

    @Autowired
    private ReportingService reportingService;

    @RequestMapping(value = "/user/lead", method = GET)
    public User lead() {
        return reportingService.getOverallLead();
    }

    @RequestMapping(value = "/user/lead/{region}", method = GET)
    public User leadForRegion(@PathVariable String region) {
        return reportingService.getLeadForRegion(region);
    }

    @RequestMapping(value = "/region/most-votes", method = GET)
    public String regionMostVotes() {
        return reportingService.getRegionWithMostVotes();
    }

    @RequestMapping(value = "/region/voted-most", method = GET)
    public String regionVotedMost() {
        return reportingService.getRegionWhichVotedMost();
    }

    @RequestMapping(value = "/region/leads", method = GET)
    public Map<String, User> leadsByRegion() {
        return reportingService.getLeadsForAllRegions();
    }
}
