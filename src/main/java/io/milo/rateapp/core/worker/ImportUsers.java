package io.milo.rateapp.core.worker;

import java.io.IOException;
import java.text.SimpleDateFormat;

import io.milo.rateapp.core.model.User;
import io.milo.rateapp.core.repository.UserRepository;
import io.milo.rateapp.core.client.UinamesUserClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 * Imports users from external source
 */
@Component
@ConditionalOnProperty("worker.importUsers.run")
public class ImportUsers {

    @Autowired
    UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(ImportUsers.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void importSingle() {
        /*User user = UinamesUserClient.getClient().getSingle(); // @todo use dependency injection
        try {
            this.userRepository.create(user);
        } catch (IOException e) {
            log.error("Error while inserting user {} to ES:\n {}", user.toString(), e.getMessage());
        }*/
    }
}
