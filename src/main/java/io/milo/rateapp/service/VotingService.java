package io.milo.rateapp.service;

import io.milo.rateapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.milo.rateapp.model.User;
import java.io.IOException;
import java.util.Arrays;

@Service
public class VotingService {

    @Autowired
    UserRepository userRepository;

    private User getUser(String id) {
        try {
            return this.userRepository.getById(id);
        } catch (IOException e) {
            throw new RuntimeException("User with id " + id + " doesn't exist.");
        }
    }

    public void vote(String voterId, String[] voteeIds) {
        try {
            Arrays.stream(voteeIds)
                    .map(id -> this.getUser(id))
                    .forEach(user -> System.out.println(user));
        } catch (RuntimeException e) {
            System.out.println("Voting transaction failed: " + e.getMessage());
        }


        // check voterId valid

        //check voteeIds valid

        // check user voting for himself

        // check user voting for the same person 2 times

        //check user vote for more than 10 peeps


    }
}
