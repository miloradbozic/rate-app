package io.milo.rateapp.voting.api;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.model.Vote;
import io.milo.rateapp.core.repository.UserRepository;
import io.milo.rateapp.shared.error.exception.ApiEntityNotFoundException;
import io.milo.rateapp.voting.repository.VoteRepository;
import io.milo.rateapp.voting.service.VotingService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api/vote")
public class VoteController {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VotingService votingService;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Voting Completed Successfully", response = ResponseEntity.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Entity Not Found"),
            @ApiResponse(code = 404, message = "Validation Exception")
    })
    @RequestMapping(value = "/add", method = POST)
    public List<Vote> add(@RequestParam("voterId") String votingUserId,
                              @RequestParam("votedIds") String[] votedUserIds) {
        User votingUser = this.getUser(votingUserId);
        List<Vote> existingVotes = this.getVotes(votingUser);
        List<User> votedUsers = this.getVotedUsers(votedUserIds);
        this.votingService.vote(votingUser, votedUsers, existingVotes);

        return this.getVotes(votingUser);
    }

    private User getUser(String id) throws ApiEntityNotFoundException {
        try {
            return this.userRepository.getById(id);
        } catch (IOException e) {
            throw new ApiEntityNotFoundException("Can not retrieve user with id " + id);
        }
    }

    private List<Vote> getVotes(User user) {
        try {
            return this.voteRepository.getVotes(user);
        } catch (IOException e) {
            throw new ApiEntityNotFoundException("Can not retrieve votes for user with id " + user.getId());
        }
    }

    private List<User> getVotedUsers(String[] votedUserIds) throws ApiEntityNotFoundException {
        return Arrays.stream(votedUserIds)
                .map(this::getUser)
                .collect(Collectors.toList());
    }

}
