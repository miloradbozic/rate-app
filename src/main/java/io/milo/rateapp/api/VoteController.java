package io.milo.rateapp.api;

import io.milo.rateapp.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api/vote")
public class VoteController {

    @Autowired
    private VotingService votingService;

    @RequestMapping(value = "/add", method = POST)
    public String add(@RequestParam("voterId") String uuid, @RequestParam("voteeIds") String[] voteeIds) {
        votingService.vote(uuid, voteeIds);
        String response = "voting: " + uuid + ", ";
        response += Arrays.stream(voteeIds).collect(Collectors.joining(", "));
        return response;
    }

}
