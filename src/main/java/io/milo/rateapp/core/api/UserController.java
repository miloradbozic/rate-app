package io.milo.rateapp.core.api;

import io.milo.rateapp.core.repository.IndexRepository;
import io.milo.rateapp.core.repository.UserRepository;
import io.milo.rateapp.shared.error.exception.ApiElasticResponseException;
import io.milo.rateapp.shared.error.exception.ApiEntityNotFoundException;
import io.milo.rateapp.core.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created only for the demo app, so that we can insert test users directly
 */
@RestController
@RequestMapping(value = "/api/index")
public class UserController {

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/insert", method = POST)
    public User insertUser(@RequestParam("name") String name, @RequestParam("surname") String surname,
                           @RequestParam("gender") String gender, @RequestParam("region") String region)
            throws ApiEntityNotFoundException {
        try {
            return this.userRepository.create(new User(name, surname, gender, region));
        } catch (IOException e) {
            throw new ApiEntityNotFoundException(e);
        }
    }

    @RequestMapping(value = "/recreate", method = GET)
    public String recreate() {
        try {
            indexRepository.delete();
        } catch (IOException e) {
            ; // skip, because it might not exist
        }

        try {
            indexRepository.create();
        } catch (IOException e) {
            throw new ApiElasticResponseException(e);
        }

        return "index created";
    }
}
