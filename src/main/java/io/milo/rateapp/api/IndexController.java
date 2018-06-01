package io.milo.rateapp.api;

import io.milo.rateapp.exception.NotFoundException;
import io.milo.rateapp.model.User;
import io.milo.rateapp.repository.user.IndexRepository;
import io.milo.rateapp.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created only for the demo app
 */
@RestController
@RequestMapping(value = "/api/index")
public class IndexController {

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/recreate", method = POST)
    public String recreate() {
        try {
            indexRepository.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            indexRepository.create();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ok";
    }

    @RequestMapping(value = "/insert", method = POST)
    public User insertUser(@RequestParam("name") String name, @RequestParam("surname") String surname,
                             @RequestParam("gender") String gender, @RequestParam("region") String region)
            throws NotFoundException {
        try {
            return this.userRepository.create(new User(name, surname, gender, region));
        } catch (IOException e) {
            throw new NotFoundException(e);
        }
    }
}
